package com.javamaster.springsecurityjwt.entity;

public class CurrentUser {

    private static Integer idOfCurrentUser;

    private static String roleOfCurrentUser;

    public static Integer getIdOfCurrentUser() {
        return idOfCurrentUser;
    }

    public static void setIdOfCurrentUser(Integer idOfCurrentUser) {
        CurrentUser.idOfCurrentUser = idOfCurrentUser;
    }

    public static String getRoleOfCurrentUser() {
        return roleOfCurrentUser;
    }

    public static void setRoleOfCurrentUser(String roleOfCurrentUser) {
        CurrentUser.roleOfCurrentUser = roleOfCurrentUser;
    }

    public void setRoleOgCurrentUser(String roleOfCurrentUser) {
        CurrentUser.roleOfCurrentUser = roleOfCurrentUser;
    }
}
