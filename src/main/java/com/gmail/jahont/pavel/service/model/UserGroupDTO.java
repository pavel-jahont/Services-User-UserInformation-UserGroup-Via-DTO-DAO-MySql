package com.gmail.jahont.pavel.service.model;

public class UserGroupDTO {

    private Integer id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserGroupDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}