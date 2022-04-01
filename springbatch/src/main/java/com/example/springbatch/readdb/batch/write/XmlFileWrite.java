package com.example.springbatch.readdb.batch.write;

import com.example.springbatch.readdb.entity.UserEntity;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
@Component("xmlFileWrite")
public class XmlFileWrite implements ItemWriter<UserEntity> {
    @Override
    public void write(List<? extends UserEntity> list) throws Exception {
        for (UserEntity userEntity:list){
            System.out.println("----------"+userEntity);
        }
    }


}
