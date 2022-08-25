import {asmDesignerDataSecuritySourcesReducer as reducer} from "../asmDesignerDataSecuritySourcesReducer";
import {asmDesignerDataSecuritySourceReducer} from "../asmDesignerDataSecuritySourceReducer";
import * as types from "redux/reduxTypes";


jest.mock("../asmDesignerDataSecuritySourceReducer", () => ({
    asmDesignerDataSecuritySourceReducer: jest.fn(() => "securitySource")
}));

describe("ASM Designer Data Security Sources (array) reducer tests", () => {
    let actionTypes = [
        types.ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA,
        types.ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
        types.ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
        types.ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD,
        types.ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER,
        types.ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
        types.ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
        types.ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING
    ];

    beforeEach(() => {
        asmDesignerDataSecuritySourceReducer.mockClear();
    });

    it("should update security sources on data load", () => {
        const source1 = {name: "source1"};
        const source2 = {name: "source2"};
        const initialState = [source1, source2];

        const data = [{id: 1, name: "new name"}, {id: 2, name: "new name2"}, {id: 3, name: "new name3"}];

        const action = {type: types.ASM_DATA_LOADED, data};
        const newState = reducer(initialState, action);

        expect(newState).toEqual(["securitySource", "securitySource", "securitySource"]);
        expect(asmDesignerDataSecuritySourceReducer).toBeCalledTimes(3);
        expect(asmDesignerDataSecuritySourceReducer).toBeCalledWith(source1, {...action, data: data[0]});
        expect(asmDesignerDataSecuritySourceReducer).toBeCalledWith(source2, {...action, data: data[1]});
        expect(asmDesignerDataSecuritySourceReducer).toBeCalledWith({}, {...action, data: data[2]});
    });

    it.each(actionTypes)("should call child reducer", (actionType) => {

        const source1 = {name: "source1"};
        const source2 = {name: "source2"};
        const initialState = [source1, source2];
        const sourceIndex = 0;

        const action = {
            type: actionType,
            sourceIndex
        };

        const newState = reducer(initialState, action);

        expect(asmDesignerDataSecuritySourceReducer).toBeCalled();
        expect(asmDesignerDataSecuritySourceReducer).toBeCalledWith(source1, action);
        expect(newState).toEqual(["securitySource", source2]);
    });

    it.each(actionTypes)("should not call child reducer if index out of bounds, should return initial state", (actionType) => {
        asmDesignerDataSecuritySourceReducer.mockClear();

        const initialState = [{}];
        const sourceIndex = 1;

        const newState = reducer(initialState, {
            type: actionType,
            sourceIndex
        });

        expect(asmDesignerDataSecuritySourceReducer).not.toBeCalled();
        expect(newState).toEqual(initialState);
    });
});