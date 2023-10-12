package com.example.func;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.security.SecureRandom;

public abstract class Function {
    protected final String fatal_error_msg = "\nFatal Error has occurred: computation canceled. Result : DECLINED!";
    protected final String minor_error_msg = "\nMinor Error has occurred: computation results undefined. Result : UNDEFINED";
    protected final String success_msg = "\nComputation successful!";
    // protected String input;
    // protected Integer minorErrorAttempts;
    protected final SecureRandom rnd = new SecureRandom();


    public abstract Result compute(String input, int minorErrorAttempts);
    // public Function(String input, int minorErrorAttempts){
    // this.input = input;
    // this.minorErrorAttempts = minorErrorAttempts;
    // }

    // public abstract Result compute();
}
