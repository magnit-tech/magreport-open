package ru.magnit.magreportbackend.service.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.magnit.magreportbackend.domain.olap.OlapConfiguration;
import ru.magnit.magreportbackend.domain.olap.ReportOlapConfiguration;
import ru.magnit.magreportbackend.domain.reportjob.ReportJob;
import ru.magnit.magreportbackend.domain.user.User;
import ru.magnit.magreportbackend.dto.request.olap.ReportOlapConfigAddRequest;
import ru.magnit.magreportbackend.dto.request.olap.ReportOlapConfigSetShareRequest;
import ru.magnit.magreportbackend.dto.request.olap.UsersReceivedMyJobsRequest;
import ru.magnit.magreportbackend.dto.response.olap.OlapAvailableConfigurationsResponse;
import ru.magnit.magreportbackend.dto.response.olap.OlapConfigResponse;
import ru.magnit.magreportbackend.dto.response.olap.ReportOlapConfigResponse;
import ru.magnit.magreportbackend.dto.response.user.UserShortInfoResponse;
import ru.magnit.magreportbackend.exception.InvalidParametersException;
import ru.magnit.magreportbackend.mapper.auth.UserShortInfoResponseMapper;
import ru.magnit.magreportbackend.mapper.olap.OlapConfigurationMapper;
import ru.magnit.magreportbackend.mapper.olap.OlapConfigurationMerger;
import ru.magnit.magreportbackend.mapper.olap.OlapConfigurationResponseMapper;
import ru.magnit.magreportbackend.mapper.olap.ReportOlapConfigurationCloner;
import ru.magnit.magreportbackend.mapper.olap.ReportOlapConfigurationMapper;
import ru.magnit.magreportbackend.mapper.olap.ReportOlapConfigurationMerger;
import ru.magnit.magreportbackend.mapper.olap.ReportOlapConfigurationResponseMapper;
import ru.magnit.magreportbackend.repository.OlapConfigurationRepository;
import ru.magnit.magreportbackend.repository.ReportOlapConfigurationRepository;
import ru.magnit.magreportbackend.util.Pair;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OlapConfigurationDomainService {


    private final OlapConfigurationRepository repository;
    private final ReportOlapConfigurationRepository reportOlapConfigurationRepository;
    private final OlapConfigurationMapper olapConfigurationMapper;
    private final OlapConfigurationMerger olapConfigurationMerger;
    private final OlapConfigurationResponseMapper olapConfigurationResponseMapper;
    private final ReportOlapConfigurationMapper reportOlapConfigurationMapper;
    private final ReportOlapConfigurationResponseMapper reportOlapConfigurationResponseMapper;

    private final ReportOlapConfigurationCloner reportOlapConfigurationCloner;

    private final ReportOlapConfigurationMerger reportOlapConfigurationMerger;
    private final UserShortInfoResponseMapper userShortInfoResponseMapper;

    @Transactional
    public Long updateReportOlapConfiguration(ReportOlapConfigAddRequest request, Long currentUser) {

        ReportOlapConfiguration config;

        if (request.getReportOlapConfigId() != null) {
            var saveConfig = reportOlapConfigurationRepository.getReferenceById(request.getReportOlapConfigId());

            if (saveConfig.getOlapConfiguration().getUser().getId().equals(currentUser))
                config = reportOlapConfigurationMerger.merge(saveConfig, request);
            else throw new InvalidParametersException("Only the creator can update the configuration");

        } else {
            config = reportOlapConfigurationMapper.from(request);
            var configData = olapConfigurationMapper.from(request.getOlapConfig());
            configData.setUser(new User(currentUser));
            config.setOlapConfiguration( repository.save(configData));
        }

        if (Boolean.TRUE.equals(request.getIsDefault()) && request.getJobId() == null)
            disableDefaultFlagReportConfig(request.getReportId(), request.getUserId());
        else
            config.setIsDefault(false);

        if(Boolean.TRUE.equals(request.getIsShare()) && request.getJobId() == null)
            config.setIsShared(false);

        config.setCreator(new User(currentUser));

        return reportOlapConfigurationRepository.save(config).getId();

    }

    @Transactional
    public OlapConfigResponse getOlapConfiguration(Long id) {
        return olapConfigurationResponseMapper.from(repository.getReferenceById(id));
    }

    @Transactional
    public ReportOlapConfigResponse getReportOlapConfiguration(Long id) {
        return reportOlapConfigurationResponseMapper.from(reportOlapConfigurationRepository.getReferenceById(id));
    }

    @Transactional
    public List<UserShortInfoResponse> getListUsersReceivedAuthorJob(UsersReceivedMyJobsRequest request, Long currentUser) {

        var mapUsers = new HashMap<String, UserShortInfoResponse>();

        var users = reportOlapConfigurationRepository.
                getReportOlapConfigurationByCreatorIdAndCreatedDateTimeAfter(currentUser, LocalDateTime.now().minusDays(request.getLastDays()))
                .stream()
                .map(ReportOlapConfiguration::getUser)
                .map(userShortInfoResponseMapper::from)
                .toList();

        users.forEach(user -> {
            if (!mapUsers.containsKey(user.getLogin())) mapUsers.put(user.getLogin(), user);
        });

        var result = users.stream()
                .collect(Collectors.groupingBy(UserShortInfoResponse::getLogin, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
                .sorted((o1, o2) -> o2.getR().intValue() - o1.getR().intValue())
                .map(Pair::getL)
                .limit(request.getTopN())
                .toList();

        return result.stream().map(mapUsers::get).toList();
    }

    @Transactional
    public void setDefaultReportConfiguration(Long reportConfigId) {

        var newDefaultReportConfig = reportOlapConfigurationRepository.getReferenceById(reportConfigId);

        if (newDefaultReportConfig.getReportJob() == null) {
            disableDefaultFlagReportConfig(
                    newDefaultReportConfig.getReport() == null ? null : newDefaultReportConfig.getReport().getId(),
                    newDefaultReportConfig.getUser() == null ? null : newDefaultReportConfig.getUser().getId());

            newDefaultReportConfig.setIsDefault(true);
            reportOlapConfigurationRepository.save(newDefaultReportConfig);
        } else throw new InvalidParametersException("Configuration can be set by default only at the report level");
    }

    @Transactional
    public void deleteOlapConfiguration(Long id, Long currentUser) {
        var config = repository.getReferenceById(id);

        if (currentUser.equals(config.getUser().getId())) {
            var reportConfigs = reportOlapConfigurationRepository.getReportOlapConfigurationsByOlapConfigurationId(id);
            reportOlapConfigurationRepository.deleteAll(reportConfigs);
            repository.deleteById(id);
        } else throw new InvalidParametersException("Deleting a configuration is available only to the author");

    }

    @Transactional
    public void deleteReportOlapConfiguration(Long id, Long currentUser) {
        var config = reportOlapConfigurationRepository.getReferenceById(id);

        if (currentUser.equals(config.getCreator().getId()))
            reportOlapConfigurationRepository.deleteById(id);

        else throw new InvalidParametersException("Deleting a configuration is available only to the author");
    }

    @Transactional
    public void updateSharedStatusOlapReportConfig(ReportOlapConfigSetShareRequest request, Long currentUser) {
        var config = reportOlapConfigurationRepository.getReferenceById(request.getReportOlapConfigId());

        if (currentUser.equals(config.getOlapConfiguration().getUser().getId())) {
            config.setIsShared(request.getShare());
            reportOlapConfigurationRepository.save(config);
        } else
            throw new InvalidParametersException("Update shared status a configuration is available only to the author");

    }

    @Transactional
    public OlapAvailableConfigurationsResponse getAvailableReportOlapConfigurationForJob(Long jobId, Long reportId, Long currentUser) {

        var commonReportConfigs = reportOlapConfigurationRepository.getROCByReport(reportId);
        var myReportConfigs = reportOlapConfigurationRepository.getROCByReportAndUser(reportId, currentUser);
        var myJobConfigs = reportOlapConfigurationRepository.getROCByUserAndJob(currentUser, jobId);
        var sharedJobConfig = reportOlapConfigurationRepository.getSharedROCByJob(jobId);

        return new OlapAvailableConfigurationsResponse()
                .setCommonReportConfigs(reportOlapConfigurationResponseMapper.from(commonReportConfigs))
                .setMyReportConfigs(reportOlapConfigurationResponseMapper.from(myReportConfigs))
                .setMyJobConfig(reportOlapConfigurationResponseMapper.from(myJobConfigs))
                .setSharedJobConfig(reportOlapConfigurationResponseMapper.from(sharedJobConfig));

    }

    @Transactional
    public Long getCurrentConfiguration(Long jobId, Long reportId, Long currentUser) {

        var config = reportOlapConfigurationRepository.findCurrentReportOlapConfiguration(jobId, currentUser);

        if (config.isPresent()) {
            return config.get().getId();

        } else {
            var currentConfig = findTemplateForCurrentConfiguration(jobId, reportId, currentUser);
            currentConfig.setIsCurrent(true);
            currentConfig.setIsDefault(false);
            currentConfig.setIsShared(false);
            currentConfig.setReportJob(new ReportJob(jobId));
            currentConfig.setReport(null);
            currentConfig.setUser(new User(currentUser));
            currentConfig.setCreator(new User(currentUser));
            currentConfig.getOlapConfiguration().setUser(new User(currentUser));
            currentConfig.setOlapConfiguration(repository.save(currentConfig.getOlapConfiguration()));

            return reportOlapConfigurationRepository.save(currentConfig).getId();
        }
    }

    @Transactional
    public void createCurrentConfigurationForUsers(List<Long> users, Long jobId, Long currentUser){

        var config = reportOlapConfigurationRepository.findCurrentReportOlapConfiguration(jobId, currentUser);

        config.ifPresent(reportOlapConfiguration -> users.forEach(user -> {
            var currentUserConfig = reportOlapConfigurationCloner.clone(reportOlapConfiguration);

            currentUserConfig.setIsDefault(false);
            currentUserConfig.setIsShared(false);
            currentUserConfig.setIsCurrent(true);
            currentUserConfig.setReport(null);
            currentUserConfig.setUser(new User(user));
            currentUserConfig.setReportJob(new ReportJob(jobId));
            currentUserConfig.setCreator(new User(user));
            currentUserConfig.getOlapConfiguration().setUser(new User(user));
            currentUserConfig.setOlapConfiguration(repository.save(currentUserConfig.getOlapConfiguration()));
            reportOlapConfigurationRepository.save(currentUserConfig);
        }));
    }

    @Transactional
   public void deleteCurrentConfigurationUsers(List<Long> users, Long jobId){
        users.forEach(user -> reportOlapConfigurationRepository.deleteCurrentByUserAndJob(jobId,user));
    }

    private void disableDefaultFlagReportConfig(Long reportId, Long userId) {
        if (userId == null) reportOlapConfigurationRepository.disableDefaultFlagForReport(reportId);
        else reportOlapConfigurationRepository.disableDefaultFlagForReportAndUser(reportId, userId);
    }

    private ReportOlapConfiguration findTemplateForCurrentConfiguration(Long jobId, Long reportId, Long currentUser) {

        var myReportConfigs = reportOlapConfigurationRepository.getROCByReportAndUser(reportId, currentUser);
        var myReportConfig = myReportConfigs.stream().filter(ReportOlapConfiguration::getIsDefault).findFirst();
        if (myReportConfig.isPresent()) return reportOlapConfigurationCloner.clone(myReportConfig.get());

        var commonReportConfigs = reportOlapConfigurationRepository.getROCByReport(reportId);
        var commonConfig = commonReportConfigs.stream().filter(ReportOlapConfiguration::getIsDefault).findFirst();
        if (commonConfig.isPresent()) return reportOlapConfigurationCloner.clone(commonConfig.get());

        return new ReportOlapConfiguration()
                .setReportJob(new ReportJob(jobId))
                .setOlapConfiguration(
                        new OlapConfiguration()
                                .setData("")
                                .setName("Current configuration")
                                .setDescription("")
                                .setCreatedDateTime(LocalDateTime.now())
                                .setModifiedDateTime(LocalDateTime.now()));

    }

}
