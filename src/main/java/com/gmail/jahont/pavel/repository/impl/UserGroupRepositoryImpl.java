package com.gmail.jahont.pavel.repository.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gmail.jahont.pavel.repository.UserGroupRepository;
import com.gmail.jahont.pavel.repository.model.UserGroup;

public class UserGroupRepositoryImpl implements UserGroupRepository {

    private static UserGroupRepository instance;

    private UserGroupRepositoryImpl() {
    }

    public static UserGroupRepository getInstance() {
        if (instance == null) {
            instance = new UserGroupRepositoryImpl();
        }
        return instance;
    }

    @Override
    public UserGroup add(Connection connection, UserGroup userGroup) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO user_group(name) VALUES (?)",
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            statement.setString(1, userGroup.getName());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user group failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    userGroup.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            return userGroup;
        }
    }

    @Override
    public UserGroup get(Connection connection, Serializable id) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT id, name FROM user_group WHERE id=?"
                )
        ) {
            statement.setInt(1, (Integer) id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return getUserGroup(rs);
                }
            }
            return null;
        }
    }

    @Override
    public void delete(Connection connection, Serializable id) {
        throw new UnsupportedOperationException("Delete action for User Group is not implemented");
    }

    @Override
    public List<UserGroup> findAll(Connection connection) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT id, name FROM user_group"
                )
        ) {
            List<UserGroup> userGroups = new ArrayList<>();
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    UserGroup userGroup = getUserGroup(rs);
                    userGroups.add(userGroup);
                }
                return userGroups;
            }
        }
    }

    @Override
    public int getCountOfUsersForGroup(Connection connection, Integer id) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT count(*) as count FROM user_group ug JOIN user u ON u.user_group_id=ug.id WHERE ug.id=? GROUP BY ug.id"
                )
        ) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
            return 0;
        }
    }

    private UserGroup getUserGroup(ResultSet rs) throws SQLException {
        UserGroup userGroup = new UserGroup();
        userGroup.setId(rs.getInt("id"));
        userGroup.setName(rs.getString("name"));
        return userGroup;
    }

}
