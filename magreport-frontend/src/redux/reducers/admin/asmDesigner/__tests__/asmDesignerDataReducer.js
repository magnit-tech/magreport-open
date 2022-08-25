import {asmDesignerDataReducer as reducer} from "../asmDesignerDataReducer";
import {asmDesignerDataSecuritySourcesReducer} from "../asmDesignerDataSecuritySourcesReducer";
import * as types from "redux/reduxTypes";


jest.mock("../asmDesignerDataSecuritySourcesReducer", () => ({
    asmDesignerDataSecuritySourcesReducer: jest.fn(() => "newSecuritySources")
}));

describe("ASM Designer Data reducer tests", () => {

    it("should change data in root object", () => {
        const initialState = {name: "ASM Name", description: ""};
        const key = "description";
        const value = "ASM description";

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_CHANGE_ROOT_DATA,
            key,
            value
        });
        expect(newState).toEqual({
            ...initialState, [key]: value
        });
    });

    it("should update data in root object on data load", () => {
       const initialState = {
           name: "",
           description: "",
           roleTypeId: null,
           securitySources: []
       };

       const id = 1;
       const data = {
           id,
           name: "name",
           description: "description",
           roleType: {id: 1},
           sources: [{id: 1}, {id: 2}]
       };

       const action = {
           type: types.ASM_DATA_LOADED,
           data
       };

       const newState = reducer(initialState, action);

       expect(newState).toEqual({
           id,
           name: data.name,
           description: data.description,
           roleTypeId: data.roleType.id,
           securitySources: "newSecuritySources"});
        expect(asmDesignerDataSecuritySourcesReducer).toBeCalled();
        expect(asmDesignerDataSecuritySourcesReducer).toBeCalledWith(initialState.securitySources, {...action, data: data.sources});
    });

    it.each([
        types.ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA,
        types.ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
        types.ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD,
        types.ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
        types.ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER,
        types.ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
        types.ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
        types.ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING
    ])("should call ASM Designer Security Sources reducer", (actionType) => {
        asmDesignerDataSecuritySourcesReducer.mockClear()

        const initialState = {securitySources: []};
        const action = {
            type: actionType
        };
        reducer(initialState, action);

        expect(asmDesignerDataSecuritySourcesReducer).toBeCalled();
        expect(asmDesignerDataSecuritySourcesReducer).toBeCalledWith(initialState.securitySources, action);
    });
})