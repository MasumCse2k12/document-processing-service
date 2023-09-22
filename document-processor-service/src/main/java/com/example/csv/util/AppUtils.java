package com.example.csv.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class AppUtils {

    public static String isAllColumnsValid(String uploadPath, int number) {
        String line = "";
        String splitBy = ",";
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader(uploadPath));
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                String[] word = line.split(splitBy);    // use comma as separator
                int numberOfFields = word.length;
                log.info("Commas: " + numberOfFields);

                if (word.length < number) {
                    log.info("Not all fields are present");
                    return "Not all fields are present";
                } else if (word.length > number) {
                    log.info("Too many fields are present");
                    return "Too many fields are present";
                }
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Constants.VALID;
    }
}
