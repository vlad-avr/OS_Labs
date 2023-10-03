package com.example.func;

public abstract class Function {
    protected final String fatal_error_msg = "\nFatal Error has occurred: computation canceled. Result : DECLINED!";
    protected final String minor_error_msg = "\nMinor Error has occurred: computation results undefined. Result : UNDEFINED";
    protected final String success_msg = "\nComputation successful!";
    protected String input;

    public Function(String input){
        this.input = input;
    }

    public abstract Result compute();
}
