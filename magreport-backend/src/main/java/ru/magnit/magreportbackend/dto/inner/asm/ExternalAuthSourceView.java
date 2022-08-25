package ru.magnit.magreportbackend.dto.inner.asm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldTypeEnum;
import ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceTypeEnum;
import ru.magnit.magreportbackend.dto.inner.dataset.DataSetView;

import java.util.List;
import java.util.Map;

import static ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldTypeEnum.CHANGE_TYPE_FIELD;
import static ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldTypeEnum.FILTER_VALUE_FIELD;
import static ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldTypeEnum.ROLE_NAME_FIELD;
import static ru.magnit.magreportbackend.domain.asm.ExternalAuthSourceFieldTypeEnum.USER_NAME_FIELD;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ExternalAuthSourceView {

    private Long id;
    private ExternalAuthSourceTypeEnum type;
    private DataSetView dataSet;
    private String preSql;
    private String postSql;
    private Map<ExternalAuthSourceFieldTypeEnum, List<ExternalAuthSourceFieldView>> fields;
    private List<ExternalAuthSecurityFilterView> filters;

    public ExternalAuthSourceFieldView getChangeTypeField() {
        return fields.get(CHANGE_TYPE_FIELD).get(0);
    }

    public ExternalAuthSourceFieldView getRoleNameField() {
        return fields.get(ROLE_NAME_FIELD).get(0);
    }

    public ExternalAuthSourceFieldView getUserNameField() {
        return fields.get(USER_NAME_FIELD).get(0);
    }

    public List<ExternalAuthSourceFieldView> getFilterSettingsFields() {
        return fields.get(FILTER_VALUE_FIELD);
    }
}
