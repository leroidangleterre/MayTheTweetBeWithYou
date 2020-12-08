package com.maythetweetbewithyou;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author arthu
 */
public class ConnectionPropertiesReader {

    private HashMap<String, String> hashmap;

    /**
     * Read the property file and set values.
     *
     */
    public ConnectionPropertiesReader() {

        hashmap = new HashMap<>();

        try {
            String filename = "Resources/twitter4j.properties";
            BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
            String line;
            while ((line = reader.readLine()) != null) {
                String split[] = line.split("=");
                String key = split[0];
                String value = split[1];
                key = removeLeadingTrailingSpaces(key);
                value = removeLeadingTrailingSpaces(value);
                hashmap.put(key, value);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Missing file \"twitter4j.properties\".");
        } catch (IOException e) {
            System.out.println("IO Exception.");
        }
    }

    public String getPropertyValue(String key) {

        String result = hashmap.get(key);
        if (result == null) {
            System.out.println("Error in retrieving value for key <" + key + ">");
        }

        return result;
    }

    public void printValues() {
        for (String value : hashmap.values()) {
            System.out.println("    Value: " + value);
        }
        for (String key : hashmap.keySet()) {
            System.out.println("Key: " + key + ", value: " + hashmap.get(key));
        }
    }

    private String removeLeadingTrailingSpaces(String key) {
        // Remove leading spaces:
        while (key.startsWith(" ")) {
            key = key.substring(1);
        }
        // Remove trailing spaces:
        while (key.endsWith(" ")) {
            key = key.substring(0, key.length() - 1);
        }
        return key;
    }
}
