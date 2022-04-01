package com.example.springbatch.mapper;

import com.example.springbatch.entity.MusicEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface MusicDao {
    @Select("select id , singerName , musicSize , musicName from music_info where id = #{id};")

    public List<MusicEntity> queryInfoById(Map<String , Integer> map);
}
