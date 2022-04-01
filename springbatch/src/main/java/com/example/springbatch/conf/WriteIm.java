package com.example.springbatch.conf;

import com.example.springbatch.entity.UserEntity;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WriteIm implements ItemWriter<UserEntity> {
    @Override
    public void write(List<? extends UserEntity> list) throws Exception {
        System.out.println("writing====================");
        for (UserEntity user : list) {
            System.out.println("--------"+user);
        }


    }
}
