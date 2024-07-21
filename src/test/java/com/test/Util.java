package com.test;

import org.apache.commons.lang3.RandomStringUtils;

public class Util {

    public String createRandomChar(int length){
        String generatedString = RandomStringUtils.randomAlphabetic(length);
        return generatedString;
    }

    public String validateRating(int rating){
        String status ="";
        if(rating==0){
            status = "Rejected";
        }else if(rating>=5){
            status = "active";

        }else if(rating<=4){
            status = "new";
        }
        return status;
    }
}
