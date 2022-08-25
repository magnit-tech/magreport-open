import {asmDesignerDataSetsReducer as reducer} from "../asmDesignerDataSetsReducer";
import * as types from "redux/reduxTypes";

describe("ASM Designer DataSets Reducer tests", () => {
    it("should update DataSets list on data load", () => {
        const initialState = [null, null, null];
        const dataSets = [{id: 1}, null, {id: 3}];
        const data = {
            sources: dataSets.map(ds => ({dataSet: ds}))
        };

        const newState = reducer(initialState, {
            type: types.ASM_DATA_LOADED,
            data
        });

        expect(newState).toEqual([{id: 1}, {}, {id: 3}]);
    });

    it("should set DataSet", () => {
        const initialState = [null, null, null];
        const sourceIndex = 1;
        const dataSet = {id: 1, name: "dataSet"};

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
            sourceIndex,
            dataSet
        });
        expect(newState).toEqual([null, dataSet, null]);
    });

    // just in case
    it("should add DataSet in new slot", () => {
        const initialState = [null, null, null];
        const sourceIndex = 3;
        const dataSet = {id: 1, name: "dataSet"};

        const newState = reducer(initialState, {
            type: types.ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
            sourceIndex,
            dataSet
        });
        expect(newState).toEqual([null, null, null, dataSet]);
    });
});