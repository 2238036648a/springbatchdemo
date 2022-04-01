package com.example.springbatch.service.impl;

import com.example.springbatch.entity.UserEntity;
import com.example.springbatch.mapper.UserMapper;
import com.example.springbatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;
    @Override
    public List<UserEntity> queryInfoById() {
        List<UserEntity> userEntities = mapper.queryInfoById();

        return userEntities;
    }
}
