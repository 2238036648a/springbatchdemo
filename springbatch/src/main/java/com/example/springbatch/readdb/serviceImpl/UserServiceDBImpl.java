package com.example.springbatch.readdb.serviceImpl;

import com.example.springbatch.readdb.entity.UserCopyEntity;
import com.example.springbatch.readdb.mapper.UserDBMapper;
import com.example.springbatch.readdb.service.UserServiceDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserServiceDBImpl implements UserServiceDB {
    @Autowired
    @Qualifier("userDBMapper")
    private UserDBMapper userDBMapper;

    @Override
    public void insert(UserCopyEntity dealBean) {
        userDBMapper.insert(dealBean);
    }
}
