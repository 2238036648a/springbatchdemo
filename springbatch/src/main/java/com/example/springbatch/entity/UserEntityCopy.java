package com.example.springbatch.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserEntityCopy {
    private Integer id;
    private String user_name;
    private String sex;
    private Integer age;
    private String address;
    private Byte status;
    private Date createTime;
}
