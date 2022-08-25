import {DATASET_FIELD_ID, FILTER_INSTANCE_FIELD_ID} from "utils/asmConstants";

export function checkFieldMappingErrors(fieldMapping) {
    return {
        [DATASET_FIELD_ID]: !Boolean(fieldMapping
            && DATASET_FIELD_ID in fieldMapping
            && fieldMapping[DATASET_FIELD_ID]),
        [FILTER_INSTANCE_FIELD_ID]:
            !Boolean(fieldMapping
                && FILTER_INSTANCE_FIELD_ID in fieldMapping
                && fieldMapping[FILTER_INSTANCE_FIELD_ID])
    };
}