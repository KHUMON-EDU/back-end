package com.teamh.khumon.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectToDtoUtil {
    public Object  jsonStrToObj(String jsonStr, Class<?> classType) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readerFor(classType).readValue(jsonStr);
    }
}
