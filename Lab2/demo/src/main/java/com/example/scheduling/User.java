package com.example.scheduling;

import java.util.ArrayList;
import java.util.List;

public class User {
    public List<sProcess> processes = new ArrayList<>();
    public int weight;
    public User(int weight){
        this.weight = weight;
    }
}
