package com.gmail.jahont.pavel.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gmail.jahont.pavel.repository.model.UserGroup;

public interface UserGroupRepository extends GeneralRepository<UserGroup> {

    List<UserGroup> findAll(Connection connection) throws SQLException;

    int getCountOfUsersForGroup(Connection connection, Integer id) throws SQLException;

}
