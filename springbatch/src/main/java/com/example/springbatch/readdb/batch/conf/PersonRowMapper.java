package com.example.springbatch.readdb.batch.conf;

import com.example.springbatch.readdb.entity.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonRowMapper implements RowMapper<UserEntity> {
    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
       return new UserEntity(
        rs.getInt("id"),
        rs.getString("user_name"),
        rs.getString("sex"),
        rs.getInt("age"),
        rs.getString("address"),
        rs.getInt("status"),
        rs.getDate("create_time"),
        rs.getDate("update_time"));
    }
}
