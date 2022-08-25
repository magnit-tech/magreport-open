import {asmDesignerFieldMappingsReducer as reducer} from "../asmDesignerFieldMappingsReducer";
import * as types from "redux/reduxTypes";
import {
    CHANGE_TYPE_FIELD,
    DATASET_FIELD_ID,
    FIELD_TYPE,
    FILTER_INSTANCE_FIELD_ID,
    FILTER_INSTANCE_FIELDS,
    FILTER_VALUE_FIELD
} from "utils/asmConstants";


describe("ASM Designer Field Mappings reducer tests", () => {
    let initialState;
    beforeEach(() => {
        initialState = {
            "1": {
                "1": [{[DATASET_FIELD_ID]: 1, [FILTER_INSTANCE_FIELD_ID]: 1}]
            },
            "2": {
                "2": []
            }
        };
    });

    //TODO: mock createFieldMappingsFromResponse
    //TODO: move this code to createFieldMappingsFromResponse test
    it("should update state on data load", () => {
        const data = {
            sources: [
                {
                    fields: [{
                        [FIELD_TYPE]: CHANGE_TYPE_FIELD
                    }],
                    securityFilters: []
                },
                {
                    fields: [
                        {
                            dataSetField: {id: 9},
                            [FIELD_TYPE]: CHANGE_TYPE_FIELD,
                        }, {
                            dataSetField: {id: 11},
                            [FIELD_TYPE]: FILTER_VALUE_FIELD,
                            filterInstanceFields: [
                                {id: 1, filterInstanceField: {id: 22}},
                                {id: 5, filterInstanceField: {id: 15}}
                            ]
                        },
                        {
                            dataSetField: {id: 5},
                            [FIELD_TYPE]: FILTER_VALUE_FIELD,
                            filterInstanceFields: [
                                {id: 6, filterInstanceField: {id: 10}}
                            ]
                        },
                        {
                            dataSetField: {id: 44},
                            [FIELD_TYPE]: FILTER_VALUE_FIELD,
                            filterInstanceFields: [
                                {id: 7, filterInstanceField: {id: 33}}
                            ]
                        },
                    ],
                    securityFilters: [
                        {id: 1, securityFilter: {id: 9, filterInstance: {id: 8, fields: [{id: 9}, {id: 10}, {id: 15}]}}},
                        {id: 2, securityFilter: {id: 11, filterInstance: {id: 43, fields: [{id: 33}, {id: 22}, {id: 90}]}}}
                    ]
                },
            ]
        };

        const newState = reducer(initialState, {type: types.ASM_DATA_LOADED, data});

        expect(newState).toEqual({
            "1": {
                "0": [{
                    [DATASET_FIELD_ID]: 11,
                    [FILTER_INSTANCE_FIELD_ID]: 15
                },{
                    [DATASET_FIELD_ID]: 5,
                    [FILTER_INSTANCE_FIELD_ID]: 10
                }],
                "1": [{
                    [DATASET_FIELD_ID]: 11,
                    [FILTER_INSTANCE_FIELD_ID]: 22
                }, {
                    [DATASET_FIELD_ID]: 44,
                    [FILTER_INSTANCE_FIELD_ID]: 33
                }]
            },
        });
    });

    it("should add new field mapping", () => {
        let sourceIndex = 1;
        let securityFilterIndex = 1;
        const fieldMapping = {[DATASET_FIELD_ID]: 0, [FILTER_INSTANCE_FIELD_ID]: 2};
        const initialFilterMappings = initialState[sourceIndex][securityFilterIndex];

        let newState = reducer(initialState, {
            type: types.ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex,
            securityFilterIndex,
            fieldMapping
        });


        expect(newState).toEqual({
            ...initialState,
            [sourceIndex]: {
                ...initialState[sourceIndex],
                [securityFilterIndex]: [...initialFilterMappings, fieldMapping]
            }
        });

        sourceIndex = 1;
        securityFilterIndex = 2;

        newState = reducer(initialState, {
            type: types.ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex,
            securityFilterIndex,
            fieldMapping
        });

        expect(newState).toEqual({
            ...initialState,
            [sourceIndex]: {
                ...initialState[sourceIndex],
                [securityFilterIndex]: [fieldMapping]
            }
        });

        sourceIndex = 3;
        securityFilterIndex = 0;

        newState = reducer(initialState, {
            type: types.ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex,
            securityFilterIndex,
            fieldMapping
        });

        expect(newState).toEqual({
            ...initialState,
            [sourceIndex]: {
                [securityFilterIndex]: [fieldMapping]
            }
        });
    });

    it("should not add new field mapping if both fields are empty", () => {
        let sourceIndex = 1;
        let securityFilterIndex = 1;
        const fieldMapping = {[DATASET_FIELD_ID]: 0, [FILTER_INSTANCE_FIELD_ID]: 0};

        let newState = reducer(initialState, {
            type: types.ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex,
            securityFilterIndex,
            fieldMapping
        });
        expect(newState).toEqual(initialState);
    });

    it("should update field mapping", () => {
        const sourceIndex = 1;
        const securityFilterIndex = 1;
        const fieldMappingIndex = 0;
        const newFieldMapping = {[DATASET_FIELD_ID]: 1, [FILTER_INSTANCE_FIELD_ID]: 2};

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex,
            securityFilterIndex,
            fieldMappingIndex,
            oldFieldMapping: {},
            newFieldMapping
        });

        expect(newState).toEqual({
            ...initialState,
            [sourceIndex]: {
                ...initialState[sourceIndex],
                [securityFilterIndex]: [newFieldMapping]
            }
        });
    });

    it("should delete field mapping if both fields are empty", () => {
        const sourceIndex = 1;
        const securityFilterIndex = 1;
        const fieldMappingIndex = 0;
        const newFieldMapping = {[DATASET_FIELD_ID]: 0, [FILTER_INSTANCE_FIELD_ID]: 0};

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex,
            securityFilterIndex,
            fieldMappingIndex,
            oldFieldMapping: {},
            newFieldMapping
        });

        expect(newState).toEqual({
            ...initialState,
            [sourceIndex]: {
                ...initialState[sourceIndex],
                [securityFilterIndex]: []
            }
        });
    });

    it("should do nothing to state if UPDATE action parameters are out of bounds", () => {
        let sourceIndex = 3;
        let securityFilterIndex = 1;
        let fieldMappingIndex = 0;
        const newFieldMapping = {[DATASET_FIELD_ID]: 5, [FILTER_INSTANCE_FIELD_ID]: 5};

        let newState = reducer(initialState, {
            type: types.ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex,
            securityFilterIndex,
            fieldMappingIndex,
            oldFieldMapping: {},
            newFieldMapping
        });

        expect(newState).toEqual(initialState);

        sourceIndex = 1;
        securityFilterIndex = 2;
        fieldMappingIndex = 0;

        newState = reducer(initialState, {
            type: types.ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex,
            securityFilterIndex,
            fieldMappingIndex,
            oldFieldMapping: {},
            newFieldMapping
        });

        expect(newState).toEqual(initialState);

        sourceIndex = 1;
        securityFilterIndex = 1;
        fieldMappingIndex = 2;

        newState = reducer(initialState, {
            type: types.ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING,
            sourceIndex,
            securityFilterIndex,
            fieldMappingIndex,
            oldFieldMapping: {},
            newFieldMapping
        });

        expect(newState).toEqual(initialState);
    });

    it("should delete field mappings when security filter is deleted", () => {
        const sourceIndex = 1;
        const securityFilterIndex = 1;
        const deletedFieldMappings = [];

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
            sourceIndex,
            securityFilterIndex,
            deletedFieldMappings
        });

        expect(newState).toEqual({
            ...initialState,
            [sourceIndex]: {}
        });
    });

    it("should do nothing to state if DELETE security filter parameters are out of bounds", () => {
        let sourceIndex = 3;
        let securityFilterIndex = 1;
        const deletedFieldMappings = [];

        let newState = reducer(initialState, {
            type: types.ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
            sourceIndex,
            securityFilterIndex,
            deletedFieldMappings
        });

        expect(newState).toEqual(initialState);

        sourceIndex = 1;
        securityFilterIndex = 5;
        newState = reducer(initialState, {
            type: types.ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
            sourceIndex,
            securityFilterIndex,
            deletedFieldMappings
        });

        expect(newState).toEqual(initialState);
    });

    it("should remove selection of Data Set Field when FILTER_VALUE_FIELD selection changes", () => {
        const sourceIndex = 1;
        const fieldIndex = 2;
        const oldField = {
            [FIELD_TYPE]: FILTER_VALUE_FIELD,
            [DATASET_FIELD_ID]: 1,
            [FILTER_INSTANCE_FIELDS]: [1, 2]
        };

        const newField = {
            ...oldField,
            [DATASET_FIELD_ID]: 2
        };

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD,
            sourceIndex,
            fieldIndex,
            oldField,
            newField
        });

        expect(newState).toEqual({
            ...initialState,
            "1": {
                "1": [{[DATASET_FIELD_ID]: 0, [FILTER_INSTANCE_FIELD_ID]: 1}]
            },
        });
    });
});