import {actionAsmEditItemClick, actionAsmEdited, actionAsmViewItemClick} from "../actionAsm";
import * as types from "redux/reduxTypes";
import {FolderItemTypes} from "../../../../main/FolderContent/FolderItemTypes";

describe("ASM actions tests", () => {

    it("should create action when user clicks View button on ASM item", () => {
        const itemId = 1;

        expect(actionAsmViewItemClick(itemId)).toEqual({
            type: types.ASM_VIEW_ITEM_CLICK,
            itemId,
            itemType: FolderItemTypes.asm,
        });
    });

   it("should create action when user clicks Edit button on ASM item", () => {
       const itemId = 1;

       expect(actionAsmEditItemClick(itemId)).toEqual({
          type: types.ASM_EDIT_ITEM_CLICK,
          itemId,
          itemType: FolderItemTypes.asm,
       });
   });

   it("should create action when user finished editing ASM in designer and pressed Save", () => {
      const data = {id: 1, name: "new name"};

      expect(actionAsmEdited(data)).toEqual({
         type: types.ASM_EDITED,
         data
      });
   });
});