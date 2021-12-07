package com.javamaster.springsecurityjwt.controller;

import lombok.Data;

import java.util.Date;

@Data
public class NewComment {
    private Integer id;
    private String message;
    private Integer rate;
    private Boolean approved;
    private Integer post_id;
    private Integer author_id;
    private Date createdAt;


}
