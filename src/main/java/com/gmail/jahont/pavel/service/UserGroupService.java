package com.gmail.jahont.pavel.service;

import java.util.List;
import java.util.Map;

import com.gmail.jahont.pavel.service.model.UserGroupDTO;

public interface UserGroupService {

    void add(List<UserGroupDTO> userGroupDTOList);

    List<UserGroupDTO> findAll();

    Map<UserGroupDTO, Integer> getCountOfUsersAtGroups();

}
