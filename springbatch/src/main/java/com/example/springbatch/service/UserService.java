package com.example.springbatch.service;

import com.example.springbatch.entity.UserEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> queryInfoById();

}
