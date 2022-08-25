import * as actions from "redux/actions/admin/actionAsmDesigner";
import * as types from "redux/reduxTypes";
import {
    DATASET_FIELD_ID,
    FIELD_TYPE,
    FILTER_INSTANCE_FIELDS,
    FILTER_VALUE_FIELD
} from "utils/asmConstants";

describe("ASM Designer actions tests", () => {
    it("should create an action when user changes field in ASM root", () => {
        const key = "name";
        const value = "ASM name";

        const action = actions.actionAsmDesignerChangeRootData(key, value);
        expect(action).toEqual({
            type: types.ASM_DESIGNER_CHANGE_ROOT_DATA,
            key,
            value
        });
    });

    it("should create an action when user changes field in ASM security source", () => {
        const sourceIndex = 0;
        const key = "name";
        const value = "ASM name";

        const action = actions.actionAsmDesignerChangeSecuritySourceData(sourceIndex, key, value);
        expect(action).toEqual({
            type: types.ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA,
            sourceIndex,
            key,
            value
        });
    });

    it("should create an action when user selects DataSet in ASM security source", () => {
        const sourceIndex = 0;
        const dataSet = {id: 1};

        const action = actions.actionAsmDesignerSetSecuritySourceDataSet(sourceIndex, dataSet);
        expect(action).toEqual({
            type: types.ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
            sourceIndex,
            dataSet
        });
    });

    it("should create an action when user changes selection for Field in ASM security source", () => {
        const sourceIndex = 0;
        const fieldIndex = 1;
        const fieldType = FILTER_VALUE_FIELD;
        const oldField = {
            [DATASET_FIELD_ID]: 1,
            [FIELD_TYPE]: fieldType
        };
        const newField = {
            [DATASET_FIELD_ID]: 3,
            [FIELD_TYPE]: fieldType
        };

        const action = actions.actionAsmDesignerSetSecuritySourceField(sourceIndex, fieldIndex, oldField, newField);
        expect(action).toEqual({
            type: types.ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD,
            sourceIndex,
            fieldIndex,
            oldField: {
                ...oldField,
                [FILTER_INSTANCE_FIELDS]: []
            },
            newField: {
                ...newField,
                [FILTER_INSTANCE_FIELDS]: []
            }
        });
    });

    it("should create an action when user adds new field to ASM security source", () => {
        const sourceIndex = 0;
        const fieldType = "FIELD_TYPE";
        const dataSetFieldId = 1;
        const filterInstanceFields = [1];

        let action = actions.actionAsmDesignerAddSecuritySourceField(sourceIndex, {fieldType});
        expect(action).toEqual({
            type: types.ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
            sourceIndex,
            field: {
                fieldType,
                dataSetFieldId: 0,
                filterInstanceFields: []
            }
        });

        action = actions.actionAsmDesignerAddSecuritySourceField(sourceIndex, {fieldType, dataSetFieldId});
        expect(action).toEqual({
            type: types.ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
            sourceIndex,
            field: {
                fieldType,
                dataSetFieldId,
                filterInstanceFields: []
            },
        });

        action = actions.actionAsmDesignerAddSecuritySourceField(sourceIndex, {
            fieldType,
            dataSetFieldId,
            filterInstanceFields
        });
        expect(action).toEqual({
            type: types.ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
            sourceIndex,
            field: {
                fieldType,
                dataSetFieldId,
                filterInstanceFields
            }
        });
    });

    it("should create an action when user adds new Security Filter", () => {
        const sourceIndex = 0;
        const securityFilter = {id: 1};
        const filterInstance = {id: 2};

        const action = actions.actionAsmDesignerAddSecuritySourceSecurityFilter(sourceIndex, securityFilter);
        expect(action).toEqual({
            type: types.ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER,
            sourceIndex,
            securityFilter
        });
    });

    it("should create an action when user deletes Security Filter", () => {
        const sourceIndex = 0;
        const securityFilterIndex = 1;
        const deletedFieldMappings = [];

        const action = actions.actionAsmDesignerDeleteSecuritySourceSecurityFilter(sourceIndex, securityFilterIndex, deletedFieldMappings);
        expect(action).toEqual({
            type: types.ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
            sourceIndex,
            securityFilterIndex,
            deletedFieldMappings
        });
    });

    it("should create an action when user adds new field mapping to Security Filter", () => {
        const sourceIndex = 0;
        const securityFilterIndex = 1;
        const dataSetFieldId = 4;
        const filterInstanceFieldId = 5;

        let action = actions.actionAsmDesignerAddSecurityFilterFieldMapping(sourceIndex, securityFilterIndex);
        expect(action).toEqual({
            type: types.ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex,
            securityFilterIndex,
            fieldMapping: {
                dataSetFieldId: 0,
                filterInstanceFieldId: 0
            }
        });

        action = actions.actionAsmDesignerAddSecurityFilterFieldMapping(sourceIndex, securityFilterIndex, {dataSetFieldId});
        expect(action).toEqual({
            type: types.ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex,
            securityFilterIndex,
            fieldMapping: {
                dataSetFieldId,
                filterInstanceFieldId: 0
            }
        });

        action = actions.actionAsmDesignerAddSecurityFilterFieldMapping(sourceIndex, securityFilterIndex, {
            dataSetFieldId,
            filterInstanceFieldId
        });
        expect(action).toEqual({
            type: types.ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex,
            securityFilterIndex,
            fieldMapping: {
                dataSetFieldId,
                filterInstanceFieldId
            }
        });
    });

    it("should create an action when user changes field(s) in Security Filter field mapping", () => {
        const sourceIndex = 0;
        const securityFilterIndex = 1;
        const fieldMappingIndex = 2;
        const dataSetFieldId = 11;
        const filterInstanceFieldId = 7;

        const oldFieldMapping = {
            dataSetFieldId: 10,
            filterInstanceFieldId: 5
        }

        // default action when new === old
        let action = actions.actionAsmDesignerUpdateSecurityFilterFieldMapping(sourceIndex, securityFilterIndex, fieldMappingIndex,
            oldFieldMapping);
        expect(action).toEqual({
            type: types.ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex,
            securityFilterIndex,
            fieldMappingIndex,
            oldFieldMapping: {...oldFieldMapping}, // fix circular reference
            newFieldMapping: oldFieldMapping
        });

        // when user changes dataset field
        action = actions.actionAsmDesignerUpdateSecurityFilterFieldMapping(sourceIndex, securityFilterIndex, fieldMappingIndex,
            oldFieldMapping, {dataSetFieldId});
        expect(action).toEqual({
            type: types.ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex,
            securityFilterIndex,
            fieldMappingIndex,
            oldFieldMapping,
            newFieldMapping: {...oldFieldMapping, dataSetFieldId}
        });

        // when user changes filter instance field
        action = actions.actionAsmDesignerUpdateSecurityFilterFieldMapping(sourceIndex, securityFilterIndex, fieldMappingIndex,
            oldFieldMapping, {dataSetFieldId, filterInstanceFieldId});
        expect(action).toEqual({
            type: types.ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex,
            securityFilterIndex,
            fieldMappingIndex,
            oldFieldMapping,
            newFieldMapping: {dataSetFieldId, filterInstanceFieldId}
        });
    });

    it("should create an action when Data Types are loaded successfully", () => {
        const data = {id: 1};
        const action = actions.actionAsmDesignerDataTypesLoaded(data);
        expect(action).toEqual({
            type: types.ASM_DESIGNER_DATA_TYPES_LOADED,
            data
        });
    });

    it("should create an action when Data Types are loaded with error", () => {
        const error = "some error";
        const action = actions.actionAsmDesignerDataTypesLoadFailed(error);
        expect(action).toEqual({
            type: types.ASM_DESIGNER_DATA_TYPES_LOAD_FAILED,
            error
        });
    });
});