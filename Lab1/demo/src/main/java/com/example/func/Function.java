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
    protected PipedInputStream inputStream = new PipedInputStream();
    protected PipedOutputStream outputStream = new PipedOutputStream();

    public void connectToInputStream(PipedInputStream inputStream) {
        try {
            this.outputStream.connect(inputStream);
            inputStream.connect(this.outputStream);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void connectToOutputStream(PipedOutputStream outputStream) {
        try {
            this.inputStream.connect(outputStream);
            outputStream.connect(this.inputStream);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public String getInputFromStream(int inputLen) {
        try {
            String res = "";
            for(int i = 0; i < inputLen; i++){
                res += (char)inputStream.read();
            }
            return res;
        } catch (IOException exception) {
            //Should not trigger
            System.out.println(exception.getMessage());
            return "";
        }
    }

    public void putOutputToStream(byte data){
        try {
            outputStream.write(data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    // public Function(String input, int minorErrorAttempts){
    // this.input = input;
    // this.minorErrorAttempts = minorErrorAttempts;
    // }

    // public abstract Result compute();
}
