import {asmDesignerErrorsSecuritySourceReducer as reducer} from "../asmDesignerErrorsSecuritySourceReducer";
import * as types from "redux/reduxTypes";
import {
    DATASET_FIELD_ID,
    DESCRIPTION,
    FIELD_TYPE,
    FIELDS,
    FILTER_VALUE_FIELD,
    NAME,
    POST_SQL,
    PRE_SQL,
    DATASET_ID,
    USER_NAME_FIELD, CHANGE_TYPE_FIELD, ROLE_NAME_FIELD
} from "utils/asmConstants";

const initialState = {
    [NAME]: false,
    [DESCRIPTION]: true,
    [DATASET_ID]: true,
    [FIELDS]: [
        {
            [FIELD_TYPE]: USER_NAME_FIELD,
            [DATASET_FIELD_ID]: true
        },
        {
            [FIELD_TYPE]: FILTER_VALUE_FIELD,
            [DATASET_FIELD_ID]: false
        }
    ]
}

describe("ASM Designer Errors of Security Source (item) reducer tests", () => {

    it("should return new state on data load", () => {
        const initialState = {[NAME]: true};
        const data = {
            id: 1,
            [NAME]: "newname",
            [DESCRIPTION]: "desc",
            [PRE_SQL]: "",
            [POST_SQL]: "select 1",
            dataSet: {id: 1},
            [FIELDS]: [{
                id: 1,
                [FIELD_TYPE]: CHANGE_TYPE_FIELD,
                dataSetField: {id: 2}
            },
            {
                id: 2,
                [FIELD_TYPE]: ROLE_NAME_FIELD,
                dataSetField: {id: 3}
            }]
        };

        const action = {type: types.ASM_DATA_LOADED, data};
        const newState = reducer(initialState, action);

        expect(newState).toEqual({
            [NAME]: false,
            [DESCRIPTION]: false,
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
        });

    });

    it("should update top-level field invalidity", () => {
        const key = DESCRIPTION;
        const value = "value";

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA,
            key,
            value
        });

        expect(newState).toEqual({...initialState, [key]: false});
    });

    it("should update dataSet field invalidity", () => {
        let dataSet = {id: 1}

        let newState = reducer(initialState, {
            type: types.ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
            dataSet
        });

        expect(newState).toEqual({...initialState, [DATASET_ID]: false});

        dataSet = undefined;

        newState = reducer(initialState, {
            type: types.ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
            dataSet
        });

        expect(newState).toEqual(initialState);
    });

    it("should add new field validity", () => {
        const fieldType = FILTER_VALUE_FIELD;
        let dataSetFieldId = 1;

        let newState = reducer(initialState, {
            type: types.ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
            field: {
                fieldType,
                dataSetFieldId
            },
        });

        expect(newState).toEqual({
            ...initialState,
            [FIELDS]: [...initialState[FIELDS], {
                [FIELD_TYPE]: fieldType,
                [DATASET_FIELD_ID]: false
            }]
        });
    });

    it("should not create field entry for empty fields (id===0)", () => {
        const fieldType = FILTER_VALUE_FIELD;
        const dataSetFieldId = 0;
        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
            field: {
                fieldType,
                dataSetFieldId
            },
        });

        expect(newState).toEqual(initialState);
    })

    it("should update field invalidity", () => {
        const fieldIndex = 0;
        const dataSetFieldId = 5;

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD,
            fieldIndex,
            newField: {
                dataSetFieldId
            }
        });

        const fields = initialState[FIELDS].slice();
        fields[fieldIndex][DATASET_FIELD_ID] = false;

        expect(newState).toEqual({
            ...initialState,
            [FIELDS]: fields
        });
    });

    it("should delete field entry for deleted (empty) FIELD_VALUE_FIELD", () => {
        const fieldIndex = 1;
        const dataSetFieldId = 0;

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD,
            fieldIndex,
            newField: {
                dataSetFieldId
            }
        });

        const fields = initialState[FIELDS].slice();
        fields.splice(fieldIndex, 1);

        expect(newState).toEqual({
            ...initialState,
            [FIELDS]: fields
        });
    });

    it("should skip postSql and preSql validation (return same state", () => {
        const postSql = "postSql";
        const preSql = "";

        let newState = reducer(initialState, {
            type: types.ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA,
            key: POST_SQL,
            value: postSql
        });

        expect(newState).toEqual(initialState);

        newState = reducer(initialState, {
            type: types.ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA,
            key: PRE_SQL,
            value: preSql
        });

        expect(newState).toEqual(initialState);
    });

});