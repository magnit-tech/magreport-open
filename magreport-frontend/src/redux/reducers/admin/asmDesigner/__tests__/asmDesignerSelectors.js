import {
    selectDataSetFieldsForSecurityFilter,
    selectFilterInstanceFields,
    fns
} from "../asmDesignerSelectors";
import {
    CHANGE_TYPE_FIELD, CODE_FIELD, DATASET_FIELD_ID,
    DATASET_ID,
    DESCRIPTION, FIELD_TYPE,
    FIELDS, FILTER_INSTANCE_FIELD_ID, FILTER_VALUE_FIELD, ID_FIELD,
    NAME,
    POST_SQL,
    PRE_SQL, ROLE_NAME_FIELD,
    SECURITY_SOURCES
} from "utils/asmConstants";

describe("selectHasErrors tests", () => {
    let state;
    beforeEach(() => {
        jest.resetModules();
        state = {
            fieldMappings: {
                "0": {
                    "1": [
                        {
                            [DATASET_FIELD_ID]: 1,
                            [FILTER_INSTANCE_FIELD_ID]: 0
                        }
                    ]
                }
            },
            errors: {
                [NAME]: false,
                [DESCRIPTION]: false,
                [SECURITY_SOURCES]: [
                    {
                        [NAME]: false,
                        [DESCRIPTION]: false,
                        [PRE_SQL]: false,
                        [POST_SQL]: false,
                        [DATASET_ID]: false,
                        [FIELDS]: [
                            {
                                [FIELD_TYPE]: CHANGE_TYPE_FIELD,
                                [DATASET_FIELD_ID]: false
                            },
                            {
                                [FIELD_TYPE]: ROLE_NAME_FIELD,
                                [DATASET_FIELD_ID]: false
                            }],
                    }],
            }
        };
    });

    it("should test selectHasErrors if state has error(s) in fieldMapping field", () => {
        jest.doMock("../checkFieldMappingErrors", () => ({
            checkFieldMappingErrors: jest.fn(() => ({dataSetFieldId: false, filterInstanceFieldId: true}))
        }));

        const testModule = require("../asmDesignerSelectors");

        return import("../checkFieldMappingErrors").then(() => {
            expect(testModule.selectHasErrors(state)).toBeTruthy();
        });
    });

    it("should test selectHasErrors if state has error(s), but not in fieldMapping field", () => {
        jest.doMock("../checkFieldMappingErrors", () => ({
            checkFieldMappingErrors: jest.fn(() => ({dataSetFieldId: false, filterInstanceFieldId: false}))
        }));

        const testModule = require("../asmDesignerSelectors");

        return import("../checkFieldMappingErrors").then(() => {
            state.fieldMappings[0][1][0][FILTER_INSTANCE_FIELD_ID] = 5;
            state.errors[NAME] = true;
            expect(testModule.selectHasErrors(state)).toBeTruthy();

            state.errors[NAME] = false;
            state.errors[SECURITY_SOURCES][0][NAME] = true;
            expect(testModule.selectHasErrors(state)).toBeTruthy();

            state.errors[SECURITY_SOURCES][0][NAME] = false;
            state.errors[SECURITY_SOURCES][0][FIELDS][1][DATASET_FIELD_ID] = true;
            expect(testModule.selectHasErrors(state)).toBeTruthy();

            state.fieldMappings = {};
            expect(testModule.selectHasErrors(state)).toBeTruthy();
        });
    });

    it("should test selectHasErrors if state has no errors", () => {
        jest.doMock("../checkFieldMappingErrors", () => ({
            checkFieldMappingErrors: jest.fn(() => ({dataSetFieldId: false, filterInstanceFieldId: false}))
        }));

        const testModule = require("../asmDesignerSelectors");

        return import("../checkFieldMappingErrors").then(() => {
            state.fieldMappings[0][1][0][FILTER_INSTANCE_FIELD_ID] = 5;
            expect(testModule.selectHasErrors(state)).toBeFalsy();
        });
    });

    it("should test selectDataSetFieldsForSecurityFilter without additional DataSets", () => {
        fns.selectDataSet = jest.fn(() => ({id: 5, fields: [{id: 10}, {id: 20}]}));
        fns.selectSecuritySource = jest.fn(() => ({
            fields: [{[FIELD_TYPE]: FILTER_VALUE_FIELD, dataSetFieldId: 10},
                {[FIELD_TYPE]: "other", dataSetFieldId: 20}]
        }));


        const state = {};
        const sourceIndex = 0;
        const securityFilterIndex = 0;

        const result = selectDataSetFieldsForSecurityFilter(state, sourceIndex, securityFilterIndex);
        expect(result).toEqual([{id: 10}]);
        expect(fns.selectDataSet).toBeCalled();
        expect(fns.selectSecuritySource).toBeCalled();
    });

    it("should test selectDataSetFieldsForSecurityFilter if DataSet in additional DataSets", () => {
        fns.selectDataSet = jest.fn(() => ({id: 5, fields: [{id: 10}, {id: 20}]}));
        fns.selectSecurityFilter = jest.fn(() => ({
            id: 1,
            dataSets: [{id: 9, dataSet: {id: 5, fields: [{id: 10}]}}],
            filterInstance: {dataSetId: 999}
        }));
        fns.selectSecuritySource = jest.fn(() => ({
            fields: [{[FIELD_TYPE]: FILTER_VALUE_FIELD, dataSetFieldId: 10},
                {[FIELD_TYPE]: "other", dataSetFieldId: 20}]
        }));


        const state = {};
        const sourceIndex = 0;
        const securityFilterIndex = 0;

        const result = selectDataSetFieldsForSecurityFilter(state, sourceIndex, securityFilterIndex);
        expect(result).toEqual([{id: 10}]);
        expect(fns.selectDataSet).toBeCalled();
    });

    it("should test selectFilterInstanceFields", () => {
        const state = {};
        const sourceIndex = 1;
        const securityFilterIndex = 2;
        fns.selectFilterInstance = jest.fn(() => ({fields: [
                {id: 1, name: "field1", type: ID_FIELD},
                {id: 2, name: "field2", type: CODE_FIELD}
            ]}));

        const result = selectFilterInstanceFields(state, sourceIndex, securityFilterIndex);
        expect(result).toEqual([{id: 2, name: "field2", type: CODE_FIELD}]);
        expect(fns.selectFilterInstance).toBeCalledWith(state, sourceIndex, securityFilterIndex);
    });
});