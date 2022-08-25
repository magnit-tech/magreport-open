import {checkFieldMappingErrors} from "../checkFieldMappingErrors";
import {DATASET_FIELD_ID, FILTER_INSTANCE_FIELD_ID} from "../../../../../utils/asmConstants";


describe("checkFieldMappingErrors function tests", () => {
    it.each([
        {},
        {[DATASET_FIELD_ID]: 0},
        {[DATASET_FIELD_ID]: 0, [FILTER_INSTANCE_FIELD_ID]: 0}
    ])(
        "should test function with fully invalid field mappings", (value) => {
            expect(checkFieldMappingErrors(value)).toEqual({
                [DATASET_FIELD_ID]: true,
                [FILTER_INSTANCE_FIELD_ID]: true
            });
        });

    it.each([
        {[FILTER_INSTANCE_FIELD_ID]: 1},
        {[DATASET_FIELD_ID]: 0, [FILTER_INSTANCE_FIELD_ID]: 1}
    ])(
        "should test function where field mappings have only dataset field invalid", (value) => {
            expect(checkFieldMappingErrors(value)).toEqual({
                [DATASET_FIELD_ID]: true,
                [FILTER_INSTANCE_FIELD_ID]: false
            });
        });

    it.each([
        {[DATASET_FIELD_ID]: 1},
        {[DATASET_FIELD_ID]: 1, [FILTER_INSTANCE_FIELD_ID]: 0}
    ])(
        "should test function where field mappings have only filter instance field invalid", (value) => {
            expect(checkFieldMappingErrors(value)).toEqual({
                [DATASET_FIELD_ID]: false,
                [FILTER_INSTANCE_FIELD_ID]: true
            });
        });

    it.each([
        {[DATASET_FIELD_ID]: 1, [FILTER_INSTANCE_FIELD_ID]: 1}
    ])(
        "should test function with correct field mappings", (value) => {
            expect(checkFieldMappingErrors(value)).toEqual({
                [DATASET_FIELD_ID]: false,
                [FILTER_INSTANCE_FIELD_ID]: false
            });
        });
})