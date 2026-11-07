package com.example.jellytest;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestResults {
    private Map<String,Map<String,List<Boolean>>> results;
    public TestResults(){
        results = new LinkedHashMap<>();
    }

    public synchronized void addResult(String testSetName, String testName, boolean result){
        if(!results.containsKey(testSetName)){
            results.put(testSetName,new HashMap<>());
        }
        if(!results.get(testSetName).containsKey(testName)){
            results.get(testSetName).put(testName,new ArrayList<>());
        }
        results.get(testSetName).get(testName).add(result);
    }

    boolean getResult(String testSetName, String testName, int index){
        try{
            return results.get(testSetName).get(testName).get(index);
        } catch (Exception e) {
            return false;
        }

    }

    public String getTestResultString(String testSetName, String testName){
        try {
            StringBuilder builder = new StringBuilder();
            for(boolean result : results.get(testSetName).get(testName)){
                builder.append(result?"PASS-":"FAIL-");
            }
            builder.deleteCharAt(builder.length()-1);
            return builder.toString();
        } catch (Exception e) {
            return "";
        }
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(String testSetKey : results.keySet()){
            builder.append(testSetKey).append("\n");
            for(String testNameKey : results.get(testSetKey).keySet()){
                builder.append("---").append(testNameKey).append(" ");
                for(boolean result : results.get(testSetKey).get(testNameKey)){
                    builder.append(result?"PASS-":"FAIL-");
                }
                builder.deleteCharAt(builder.length()-1);
                builder.append("\n");
            }
        }
        return builder.toString();
    }
}
