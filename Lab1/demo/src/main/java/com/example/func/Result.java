package com.example.func;

public class Result {
    public Status status;
    public Integer value;
    public Integer attempts;
    public String functionName;
    // protected static final String fatal_error_msg = "\nFatal Error has occurred:
    // computation canceled. Result : DECLINED!";
    // protected static final String minor_error_msg = "\nMinor Error has occurred:
    // computation results undefined. Result : UNDEFINED";
    // protected static final String success_msg = "\nComputation successful!";
    public static enum Status {
        SUCCESS,
        MINOR_ERROR,
        FATAL_ERROR
    }

    public Result(int value, Status stat) {
        this.status = stat;
        this.value = value;
    }

    public Result(String strToParse) {
        status = Status.valueOf(strToParse.substring(0, strToParse.indexOf("\t")));
        try {
            if (status != Status.FATAL_ERROR) {
                value = Integer.parseInt(strToParse.substring(strToParse.indexOf("\t") + 1, strToParse.length()));
            }
            attempts = Integer.parseInt(strToParse.substring(strToParse.lastIndexOf("\t"), strToParse.length()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public Result() {
    };

    public void show() {
        System.out.println("\n Result status of " + functionName + " : " + status.toString());
        if (value != null) {
            System.out.println("\n Result value : " + value);
        }
        System.out.println("\n Attempts taken : " + attempts);
    }

    public void setFunctionName(String functionName){
        this.functionName = functionName;
    }

    public String toString() {
        return status.toString() + "\t" + value + "\t" + attempts;
    }
}
