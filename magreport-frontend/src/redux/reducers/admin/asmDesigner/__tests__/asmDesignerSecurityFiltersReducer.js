import {asmDesignerSecurityFiltersReducer as reducer} from "../asmDesignerSecurityFiltersReducer";
import * as types from "redux/reduxTypes";


describe("ASM Designer Security Filters reducer tests", () => {

    it("should update security filters on data load", () => {
        const initialState = {securityFilters: {"10": {}}};
        const data = {sources: [
                {securityFilters: [{id: 1, securityFilter: {id: 5}}, {id: 1, securityFilter: {id: 6}}]}
            ]};

        const newState = reducer(initialState, {
            type: types.ASM_DATA_LOADED,
            data
        });

        expect(newState).toEqual({
                "0": [{id: 5}, {id: 6}]
            });
    });

    it("should add new security filter", () => {
        const initialState = {};
        const sourceIndex = 1;
        const securityFilter = {id: 2, filterInstance: {id: 1}};

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER,
            sourceIndex,
            securityFilter
        });

        expect(newState).toEqual({
            [sourceIndex]: [securityFilter]
        });
    });

    it("should delete security filter", () => {
        const initialState = {
            "2": [
                {id: 2, filterInstance: {id: 1}},
                {id: 3, filterInstance: {id: 2}}
            ]
        };

        const sourceIndex = 2;
        const securityFilterIndex = 0;

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
            sourceIndex,
            securityFilterIndex
        });

        expect(newState).toEqual({
            "2": [
                {id: 3, filterInstance: {id: 2}}
            ]
        });
    });

    it("should not change state if arguments for delete action are out of bounds", () => {
        const initialState = {
            "2": [
                {id: 2, filterInstance: {id: 1}},
                {id: 3, filterInstance: {id: 2}}
            ]
        };

        let sourceIndex = 2;
        let securityFilterIndex = 4;

        let newState = reducer(initialState, {
            type: types.ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
            sourceIndex,
            securityFilterIndex
        })

        expect(newState).toEqual(initialState);

        sourceIndex = 3;
        securityFilterIndex = 0;

        newState = reducer(initialState, {
            type: types.ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
            sourceIndex,
            securityFilterIndex
        })

        expect(newState).toEqual(initialState);
    });
})