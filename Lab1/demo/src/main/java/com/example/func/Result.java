package com.example.func;

public class Result {
    public String status_string;
    public Status status;
    public Integer value;
    public static enum Status{
        SUCCESS,
        MINOR_ERROR,
        FATAL_ERROR
    }

    public Result(String status, int value, Status stat){
        this.status_string = status;
        this.status = stat;
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
