import React from "react";
import { render, userEvent, getByTestId, getByPlaceholderText, queryByText } from "test-utils";
import UserList from "../UserList";


let users = [{
    id: 1,
    name: "pupkin_vv",
    description: null,
    email: "pupkin_vv@magnit.ru",
    status: "ACTIVE",
    roles: [{
        id: 1,
        typeId: 0,
        name: "ADMIN",
        description: "Админ"
    }],
    created: "2020-01-01T00:00:00.000000",
    modified: "2020-01-01T00:00:00.000000"
}, {
    id: 2,
    name: "ivanov_ii",
    description: null,
    email: "ivanov_ii@magnit.ru",
    status: "ACTIVE",
    roles: [],
    created: "2020-03-01T00:00:00.000000",
    modified: "2020-03-01T00:00:00.000000"
}]

it("should render UserList with users.length items", function () {
    let { container } = render(<UserList items={users} />);
    let usersList = getByTestId(container, "users_list");

    expect(usersList.children).toHaveLength(users.length);
});

it("should have only single user after typing into search field", function () {
    let { container } = render(<UserList items={users} />);
    let usersList = getByTestId(container,"users_list");
    let searchField = getByPlaceholderText(container,/Поиск/ );

    expect(usersList.children).toHaveLength(users.length);

    userEvent.type(searchField, "ivanov_ii");
    expect(usersList.children).toHaveLength(1);
    expect(queryByText(usersList, "ivanov_ii")).not.toBeNull();
});