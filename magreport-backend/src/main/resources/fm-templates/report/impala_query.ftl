<#-- @ftlvariable name="" type="ru.magnit.magreportbackend.dto.inner.reportjob.ReportJobData" -->
SELECT
<#list reportData().getVisibleFields() as field><#--
-->    ${field.columnName()}<#sep>,</#sep>
</#list><#--
-->FROM
${reportData().schemaName()}.${reportData().tableName()} AS job${id()?c}
<#if !parameters().isEmpty()><#--
-->WHERE<#--

--><#macro ParseFilter filter><#--
--><#switch filter.filterType()><#--
--><#case "VALUE_LIST"><#--
-->(${filter.fieldValues().get(0).fieldValues().get(0).fieldName()} IN (${filter.getValuesForSqlIn()}))<#--
--><#break><#--
--><#case "TOKEN_INPUT"><#--
-->(${filter.fieldValues().get(0).fieldValues().get(0).fieldName()} IN (${filter.getValuesForSqlIn()}))<#--
--><#break><#--
--><#case "DATE_VALUE"><#--
-->(${filter.fieldValues().get(0).fieldValues().get(0).fieldName()} = '${filter.fieldValues().get(0).fieldValues().get(0).value()}')<#--
--><#break><#--
--><#case "DATE_RANGE"><#--
-->(${filter.fieldValues().get(0).fieldValues().get(0).fieldName()} ${filter.getImpalaSqlBetween()})<#--
--><#break><#--
--><#case "RANGE"><#--
-->(${filter.fieldValues().get(0).fieldValues().get(0).fieldName()} ${filter.getImpalaSqlBetween()})<#--
--><#break><#--
--><#case "SINGLE_VALUE_UNBOUNDED"><#--
-->(${filter.fieldValues().get(0).fieldValues().get(0).fieldName()} = ${filter.getValue()})<#--
--><#break><#--
--><#case "VALUE_LIST_UNBOUNDED"><#--
-->(${filter.fieldValues().get(0).fieldValues().get(0).fieldName()} IN (${filter.getValuesForSqlIn()}))<#--
--><#break><#--
--><#case "HIERARCHY"><#--
-->(${filter.getImpalaSqlStrict()})<#--
--><#break><#--
--><#case "HIERARCHY_M2M"><#--
-->(${filter.getImpalaSqlM2M()})<#--
--><#break><#--
--></#switch><#--
--></#macro><#--

--><#macro ParseFilterGroup filterGroup><#--
--><#list getFiltersWithSettings(filterGroup) as filter>
    <@ParseFilter getFilterData(filter.filterId())/> <#sep>${filterGroup.operationType()}</#sep><#--
--></#list><#--
--><#if isFiltersHaveSettings(filterGroup.filters()) && anyNonEmptyGroup(filterGroup.groups())> ${filterGroup.operationType()}</#if>
    <#list getNonEmptyFilterGroups(filterGroup.groups()) as group><#if isFilterGroupNotEmpty(group)> (<@ParseFilterGroup group/>) <#sep>${filterGroup.operationType()}</#sep></#if></#list><#--
--></#macro><#--

--><@ParseFilterGroup reportData().filterGroup()/><#--
--></#if>
<#if (!securityFilterParameters().isEmpty())>
    <#if (parameters().isEmpty())> WHERE <#else> AND </#if>
    <#list securityFilterParameters() as securityFilter>
        <#if (securityFilter.filterType().name() == "HIERARCHY")>(${securityFilter.getImpalaSqlStrict()})</#if>
        <#if (securityFilter.filterType().name() == "HIERARCHY_M2M")>(${securityFilter.getImpalaSqlM2M()})</#if>
        <#sep> AND </#sep>
    </#list>
</#if>
