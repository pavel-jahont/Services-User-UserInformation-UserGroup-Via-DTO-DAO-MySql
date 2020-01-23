package com.gmail.jahont.pavel.repository.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.gmail.jahont.pavel.repository.UserInformationRepository;
import com.gmail.jahont.pavel.repository.model.UserInformation;

public class UserInformationRepositoryImpl implements UserInformationRepository {

    private static UserInformationRepository instance;

    private UserInformationRepositoryImpl() {
    }

    public static UserInformationRepository getInstance() {
        if (instance == null) {
            instance = new UserInformationRepositoryImpl();
        }
        return instance;
    }

    @Override
    public UserInformation add(Connection connection, UserInformation userInformation) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO user_information(user_id, address, telephone) VALUES (?,?,?)"
                )
        ) {
            statement.setInt(1, userInformation.getUserId());
            statement.setString(2, userInformation.getAddress());
            statement.setString(3, userInformation.getTelephone());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user information failed, no rows affected.");
            }
            return userInformation;
        }
    }

    @Override
    public UserInformation get(Connection connection, Serializable id) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT user_id, address, telephone FROM user_information u WHERE user_id=?"
                )
        ) {
            statement.setInt(1, (Integer) id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return getUserInformation(rs);
                }
            }
            return null;
        }
    }

    @Override
    public void delete(Connection connection, Serializable id) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "DELETE FROM user_information WHERE user_id=?"
                )
        ) {
            statement.setInt(1, (Integer) id);
            statement.execute();
        }
    }

    private UserInformation getUserInformation(ResultSet rs) throws SQLException {
        UserInformation userInformation = new UserInformation();
        userInformation.setUserId(rs.getInt("user_id"));
        userInformation.setAddress(rs.getString("address"));
        userInformation.setTelephone(rs.getString("telephone"));
        return userInformation;
    }

}
