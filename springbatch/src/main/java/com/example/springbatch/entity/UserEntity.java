package com.example.springbatch.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserEntity implements Serializable {

    private String userName;
    private String sex;
    private Integer age;
    private String address;
    private Byte status;
    private Date createTime;

}
