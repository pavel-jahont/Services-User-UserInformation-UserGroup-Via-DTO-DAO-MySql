package com.gmail.jahont.pavel.service.impl;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.gmail.jahont.pavel.repository.ConnectionRepository;
import com.gmail.jahont.pavel.repository.UserInformationRepository;
import com.gmail.jahont.pavel.repository.UserRepository;
import com.gmail.jahont.pavel.repository.impl.ConnectionRepositoryImpl;
import com.gmail.jahont.pavel.repository.impl.UserInformationRepositoryImpl;
import com.gmail.jahont.pavel.repository.impl.UserRepositoryImpl;
import com.gmail.jahont.pavel.repository.model.User;
import com.gmail.jahont.pavel.repository.model.UserGroup;
import com.gmail.jahont.pavel.repository.model.UserInformation;
import com.gmail.jahont.pavel.service.UserService;
import com.gmail.jahont.pavel.service.model.AddUserDTO;
import com.gmail.jahont.pavel.service.model.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserServiceImpl implements UserService{

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static UserService instance;
    private UserRepository userRepository;
    private UserInformationRepository userInformationRepository;
    private ConnectionRepository connectionRepository;

    private UserServiceImpl(
            ConnectionRepository connectionRepository,
            UserRepository userRepository,
            UserInformationRepository userInformationRepository
    ) {
        this.connectionRepository = connectionRepository;
        this.userRepository = userRepository;
        this.userInformationRepository = userInformationRepository;
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl(
                    ConnectionRepositoryImpl.getInstance(),
                    UserRepositoryImpl.getInstance(),
                    UserInformationRepositoryImpl.getInstance());
        }
        return instance;
    }

    @Override
    public void add(List<AddUserDTO> users) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                for (AddUserDTO user : users) {
                    User databaseUser = convertDTOToDatabaseUser(user);
                    User addedUser = userRepository.add(connection, databaseUser);
                    databaseUser.getUserInformation().setUserId(addedUser.getId());
                    userInformationRepository.add(connection, databaseUser.getUserInformation());
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public List<UserDTO> findAll() {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<User> people = userRepository.findAll(connection);
                List<UserDTO> userDTOList = convertDatabaseUserToDTO(people);
                connection.commit();
                return userDTOList;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public int deleteUserWhereAgeLessThanParameter(int age) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<User> people = userRepository.findUsersWhereAgeLessThanParameter(connection, age);
                List<Integer> listOfUserIds = people.stream()
                        .map(User::getId)
                        .collect(Collectors.toList());
                for (Integer id : listOfUserIds) {
                    userInformationRepository.delete(connection, id);
                    userRepository.delete(connection, id);
                }
                connection.commit();
                return listOfUserIds.size();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public void disableUsersByAge(int minAge, int maxAge) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                userRepository.disableUsersByAge(connection, minAge, maxAge);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private User convertDTOToDatabaseUser(AddUserDTO user) {
        User databaseUser = new User();
        databaseUser.setUsername(user.getUsername());
        databaseUser.setPassword(user.getPassword());
        databaseUser.setAge(user.getAge());
        databaseUser.setActive(user.isActive());

        UserGroup userGroup = new UserGroup();
        userGroup.setId(user.getUserGroupId());

        UserInformation userInformation = new UserInformation();
        userInformation.setTelephone(user.getTelephone());
        userInformation.setAddress(user.getAddress());
        databaseUser.setUserInformation(userInformation);

        databaseUser.setUserGroup(userGroup);
        return databaseUser;
    }

    private List<UserDTO> convertDatabaseUserToDTO(List<User> people) {
        return people.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    private UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setAge(user.getAge());
        userDTO.setActive(user.getActive());
        userDTO.setGroupName(user.getUserGroup().getName());
        if (user.getUserInformation() != null) {
            userDTO.setTelephone(user.getUserInformation().getTelephone());
            userDTO.setAddress(user.getUserInformation().getAddress());
        }
        return userDTO;
    }

}
