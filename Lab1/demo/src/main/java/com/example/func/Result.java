package com.example.func;

public class Result {
    public String status;
    public Integer value;

    public Result(String status, int value){
        this.status = status;
        this.value = value;
    }

    public Result(){};

    public void show(){
        System.out.println("\n Result status : " + status);
        if(value != null){
            System.out.println("\n Result value : " + value);
        }
    }
}
