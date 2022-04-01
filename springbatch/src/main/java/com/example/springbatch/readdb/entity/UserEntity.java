package com.example.springbatch.readdb.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserEntity implements Serializable {




    private Integer id;
    private String user_name;
    private String sex;

    public UserEntity(Integer id, String user_name, String sex, Integer age, String address, Integer status, Date create_time, Date update_time) {
        this.id = id;
        this.user_name = user_name;
        this.sex = sex;
        this.age = age;
        this.address = address;
        this.status = status;
        this.create_time = create_time;
        this.update_time = update_time;
    }

    public UserEntity() {
    }

    private Integer age;
    private String address;
    private Integer status;
    private Date create_time;
    private Date update_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}
