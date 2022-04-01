package com.example.springbatch.jiaoyan;

import com.example.springbatch.entity.UserEntity;
import org.springframework.batch.item.validator.ValidatingItemProcessor;

import java.util.Date;

public class CsvItemProcessor extends ValidatingItemProcessor<UserEntity> {
    @Override
    public UserEntity process(UserEntity item) {
        super.process(item);

        // 对数据进行简单的处理，若性别为.男，则数据转换为1，其余转换为2
        if (item.getSex().equals("男")) {
            item.setSex("1");
        } else {
            item.setSex("2");
        }

        // 设置默认值
        item.setStatus((byte) 1);
        item.setCreateTime(new Date());
        return item;
    }
}

