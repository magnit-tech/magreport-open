import {asmDesignerRootReducer as reducer} from "../asmDesignerRootReducer";
import {asmDesignerDataReducer} from "../asmDesignerDataReducer";
import {asmDesignerErrorsReducer} from "../asmDesignerErrorsReducer";
import {asmDesignerDataSetsReducer} from "../asmDesignerDataSetsReducer";
import {asmDesignerSecurityFiltersReducer} from "../asmDesignerSecurityFiltersReducer";
import {asmDesignerFieldMappingsReducer} from "../asmDesignerFieldMappingsReducer";
import {asmDesignerDataTypesReducer} from "../asmDesignerDataTypesReducer";
import * as types from "redux/reduxTypes";

jest.mock("../asmDesignerDataReducer", () => ({
    asmDesignerDataReducer: jest.fn(() => "data")
}));

jest.mock("../asmDesignerErrorsReducer", () => ({
    asmDesignerErrorsReducer: jest.fn(() => "errors")
}));

jest.mock("../asmDesignerDataSetsReducer", () => ({
    asmDesignerDataSetsReducer: jest.fn(() => "dataSets")
}));

jest.mock("../asmDesignerSecurityFiltersReducer", () => ({
    asmDesignerSecurityFiltersReducer: jest.fn(() => "securityFilters")
}));

jest.mock("../asmDesignerFieldMappingsReducer", () => ({
    asmDesignerFieldMappingsReducer: jest.fn(() => "fieldMappings")
}));

jest.mock("../asmDesignerDataTypesReducer", () => ({
    asmDesignerDataTypesReducer: jest.fn(() => "dataTypes")
}));

describe("ASM Designer root reducer tests", () => {
    const actionTypes = [
        types.ASM_DATA_LOADED,
        types.ASM_DESIGNER_CHANGE_ROOT_DATA,
        types.ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA,
        types.ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
        types.ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
        types.ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD,
        types.ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER,
        types.ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
        types.ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
        types.ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING,
        types.ASM_DESIGNER_DATA_TYPES_LOADED,
        types.ASM_DESIGNER_DATA_TYPES_LOAD_FAILED
    ];

    const mockedReducers = [
        asmDesignerDataReducer,
        asmDesignerErrorsReducer,
        asmDesignerDataSetsReducer,
        asmDesignerSecurityFiltersReducer,
        asmDesignerFieldMappingsReducer,
        asmDesignerDataTypesReducer
    ];

    it.each([
        types.ASM_ADDED,
        types.ASM_EDITED
    ])("should return initial state when user finished adding/editing ASM", (actionType) => {
        const state = {
            data: {},
            errors: {}
        };

        const newState = reducer(state, {
            type: actionType
        });

        expect(newState).not.toEqual(state);
        expect(newState.data).toBeInstanceOf(Object);
        expect(newState.errors).toBeInstanceOf(Object);
        expect(newState.dataSets).toBeInstanceOf(Array);
        expect(newState.securityFilters).toBeInstanceOf(Object);
        expect(newState.fieldMappings).toBeInstanceOf(Object);
        expect(newState.dataTypes).toBeInstanceOf(Object);
    });

    it.each(actionTypes)("should call descendant reducers", (actionType) => {
        mockedReducers.forEach(mock => mock.mockClear());

        const initialState = {};

        const newState = reducer(initialState, {
            type: actionType
        });

        expect(newState).toEqual({
            data: "data",
            errors: "errors",
            dataSets: "dataSets",
            securityFilters: "securityFilters",
            fieldMappings: "fieldMappings",
            dataTypes: "dataTypes"
        });

        mockedReducers.forEach(mock => expect(mock).toBeCalled());
    });
})