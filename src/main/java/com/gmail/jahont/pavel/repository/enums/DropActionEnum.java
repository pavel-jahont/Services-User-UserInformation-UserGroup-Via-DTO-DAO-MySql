package com.gmail.jahont.pavel.repository.enums;

public enum DropActionEnum {

    DROP_USER_INFORMATION_TABLE("DROP TABLE IF EXISTS user_information"),
    DROP_USER_TABLE("DROP TABLE IF EXISTS user"),
    DROP_USER_GROUP_TABLE("DROP TABLE IF EXISTS user_group");

    private final String query;

    DropActionEnum(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
