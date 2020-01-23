package com.gmail.jahont.pavel.service;

import java.util.List;

import com.gmail.jahont.pavel.service.model.AddUserDTO;
import com.gmail.jahont.pavel.service.model.UserDTO;

public interface UserService {

    void add(List<AddUserDTO> users);

    List<UserDTO> findAll();

    int deleteUserWhereAgeLessThanParameter(int age);

    void disableUsersByAge(int minAge, int maxAge);
}
