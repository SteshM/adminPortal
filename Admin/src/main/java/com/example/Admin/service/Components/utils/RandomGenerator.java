package com.example.Admin.service.Components.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomGenerator {
    private static String choices = "abcdefghijklmnopqrstuvwxyz0123456789";


    static Random random = new Random();
    public static String generateRandom(int length){
        String pass = "";
        for(int i = 0; i<length; i++){
            pass += ""+choices.charAt(random.nextInt(choices.length()));
        }
        return pass;
    }
}
