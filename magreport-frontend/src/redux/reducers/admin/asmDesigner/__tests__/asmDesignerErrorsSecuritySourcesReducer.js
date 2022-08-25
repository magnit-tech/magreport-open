import {asmDesignerErrorsSecuritySourcesReducer as reducer} from "../asmDesignerErrorsSecuritySourcesReducer";
import {asmDesignerErrorsSecuritySourceReducer} from "../asmDesignerErrorsSecuritySourceReducer";
import * as types from "redux/reduxTypes";

jest.mock("../asmDesignerErrorsSecuritySourceReducer", () => ({
    asmDesignerErrorsSecuritySourceReducer: jest.fn(() => "securitySource")
}));

describe("ASM Designer Errors of Security Sources reducer tests", () => {
    const actionTypes = [
        types.ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA,
        types.ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
        types.ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
        types.ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD
    ];

    beforeEach(() => {
        asmDesignerErrorsSecuritySourceReducer.mockClear();
    })

    it.each(actionTypes)("should call child reducer", (actionType) => {
        const initialState = [{}, {}];
        const sourceIndex = 0;

        const newState = reducer(initialState, {
            type: actionType,
            sourceIndex
        });

        expect(newState).toEqual(["securitySource", {}]);
        expect(asmDesignerErrorsSecuritySourceReducer).toBeCalled();
    });

    it.each(actionTypes)("should not call child reducer when index out of bounds", (actionType) => {
       const initialState = [{}, {}];
       const sourceIndex = 10;

        const newState = reducer(initialState, {
            type: actionType,
            sourceIndex
        });

        expect(newState).toEqual(initialState);
        expect(asmDesignerErrorsSecuritySourceReducer).not.toBeCalled();
    });

    it("should return new state on data load", () => {
        const initialState = [{name: true}];
        const source1 = {id: 1};
        const source2 = {id: 2};
        const data = {sources: [source1, source2]};
        const action = {type: types.ASM_DATA_LOADED, data};
        const newState = reducer(initialState, action);

        expect(newState).toEqual(["securitySource", "securitySource"]);
        expect(asmDesignerErrorsSecuritySourceReducer).toBeCalledWith({}, {...action, data: source1});
        expect(asmDesignerErrorsSecuritySourceReducer).toBeCalledWith({}, {...action, data: source2});
        expect(asmDesignerErrorsSecuritySourceReducer).toBeCalledTimes(2);
    })
});