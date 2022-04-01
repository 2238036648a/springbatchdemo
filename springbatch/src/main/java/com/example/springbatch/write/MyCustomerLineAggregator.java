package com.example.springbatch.write;

import com.example.springbatch.entity.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.file.transform.LineAggregator;

public class MyCustomerLineAggregator implements LineAggregator<UserEntity> {
    //JSON
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public String aggregate(UserEntity customer) {

        try {
            return mapper.writeValueAsString(customer);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize.",e);
        }
    }

}
