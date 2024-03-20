package com.moment.auth.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class CommonUtils {
    public static Map<String,Object> convertJSONstringToMap(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();

        map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});

        return map;
    }
}
