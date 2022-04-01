package com.example.springbatch.readdb.mapper;

import com.example.springbatch.readdb.entity.UserCopyEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDBMapper {
    void insert(UserCopyEntity dealBean);
}
