package com.gmail.jahont.pavel.service.impl;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gmail.jahont.pavel.repository.ConnectionRepository;
import com.gmail.jahont.pavel.repository.UserGroupRepository;
import com.gmail.jahont.pavel.repository.impl.ConnectionRepositoryImpl;
import com.gmail.jahont.pavel.repository.impl.UserGroupRepositoryImpl;
import com.gmail.jahont.pavel.repository.model.UserGroup;
import com.gmail.jahont.pavel.service.UserGroupService;
import com.gmail.jahont.pavel.service.model.UserGroupDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserGroupServiceImpl implements UserGroupService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static UserGroupService instance;
    private UserGroupRepository userGroupRepository;
    private ConnectionRepository connectionRepository;

    private UserGroupServiceImpl(ConnectionRepository connectionRepository, UserGroupRepository userGroupRepository) {
        this.connectionRepository = connectionRepository;
        this.userGroupRepository = userGroupRepository;
    }

    public static UserGroupService getInstance() {
        if (instance == null) {
            instance = new UserGroupServiceImpl(
                    ConnectionRepositoryImpl.getInstance(),
                    UserGroupRepositoryImpl.getInstance()
            );
        }
        return instance;
    }

    @Override
    public void add(List<UserGroupDTO> userGroups) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                for (UserGroupDTO userGroup : userGroups) {
                    UserGroup databaseUserGroup = convertDTOToDatabaseUserGroup(userGroup);
                    userGroupRepository.add(connection, databaseUserGroup);
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
    public List<UserGroupDTO> findAll() {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<UserGroup> userGroups = userGroupRepository.findAll(connection);
                List<UserGroupDTO> userGroupDTOS = convertDatabaseUserGroupToDTO(userGroups);
                connection.commit();
                return userGroupDTOS;
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
    public Map<UserGroupDTO, Integer> getCountOfUsersAtGroups() {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<UserGroup> userGroups = userGroupRepository.findAll(connection);
                List<UserGroupDTO> userGroupDTOS = convertDatabaseUserGroupToDTO(userGroups);
                Map<UserGroupDTO, Integer> countOfUsersAtGroupsMap = new HashMap<>();
                for (UserGroupDTO userGroupDTO : userGroupDTOS) {
                    int count = userGroupRepository.getCountOfUsersForGroup(connection, userGroupDTO.getId());
                    countOfUsersAtGroupsMap.put(userGroupDTO, count);
                }
                connection.commit();
                return countOfUsersAtGroupsMap;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyMap();
    }

    private List<UserGroupDTO> convertDatabaseUserGroupToDTO(List<UserGroup> userGroups) {
        return userGroups.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    private UserGroupDTO convert(UserGroup userGroup) {
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setId(userGroup.getId());
        userGroupDTO.setName(userGroup.getName());
        return userGroupDTO;
    }

    private UserGroup convertDTOToDatabaseUserGroup(UserGroupDTO userGroup) {
        UserGroup databaseUserGroup = new UserGroup();
        databaseUserGroup.setName(userGroup.getName());
        return databaseUserGroup;
    }

}
