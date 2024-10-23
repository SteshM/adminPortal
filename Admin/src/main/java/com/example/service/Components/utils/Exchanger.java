package com.example.service.Components.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
@Component
public class Exchanger {
    @Autowired
    RestTemplate restTemplate;

    public JsonNode sendPostRequest(String url, Object object){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(MyDtoMapper.convertToString(object),headers);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, JsonNode.class).getBody();
    }
    @Async
    public void sendPostEmailRequest(String url, Object object){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(MyDtoMapper.convertToString(object),headers);
        restTemplate.exchange(url, HttpMethod.POST, requestEntity, JsonNode.class).getBody();
    }


    public JsonNode sendGetRequest(String url){
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, JsonNode.class).getBody();
    }

    public JsonNode sendFormData(String url, MultiValueMap<String, Object> formData){
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(formData, headers);
        return restTemplate.exchange(url, HttpMethod.POST, request, JsonNode.class).getBody();
    }
}
