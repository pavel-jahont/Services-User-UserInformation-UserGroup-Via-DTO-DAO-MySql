package com.gmail.jahont.pavel.repository;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

public interface GeneralRepository<T> {

    T add(Connection connection, T t) throws SQLException;

    T get(Connection connection, Serializable id) throws SQLException;

    void delete(Connection connection, Serializable id) throws SQLException;

}
