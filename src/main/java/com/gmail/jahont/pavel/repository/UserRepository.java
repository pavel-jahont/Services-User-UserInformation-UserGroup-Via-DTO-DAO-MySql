package com.gmail.jahont.pavel.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gmail.jahont.pavel.repository.model.User;

public interface UserRepository extends GeneralRepository<User> {

    void update(Connection connection, User user) throws SQLException;

    List<User> findAll(Connection connection) throws SQLException;

    List<User> findUsersWhereAgeLessThanParameter(Connection connection, int age) throws SQLException;

    void disableUsersByAge(Connection connection, int minAge, int maxAge) throws SQLException;
}
