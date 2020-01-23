package com.gmail.jahont.pavel.controller;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gmail.jahont.pavel.service.TableService;
import com.gmail.jahont.pavel.service.UserGroupService;
import com.gmail.jahont.pavel.service.UserService;
import com.gmail.jahont.pavel.service.impl.TableServiceImpl;
import com.gmail.jahont.pavel.service.impl.UserGroupServiceImpl;
import com.gmail.jahont.pavel.service.impl.UserServiceImpl;
import com.gmail.jahont.pavel.service.model.AddUserDTO;
import com.gmail.jahont.pavel.service.model.UserDTO;
import com.gmail.jahont.pavel.service.model.UserGroupDTO;
import com.gmail.jahont.pavel.util.RandomUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HomeWorkController {

    public static final int COUNT_OF_GROUPS = 3;
    public static final int COUNT_OF_USERS = 30;
    public static final int MAX_AGE = 35;
    public static final int MIN_AGE = 25;
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static HomeWorkController instance;
    private TableService tableService = TableServiceImpl.getInstance();
    private UserGroupService userGroupService = UserGroupServiceImpl.getInstance();
    private UserService userService = UserServiceImpl.getInstance();
    private HomeWorkController() {
    }

    public static HomeWorkController getInstance() {
        if (instance == null) {
            instance = new HomeWorkController();
        }
        return instance;
    }

    public void runTask() {
        deleteTables();
        createTables();
        createUserGroups();
        createUsers();
        showAllUsers();
        showUserGroups();
        removeUsersByCondition();
        updateUsersByCondition();
    }

    private void deleteTables() {
        tableService.deleteAllTables();
    }

    private void createTables() {
        tableService.createAllTables();
    }

    private void createUserGroups() {
        List<UserGroupDTO> groups = new ArrayList<>();
        for (int i = 0; i < COUNT_OF_GROUPS; i++) {
            UserGroupDTO userGroup = new UserGroupDTO();
            userGroup.setName("Group" + i);
            groups.add(userGroup);
        }
        userGroupService.add(groups);
    }

    private void createUsers() {
        List<AddUserDTO> userDTOList = new ArrayList<>();
        List<UserGroupDTO> userGroups = userGroupService.findAll();
        for (int i = 0; i < COUNT_OF_USERS; i++) {
            AddUserDTO user = new AddUserDTO();
            user.setUsername("Username" + i);
            user.setPassword("Password" + i);
            user.setTelephone("Telephone" + i);
            user.setAddress("Address" + i);
            user.setAge(RandomUtil.getElement(MIN_AGE, MAX_AGE));
            user.setActive(RandomUtil.getRandomBoolean());
            user.setUserGroupId(getRandomUserGroupId(userGroups));
            userDTOList.add(user);
        }
        userService.add(userDTOList);
    }

    private Integer getRandomUserGroupId(List<UserGroupDTO> userGroups) {
        int element = RandomUtil.getElement(0, userGroups.size() - 1);
        UserGroupDTO userGroupDTO = userGroups.get(element);
        return userGroupDTO.getId();
    }

    private void showAllUsers() {
        List<UserDTO> users = userService.findAll();
        users.forEach(logger::info);
    }

    private void showUserGroups() {
        Map<UserGroupDTO, Integer> countOfUsersAtGroupDTOMap = userGroupService.getCountOfUsersAtGroups();
        countOfUsersAtGroupDTOMap.entrySet().forEach(logger::info);
    }

    private void removeUsersByCondition() {
        int age = 27;
        int countOfRemovedUsers = userService.deleteUserWhereAgeLessThanParameter(age);
        logger.info(countOfRemovedUsers);
    }

    private void updateUsersByCondition() {
        int minAge = 30;
        int maxAge = 33;
        userService.disableUsersByAge(minAge, maxAge);
    }
}