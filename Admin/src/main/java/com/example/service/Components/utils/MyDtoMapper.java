package com.example.service.Components.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyDtoMapper {
    private static JsonMapper jsonMapper = new JsonMapper();
    public static <T> T mapDtoToClass(Object dto, Class<T> classValue){
        T t = null;

        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try{
            String data = jsonMapper.writeValueAsString(dto);
            t = jsonMapper.readValue(data, classValue);
        }catch(Exception e){
            log.warn(e.getLocalizedMessage());;
        }
        return t;
    }


    public static String convertToString(Object object) {
        Gson gson = new Gson();
        try {
            return gson.toJson(object);
        } catch (Exception e) {
            log.warn(e.getLocalizedMessage());
            ;
            return null;
        }
    }

    public static<T> T  stringToClass(String obj, Class<T> classValue){
        T t = null;
        try{
            t = jsonMapper.readValue(obj, classValue);
        }catch(Exception e){
            log.warn("There was an error converting string to class");
            log.warn(e.getLocalizedMessage());
        }
        return t;
    }
}
