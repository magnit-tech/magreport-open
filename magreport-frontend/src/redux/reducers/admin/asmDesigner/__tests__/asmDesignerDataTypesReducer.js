import {asmDesignerDataTypesReducer as reducer} from "../asmDesignerDataTypesReducer";
import * as types from "redux/reduxTypes";

describe("ASM Designer Data Types reducer tests", () => {
    it("should update state after successful load", () => {
        const initialState = {"3": {id: 3, name: "BYTES"}};
        const data = [
            {id: 1, name: "INTEGER"},
            {id: 2, name: "CHAR"}
        ];

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_DATA_TYPES_LOADED,
            data
        });

        expect(newState).toEqual({
            ...initialState,
            "1": {id: 1, name: "INTEGER"},
            "2": {id: 2, name: "CHAR"}
        });
    });
});