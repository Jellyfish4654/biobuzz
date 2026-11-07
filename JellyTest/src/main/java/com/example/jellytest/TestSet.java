package com.example.jellytest;

import java.util.ArrayList;

/**Class for holding multiple tests for running*/
public class TestSet {
    public String name;
    public boolean isAsync;
    private ArrayList<OnBotTest> tests;
    public TestSet(String name, boolean isAsync){
        this.name = name;
        this.isAsync = isAsync;
        tests = new ArrayList<>();
    }

    public void addTest(OnBotTest test){
        tests.add(test);
    }
    public OnBotTest getTest(int index){
        return tests.get(index);
    }

    public int getSize(){
        return tests.size();
    }

    public int getAmountDone(){
        int counter=0;
        for(OnBotTest t : tests){
            if(t.done){
                counter++;
            }
        }
        return counter;
    }
    public int getAmountPassed(){
        int counter=0;
        for(OnBotTest t : tests){
            if(t.done && t.passed){
                counter++;
            }
        }
        return counter;
    }
    public int getAmountFailed(){
        int counter=0;
        for(OnBotTest t : tests){
            if(t.done && !t.passed){
                counter++;
            }
        }
        return counter;
    }


}
