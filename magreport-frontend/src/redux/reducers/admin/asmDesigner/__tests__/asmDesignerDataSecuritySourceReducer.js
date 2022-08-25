import {asmDesignerDataSecuritySourceReducer as reducer} from "../asmDesignerDataSecuritySourceReducer";
import {asmDesignerDataSecuritySourceFieldsReducer} from "../asmDesignerDataSecuritySourceFieldsReducer";
import * as types from "redux/reduxTypes";
import {
    SOURCE_TYPE,
    NAME,
    DESCRIPTION,
    DATASET_ID,
    PRE_SQL,
    POST_SQL,
    FIELDS,
    SECURITY_FILTERS,
    SECURITY_FILTER_ID
} from "utils/asmConstants";

jest.mock("../asmDesignerDataSecuritySourceFieldsReducer", () => ({
    asmDesignerDataSecuritySourceFieldsReducer: jest.fn(() => "fields")
}));

describe("ASM Designer Data Security Source (item) reducer tests", () => {
    const onlyFieldsActionTypes = [
        types.ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
        types.ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD,
        types.ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
        types.ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING
    ];

    beforeEach(() => {
        asmDesignerDataSecuritySourceFieldsReducer.mockClear();
    });

    it("should update state on data load", () => {
        const initialState = {
            [SOURCE_TYPE]: "type",
            [NAME]: "name",
            [DESCRIPTION]: "description",
            [DATASET_ID]: 0,
            [PRE_SQL]: "",
            [POST_SQL]: "",
            [FIELDS]: [],
            [SECURITY_FILTERS]: []
        };

        const data = {
            id: 1,
            [SOURCE_TYPE]: "new type",
            [NAME]: "new name",
            [DESCRIPTION]: "new description",
            dataSet: {id: 1},
            [PRE_SQL]: "pre SQL",
            [POST_SQL]: "post SQL",
            [FIELDS]: [],
            securityFilters: [{id: 1, securityFilter: {id: 2}}, {id: 2, securityFilter: {id: 5}}],
            created: "date created",
            modified: "date modified"
        }

        const securityFilters = data.securityFilters.map(sf => ({id: sf.id, [SECURITY_FILTER_ID]: sf.securityFilter.id}));

        const action = {type: types.ASM_DATA_LOADED, data};

        const newState = reducer(initialState, action);

        expect(newState).toEqual({
            id: data.id,
            [SOURCE_TYPE]: data[SOURCE_TYPE],
            [NAME]: data[NAME],
            [DESCRIPTION]: data[DESCRIPTION],
            [DATASET_ID]: data.dataSet.id,
            [PRE_SQL]: data[PRE_SQL],
            [POST_SQL]: data[POST_SQL],
            [FIELDS]: "fields",
            [SECURITY_FILTERS]: securityFilters
        });

        expect(asmDesignerDataSecuritySourceFieldsReducer).toBeCalled();
        expect(asmDesignerDataSecuritySourceFieldsReducer).toBeCalledWith(initialState[FIELDS], {...action, data: data[FIELDS]});
    });

    it("should change field in security source", () => {
        const initialState = {[NAME]: "", [DESCRIPTION]: "description"};
        const key = NAME;
        const value = "ASM Security Source";

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA,
            key,
            value
        });

        expect(newState).toEqual({...initialState, [key]: value});
    });

    it("should set data set ID value from dataSet object and call Fields reducer", () => {
        const initialState = {[NAME]: "name", [FIELDS]: [], [DATASET_ID]: 0}
        const dataSet = {id: 1};

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
            dataSet
        });

        expect(newState).toEqual({...initialState, [FIELDS]: "fields", [DATASET_ID]: 1});
        expect(asmDesignerDataSecuritySourceFieldsReducer).toBeCalled();
    });

    it.each(onlyFieldsActionTypes)("should call ASM Designer Security Source Fields reducer", (actionType) => {
        const initialState = {name: "name", fields: []};

        const newState = reducer(initialState, {
            type: actionType
        });

        expect(newState).toEqual({...initialState, fields: "fields"});

        expect(asmDesignerDataSecuritySourceFieldsReducer).toBeCalled();
    });

    it("should add security filter and not call Fields reducer", () => {
        const initialState = {[NAME]: "name", [FIELDS]: [], [SECURITY_FILTERS]: [{[SECURITY_FILTER_ID]: 1}]};
        const securityFilter = {id: 2};

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER,
            securityFilter
        });

        expect(newState).toEqual({
            ...initialState,
            [SECURITY_FILTERS]: [...initialState[SECURITY_FILTERS], {[SECURITY_FILTER_ID]: 2}]});
        expect(asmDesignerDataSecuritySourceFieldsReducer).not.toBeCalled();
    });

    it("should delete security filter and call Fields reducer", () => {
        const securityFilter1 = {[SECURITY_FILTER_ID]: 1};
        const securityFilter2 = {[SECURITY_FILTER_ID]: 2};
        const securityFilter3 = {[SECURITY_FILTER_ID]: 3};
        const initialState = {[NAME]: "name", [FIELDS]: [], [SECURITY_FILTERS]: [securityFilter1, securityFilter2, securityFilter3]};
        let securityFilterIndex = 1

        let newState = reducer(initialState, {
            type: types.ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
            securityFilterIndex
        });

        expect(newState).toEqual({...initialState, [FIELDS]: "fields", [SECURITY_FILTERS]: [securityFilter1, securityFilter3]});
        expect(asmDesignerDataSecuritySourceFieldsReducer).toBeCalled();

        securityFilterIndex = 10

        newState = reducer(initialState, {
            type: types.ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
            securityFilterIndex
        });

        expect(newState).toEqual(initialState);
        expect(asmDesignerDataSecuritySourceFieldsReducer).toBeCalled();
    });
});