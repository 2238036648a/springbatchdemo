package com.example.springbatch.readdb.batch.process;

import com.example.springbatch.readdb.entity.UserEntity;
import com.example.springbatch.readdb.entity.UserCopyEntity;
import org.springframework.batch.item.ItemProcessor;

/**
 *处理数据器
 *ItemProcessor<Bean, DealBean>
 *Bean为传入数据，DealBean为处理后返回数据
 */
public class JdbcItemProcessor implements ItemProcessor<UserEntity, UserEntity> {

    public UserEntity process(UserEntity bean) throws Exception {
        // Bean bean 为读操作数据
        UserCopyEntity deal = new UserCopyEntity();

        if (bean.getSex().equals("1")) {
            bean.setSex("男");
        } else {
            bean.setSex("女");
        }
        return bean;

    }
}
