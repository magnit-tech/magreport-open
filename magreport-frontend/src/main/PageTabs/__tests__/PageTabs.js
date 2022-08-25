import React from "react";
import PageTabs from "../PageTabs";
import Typography from "@material-ui/core/Typography";
import { render, queryByText } from "test-utils";

describe("PageTabs tests", () => {
    const tab1Label = "tab1";
    const tab2Label = "tab2";
    const tab3Label = "tab3";
    const tab1Text = "Tab 1";
    const tab2Text = "Tab 2";
    const tab3Text = "Tab 3";
    let tabsData;

    beforeEach(() => {
        tabsData = [
            {
                tablabel: tab1Label,
                tabcontent: <Typography>{tab1Text}</Typography>
            }, {
                tablabel: tab2Label,
                tabcontent: <Typography>{tab2Text}</Typography>
            }, {
                tablabel: tab3Label,
                tabcontent: <Typography>{tab3Text}</Typography>
            }
        ];
    });

    it("should test rendering PageTabs with 3 tabs, first tab is default", () => {
        const pageTitle = "Test tabs"
        const { container } = render((
            <PageTabs
                pageName={pageTitle}
                tabsdata={tabsData}
            />));

        const pageTitleContainer = queryByText(container, pageTitle);
        expect(pageTitleContainer).not.toBeNull();

        const firstTabContent = queryByText(container, tab1Text);
        expect(firstTabContent).not.toBeNull();
        const secondTabContent = queryByText(container, tab2Text);
        expect(secondTabContent).toBeNull();
        const thirdTabContent = queryByText(container, tab3Text);
        expect(thirdTabContent).toBeNull();

        const firstTabLabel = queryByText(container, tab1Label);
        expect(firstTabLabel).not.toBeNull();
        const secondTabLabel = queryByText(container, tab2Label);
        expect(secondTabLabel).not.toBeNull();
        const thirdTabLabel = queryByText(container, tab3Label);
        expect(thirdTabLabel).not.toBeNull();
    });

    it("should test default tab ID behaviour", () => {
        tabsData[0]["tabdisabled"] = true;

        const { container } = render((<PageTabs
            tabsdata={tabsData}
        />));

        const firstTabContent = queryByText(container, tab1Text);
        expect(firstTabContent).toBeNull();
        const secondTabContent = queryByText(container, tab2Text);
        expect(secondTabContent).not.toBeNull();

    });

    it("should test switching between tabs", () => {
        const { container } = render((
            <PageTabs
                tabsdata={tabsData}
            />));

        let firstTabContent = queryByText(container, tab1Text);
        expect(firstTabContent).not.toBeNull();
        let secondTabContent = queryByText(container, tab2Text);
        expect(secondTabContent).toBeNull();

        // second tab clicked
        const secondTabLabel = queryByText(container, tab2Label);
        secondTabLabel.click();

        firstTabContent = queryByText(container, tab1Text);
        expect(firstTabContent).toBeNull();
        secondTabContent = queryByText(container, tab2Text);
        expect(secondTabContent).not.toBeNull();

        const firstTabLabel = queryByText(container, tab1Label);
        firstTabLabel.click();

        firstTabContent = queryByText(container, tab1Text);
        expect(firstTabContent).not.toBeNull();
        secondTabContent = queryByText(container, tab2Text);
        expect(secondTabContent).toBeNull();
    });

    it("should test switching between tabs with isTabChangeAllowed callback", () => {
        // callback does not allow to switch to the second tab
        let isTabChangeAllowed = jest.fn(newTab => newTab !== 1);
        const { container } = render((
            <PageTabs
                tabsdata={tabsData}
                isTabChangeAllowed={isTabChangeAllowed}
            />));

        const thirdTabLabel = queryByText(container, tab3Label);
        thirdTabLabel.click();
        expect(isTabChangeAllowed.mock.calls[0][0]).toBe(2);
        let thirdTabContent = queryByText(container, tab3Text);
        expect(thirdTabContent).not.toBeNull();

        const secondTabLabel = queryByText(container, tab2Label);
        secondTabLabel.click();
        expect(isTabChangeAllowed.mock.calls[1][0]).toBe(1);
        thirdTabContent = queryByText(container, tab3Text);
        expect(thirdTabContent).not.toBeNull();
    });
});