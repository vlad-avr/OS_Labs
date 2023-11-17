package com.example.scheduling;

import java.util.ArrayList;
import java.util.List;

public class User {
    public List<sProcess> processes = new ArrayList<>();
    public int processNum;
    public int weight;
    public String name;
    public User(int processNum, int weight, String name){
        this.weight = weight;
        this.processNum = processNum;
        this.name = name;
    }
}
