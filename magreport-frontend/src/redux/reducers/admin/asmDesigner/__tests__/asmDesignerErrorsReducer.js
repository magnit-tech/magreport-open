import {asmDesignerErrorsReducer as reducer} from "../asmDesignerErrorsReducer";
import {asmDesignerErrorsSecuritySourcesReducer} from "../asmDesignerErrorsSecuritySourcesReducer";
import * as types from "redux/reduxTypes";
import {DESCRIPTION, NAME, ROLE_TYPE_ID, SECURITY_SOURCES} from "utils/asmConstants";

jest.mock("../asmDesignerErrorsSecuritySourcesReducer", () => ({
    asmDesignerErrorsSecuritySourcesReducer: jest.fn(() => "securitySources")
}));


describe("ASM Designer Errors reducer tests", () => {
    const securitySourcesActionTypes = [
        types.ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA,
        types.ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
        types.ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
        types.ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD
    ];

    beforeEach(() => {
        asmDesignerErrorsSecuritySourcesReducer.mockClear();
    });

    it("should return new errors state on data load", () => {
        const initialState = {[NAME]: true};
        const data = {
            id: 1,
            [NAME]: "name",
            [DESCRIPTION]: "desc",
            roleType: {id: 1},
            sources: [],
        };

        const action = {type: types.ASM_DATA_LOADED, data};
        const newState = reducer(initialState, action);

        expect(newState).toEqual({
            [NAME]: false,
            [DESCRIPTION]: false,
            [ROLE_TYPE_ID]: false,
            [SECURITY_SOURCES]: "securitySources"
        });
        expect(asmDesignerErrorsSecuritySourcesReducer).toBeCalledWith([], action);
    });

    it("should check correctness of data field", () => {
        const initialState = {"name": false, "description": false};
        const key = "name";

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_CHANGE_ROOT_DATA,
            key
        });

        expect(newState).toEqual({...initialState, name: true});
        expect(asmDesignerErrorsSecuritySourcesReducer).not.toBeCalled();
    })

    it.each(securitySourcesActionTypes)(
        "should check correctness of security sources and call reducer function", (actionType) => {
            const initialState = {name: "name", securitySources: [{}]};

            const newState = reducer(initialState, {
                type: actionType
            });

            expect(newState).toEqual({...initialState, securitySources: "securitySources"});
            expect(asmDesignerErrorsSecuritySourcesReducer).toBeCalled();
        });
});
