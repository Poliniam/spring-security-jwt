package com.javamaster.springsecurityjwt.controller;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RegistrationRequest {

    private String email;

    private String login;

    private String password;

    private String confirmPassword;
}
