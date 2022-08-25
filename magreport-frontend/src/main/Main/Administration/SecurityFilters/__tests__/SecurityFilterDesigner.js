import React from "react";
import SecurityFilterDesigner from "../SecurityFilterDesigner";
import {
    screen, render, cleanup, wait,
    queryByLabelText, userEvent, queryByText, getAllByRole, findByText, findByLabelText
} from "test-utils";

import dataHub from "ajax/DataHub";

describe("SecurityFilterDesigner tests", () => {
    const filterInstancesData = {
        success: true,
        message: "",
        data: {
            id: null,
            parentId: null,
            name: "root",
            childFolders: [],
            filterInstances: [
                {
                    id: 1,
                    name: "instance",
                    dataSetId: 1,
                    type: {
                        name: "instancetype"
                    },
                    fields: [{
                        id: 1,
                        dataSetFieldId: 1,
                        name: "instance_field",
                        type: "CODE_FIELD"
                    }]
                }
            ]
        }
    };

    const dataSetsData = {
        success: true,
        message: "",
        data: {
            id: null,
            parentId: null,
            name: "root",
            childFolders: [],
            dataSets: [{
                    id: 1,
                    name: "dataset",
                    fields: [{
                            id: 1,
                            name: "dataset_field"
                    }]
            }]
        }
    };

    const securityFilterData = {
        success: true,
        message: "",
        data: {
            id: 1,
            filterInstance: {
                id: 1,
                folderId: null
            }
        }
    };

    const roleSettingsData = {
        success: true,
        message: "",
        data: {
            securityFilterId: 1,
            roleSettings: []
        }
    };

    beforeAll(() => {
        // mock user info
        jest.spyOn(dataHub.localCache, "getUserInfo").mockImplementation(() => {
            return {
                isAdmin: true,
                isDeveloper: true
            };
        });

        // mock API responses
        jest.spyOn(window, "fetch").mockImplementation((url, data) => {
            let responseData = {};
            if (url.endsWith("/filter-instance/get-folder")) {
                responseData = filterInstancesData;
            } else if (url.endsWith("/dataset/get-folder")) {
                responseData = dataSetsData;
            } else if (url.endsWith("/security-filter/add")) {
                responseData = securityFilterData;
            } else if (url.endsWith("/security-filter/get-role-settings")) {
                responseData = roleSettingsData;
            }
            return Promise.resolve({
                ok: true,
                json: () => Promise.resolve(responseData)
            });
        })
    });

    afterEach(cleanup);

    afterAll(() => {
        dataHub.localCache.getUserInfo.mockRestore();
        window.fetch.mockRestore();
    })

    it("should test activation of Roles tab", async () => {
        const {container} = render((
            <SecurityFilterDesigner
                mode={"create"}
            />));

        const rolesTab = queryByText(container, "Привязка роли");
        expect(rolesTab).not.toBeNull();

        let rolesPanel = queryByText(container, "Роли");
        expect(rolesPanel).toBeNull();

        //click Roles tab when form is empty - should not change tabs
        await userEvent.click(rolesTab);
        await wait(() => {
            rolesPanel = queryByText(container, "Роли");
            expect(rolesPanel).toBeNull();
        });

        // set values on Filter Details tab
        const detailsInputs = getAllByRole(container, "textbox");
        const nameInput = detailsInputs[0];
        const descriptionInput = detailsInputs[1];
        let openFolderButton = queryByLabelText(container, "openFolders");
        await userEvent.type(nameInput, "filter");
        await userEvent.type(descriptionInput, "description");

        //select Filter Instance
        userEvent.click(openFolderButton);
        const instanceSelectionDialog = screen.getByRole("dialog");
        const filterInstanceCard = await findByText(instanceSelectionDialog, "instance");
        userEvent.click(filterInstanceCard);

        //click Roles tab when Filter details are populated - should switch to Roles tab
        userEvent.click(rolesTab);
        await wait(() => {
            rolesPanel = screen.queryByText("Роли");
            expect(rolesPanel).not.toBeNull();
        });

        //
        // Switch to DataSets tab
        const dataSetsTab = queryByText(container, "Наборы данных");
        userEvent.click(dataSetsTab);

        let addDataSetButton = await findByLabelText(container, "add");

        userEvent.click(addDataSetButton);

        // click Roles tab - should fail because DataSet is not selected
        userEvent.click(rolesTab);
        await wait(() => {
            rolesPanel = queryByText(container, "Роли");
            expect(rolesPanel).toBeNull();
        });

        // select DataSet
        openFolderButton = queryByLabelText(container, "openFolders");
        await userEvent.click(openFolderButton);
        const dataSetSelectionDialog = screen.getByRole("dialog");
        const dataSetCard = await findByText(dataSetSelectionDialog, "dataset");
        userEvent.click(dataSetCard);

        // click Roles tab - should fail because FilterInstance field is not mapped to DataSet field
        userEvent.click(rolesTab);
        await wait(() => {
            rolesPanel = queryByText(container, "Роли");
            expect(rolesPanel).toBeNull();
        });

        // TODO: case when DataSet field is mapped to FilterInstance field
        // select DataSet field
        // let dataSetFieldSelect;
        // await wait(() => {
        //     const btns = getAllByRole(container, "button");
        //     for (let btn of btns) {
        //         if (btn.id === "idSelectInputLabel") {
        //             dataSetFieldSelect = btn;
        //             break;
        //         }
        //     }
        // });
        // fireEvent.mouseDown(dataSetFieldSelect);

        // click Roles tab - success, because DataSet tab is valid
        // userEvent.click(rolesTab);
        // await wait(() => {
        //     rolesPanel = queryByText(container, "Роли");
        //     expect(rolesPanel).not.toBeNull();
        // });
    });
});
