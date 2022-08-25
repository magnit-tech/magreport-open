import React from "react";
import { render, queryByText } from "test-utils";
import UserRoleCard from "../UserRoleCard";

const userRoleName = "Test role";

it("should render UserRoleCard and display role name", () => {
    const { container } = render(<UserRoleCard roleName={userRoleName} />);

    const textElement = queryByText(container, userRoleName);
    expect(textElement).not.toBeNull();
})
