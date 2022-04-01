package com.example.springbatch.mapper;

import com.example.springbatch.entity.UserEntity;
import com.example.springbatch.entity.UserEntityCopy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<UserEntity> queryInfoById();

}
