package com.javamaster.springsecurityjwt.controller;

import lombok.Data;

import java.util.Date;

@Data
public class NewForm {

    private Integer id;
    private String title;
    private Boolean status;
    private String gameName;
    private Integer author_id;
    private String text;
    private Date createdAt;
    private Date updatedAt;
}
