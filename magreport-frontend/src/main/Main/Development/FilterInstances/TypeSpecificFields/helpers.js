export function getRangeFieldsLabels(filterTemplateType) {
    const allFieldLabels = {
        dateRangeFieldName: "Название поля даты в отчёте",
        dateRangeFieldDescription: "Описание поля даты в отчёте",
        dateRangeStartFieldName: "Название поля начала периода",
        dateRangeStartFieldDescription: "Описание поля начала периода",
        dateRangeEndFieldName: "Название поля окончания периода",
        dateRangeEndFieldDescription: "Описание поля окончания периода",
        rangeFieldName: "Название поля значения в отчёте",
        rangeFieldDescription: "Описание поля значения в отчёте",
        rangeStartFieldName: "Название поля начального значения",
        rangeStartFieldDescription: "Описание поля начального значения",
        rangeEndFieldName: "Название поля конечного значения",
        rangeEndFieldDescription: "Описание поля конечного значения",
    };

    return {
        fieldName: filterTemplateType === "DATE_RANGE" ? allFieldLabels.dateRangeFieldName : allFieldLabels.rangeFieldName,
        fieldDescription: filterTemplateType === "DATE_RANGE" ? allFieldLabels.dateRangeFieldDescription : allFieldLabels.rangeFieldDescription,
        startFieldName: filterTemplateType === "DATE_RANGE" ? allFieldLabels.dateRangeStartFieldName : allFieldLabels.rangeStartFieldName,
        startFieldDescription: filterTemplateType === "DATE_RANGE" ? allFieldLabels.dateRangeStartFieldDescription : allFieldLabels.rangeStartFieldDescription,
        endFieldName: filterTemplateType === "DATE_RANGE" ? allFieldLabels.dateRangeEndFieldName : allFieldLabels.rangeEndFieldName,
        endFieldDescription: filterTemplateType === "DATE_RANGE" ? allFieldLabels.dateRangeEndFieldDescription : allFieldLabels.rangeEndFieldDescription,
    };
}
