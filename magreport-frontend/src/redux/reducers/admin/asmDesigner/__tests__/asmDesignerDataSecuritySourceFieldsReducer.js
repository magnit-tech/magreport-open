import {asmDesignerDataSecuritySourceFieldsReducer as reducer} from "../asmDesignerDataSecuritySourceFieldsReducer";
import * as types from "redux/reduxTypes";
import {
    FIELD_TYPE,
    DATASET_FIELD_ID,
    FILTER_INSTANCE_FIELDS,
    CHANGE_TYPE_FIELD,
    ROLE_NAME_FIELD,
    FILTER_VALUE_FIELD, FILTER_INSTANCE_FIELD_ID
} from "utils/asmConstants";
import {
    ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
    ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
    ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING
} from "redux/reduxTypes";

let changeTypeField;
let roleNameField;
let filterValueFieldFirst;
let filterValueFieldSecond;
let initialState;


describe("ASM Designer Security Source Fields reducer tests", () => {
    beforeEach(() => {
        changeTypeField = {
            [FIELD_TYPE]: CHANGE_TYPE_FIELD,
            [DATASET_FIELD_ID]: 1,
            [FILTER_INSTANCE_FIELDS]: []
        };

        roleNameField = {
            [FIELD_TYPE]: ROLE_NAME_FIELD,
            [DATASET_FIELD_ID]: 4,
            [FILTER_INSTANCE_FIELDS]: []
        };

        filterValueFieldFirst = {
            [FIELD_TYPE]: FILTER_VALUE_FIELD,
            [DATASET_FIELD_ID]: 4,
            [FILTER_INSTANCE_FIELDS]: [{[FILTER_INSTANCE_FIELD_ID]: 1}, {[FILTER_INSTANCE_FIELD_ID]: 2}]
        };

        filterValueFieldSecond = {
            [FIELD_TYPE]: FILTER_VALUE_FIELD,
            [DATASET_FIELD_ID]: 5,
            [FILTER_INSTANCE_FIELDS]: []
        };

        initialState = [
            changeTypeField,
            roleNameField,
            filterValueFieldFirst,
            filterValueFieldSecond,
        ];
    });

    it("should update fields on data load", () => {
       const data = [{
           id: 1,
           [FIELD_TYPE]: "new type",
           [FILTER_INSTANCE_FIELDS]: [
               {id: 1, filterInstanceField: {id: 1}},
               {id: 2, filterInstanceField: {id: 2}},
               {id: 3, filterInstanceField: {id: 3}}],
           dataSetField: {
               id: 2
           },
           created: "created date",
           modified: "modified date"
       }];

       const action = {type: types.ASM_DATA_LOADED, data};

       const newState = reducer(initialState, action);

       expect(newState).toEqual([{
           id: 1,
           [FIELD_TYPE]: "new type",
           [FILTER_INSTANCE_FIELDS]: [
               {id: 1, [FILTER_INSTANCE_FIELD_ID]: 1},
               {id: 2, [FILTER_INSTANCE_FIELD_ID]: 2},
               {id: 3, [FILTER_INSTANCE_FIELD_ID]: 3}
           ],
           [DATASET_FIELD_ID]: 2
       }]);
    });

    it("should cleanup field selections when new DataSet is selected", () => {

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
            dataSet: {id: 1, name: "does not matter"}
        });

        expect(newState).toHaveLength(3);
        expect(newState).toEqual([
            {
                [FIELD_TYPE]: CHANGE_TYPE_FIELD,
                [DATASET_FIELD_ID]: 0,
                [FILTER_INSTANCE_FIELDS]: []
            },
            {
                [FIELD_TYPE]: ROLE_NAME_FIELD,
                [DATASET_FIELD_ID]: 0,
                [FILTER_INSTANCE_FIELDS]: []
            },
            {
                [FIELD_TYPE]: FILTER_VALUE_FIELD,
                [DATASET_FIELD_ID]: 0,
                [FILTER_INSTANCE_FIELDS]: []
            },
        ]);
    });

    it("should set field value and reset filter instance fields selection", () => {
        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD,
            fieldIndex: 2, // filter value field with filter instance fields
            oldField: filterValueFieldFirst,
            newField: {
                ...filterValueFieldFirst,
                [DATASET_FIELD_ID]: 8
            }
        });

        expect(newState).toEqual([
            changeTypeField,
            roleNameField,
            {
                [FIELD_TYPE]: FILTER_VALUE_FIELD,
                [DATASET_FIELD_ID]: 8,
                [FILTER_INSTANCE_FIELDS]: []
            },
            filterValueFieldSecond,
        ]);
    });

    it("should delete FILTER_VALUE_FIELD on empty selection (id === 0", () => {
        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD,
            fieldIndex: 2, // filter value field with filter instance fields
            oldField: filterValueFieldFirst,
            newField: {
                ...filterValueFieldFirst,
                [DATASET_FIELD_ID]: 0
            }
        });

        expect(newState).toEqual([
            changeTypeField,
            roleNameField,
            filterValueFieldSecond,
        ]);
    });

    it("should append new security source field", () => {
        const newField = {
            [FIELD_TYPE]: FILTER_VALUE_FIELD,
            [DATASET_FIELD_ID]: 5,
            [FILTER_INSTANCE_FIELDS]: []
        };

        const newState = reducer(initialState, {
            type: ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
            field: newField
        });

        expect(newState).toEqual([...initialState, newField]);
    });

    it("should change field mapping for FILTER_VALUE_FIELD when filter instance field selected for the first time", () => {
        const oldFieldMapping = {[DATASET_FIELD_ID]: 4, [FILTER_INSTANCE_FIELD_ID]: 0};
        const newFieldMapping = {[DATASET_FIELD_ID]: 4, [FILTER_INSTANCE_FIELD_ID]: 6};

        const newState = reducer(initialState, {
            type: ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex: 0,
            securityFilterIndex: 1,
            fieldMappingIndex: 3,
            oldFieldMapping,
            newFieldMapping
        });

        expect(newState).toEqual([changeTypeField,
            roleNameField,
            {
                ...filterValueFieldFirst,
                [FILTER_INSTANCE_FIELDS]: [
                    ...filterValueFieldFirst[FILTER_INSTANCE_FIELDS],
                    {[FILTER_INSTANCE_FIELD_ID]: 6}
                ]
            }, filterValueFieldSecond]);
    });

    it("should change field mapping for FILTER_VALUE_FIELD when filter instance field changed", () => {
        const oldFieldMapping = {[DATASET_FIELD_ID]: 4, [FILTER_INSTANCE_FIELD_ID]: 2};
        const newFieldMapping = {[DATASET_FIELD_ID]: 4, [FILTER_INSTANCE_FIELD_ID]: 6};

        const newState = reducer(initialState, {
            type: ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex: 0,
            securityFilterIndex: 1,
            fieldMappingIndex: 3,
            oldFieldMapping,
            newFieldMapping
        });

        expect(newState).toEqual([changeTypeField,
            roleNameField,
            {
                ...filterValueFieldFirst,
                [FILTER_INSTANCE_FIELDS]: [
                    {[FILTER_INSTANCE_FIELD_ID]: 1},
                    {[FILTER_INSTANCE_FIELD_ID]: 6}
                ]
            }, filterValueFieldSecond]);
    });

    it("should remove field mapping for FILTER_VALUE_FIELD when filter instance field ID = 0", () => {
        const oldFieldMapping = {[DATASET_FIELD_ID]: 4, [FILTER_INSTANCE_FIELD_ID]: 2};
        const newFieldMapping = {[DATASET_FIELD_ID]: 4, [FILTER_INSTANCE_FIELD_ID]: 0};

        const newState = reducer(initialState, {
            type: ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex: 0,
            securityFilterIndex: 1,
            fieldMappingIndex: 3,
            oldFieldMapping,
            newFieldMapping
        });

        expect(newState).toEqual([changeTypeField,
            roleNameField,
            {
                ...filterValueFieldFirst,
                [FILTER_INSTANCE_FIELDS]: [{[FILTER_INSTANCE_FIELD_ID]: 1}]
            }, filterValueFieldSecond]);
    });

    it("should remove field mapping for FILTER_VALUE_FIELD when dataset field ID changes to nonexistent ID", () => {
        const oldFieldMapping = {[DATASET_FIELD_ID]: 4, [FILTER_INSTANCE_FIELD_ID]: 2};
        const newFieldMapping = {[DATASET_FIELD_ID]: 999, [FILTER_INSTANCE_FIELD_ID]: 2};

        const newState = reducer(initialState, {
            type: ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex: 0,
            securityFilterIndex: 1,
            fieldMappingIndex: 3,
            oldFieldMapping,
            newFieldMapping
        });

        expect(newState).toEqual([changeTypeField,
            roleNameField,
            {
                ...filterValueFieldFirst,
                [FILTER_INSTANCE_FIELDS]: [{[FILTER_INSTANCE_FIELD_ID]: 1}]
            }, filterValueFieldSecond]);
    });

    it("should move field mapping to another FILTER_VALUE_FIELD when dataset field ID changes", () => {
        const oldFieldMapping = {[DATASET_FIELD_ID]: 4, [FILTER_INSTANCE_FIELD_ID]: 2};
        const newFieldMapping = {[DATASET_FIELD_ID]: 5, [FILTER_INSTANCE_FIELD_ID]: 2};

        const newState = reducer(initialState, {
            type: ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex: 0,
            securityFilterIndex: 1,
            fieldMappingIndex: 3,
            oldFieldMapping,
            newFieldMapping
        });

        expect(newState).toEqual([changeTypeField,
            roleNameField,
            {
                [FIELD_TYPE]: FILTER_VALUE_FIELD,
                [DATASET_FIELD_ID]: 4,
                [FILTER_INSTANCE_FIELDS]: [{[FILTER_INSTANCE_FIELD_ID]: 1}]
            },
            {
                ...filterValueFieldSecond,
                [FILTER_INSTANCE_FIELDS]: [
                    ...filterValueFieldSecond[FILTER_INSTANCE_FIELDS],
                    {[FILTER_INSTANCE_FIELD_ID]: 2}
                ]
            }
        ]);
    });

    it("should do nothing when added partial field mapping", () => {
        const fieldMapping = {[DATASET_FIELD_ID]: 4, [FILTER_INSTANCE_FIELD_ID]: 0};

        const newState = reducer(initialState, {
            type: ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex: 0,
            securityFilterIndex: 1,
            fieldMapping
        });

        expect(newState).toEqual(initialState);
    });

    it("should create field mapping in rare cases, when added field mapping with both fields provided", () => {
        const fieldMapping = {[DATASET_FIELD_ID]: 4, [FILTER_INSTANCE_FIELD_ID]: 7};

        const newState = reducer(initialState, {
            type: ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex: 0,
            securityFilterIndex: 1,
            fieldMapping
        });

        expect(newState).toEqual([changeTypeField,
            roleNameField,
            {
                [FIELD_TYPE]: FILTER_VALUE_FIELD,
                [DATASET_FIELD_ID]: 4,
                [FILTER_INSTANCE_FIELDS]: [
                    ...filterValueFieldFirst[FILTER_INSTANCE_FIELDS],
                    {[FILTER_INSTANCE_FIELD_ID]: 7}
                ]
            }, filterValueFieldSecond]);
    });

    it("should delete field mappings when security filter is deleted", () => {
        const deletedFieldMappings = [
            {[DATASET_FIELD_ID]: 4, [FILTER_INSTANCE_FIELD_ID]: 1}, // exists in initialState
            {[DATASET_FIELD_ID]: 5, [FILTER_INSTANCE_FIELD_ID]: 2}, // FILTER_VALUE_FIELD exists in initialState, but mapping is not
            {[DATASET_FIELD_ID]: 10, [FILTER_INSTANCE_FIELD_ID]: 3} // mapping does not exist among FILTER_VALUE_FIELD
        ];


        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
            deletedFieldMappings
        });

        expect(newState).toEqual([
            changeTypeField,
            roleNameField,
            {
                ...filterValueFieldFirst,
                [FILTER_INSTANCE_FIELDS]: [{[FILTER_INSTANCE_FIELD_ID]: 2}]
            },
            filterValueFieldSecond
        ]);
    });
});