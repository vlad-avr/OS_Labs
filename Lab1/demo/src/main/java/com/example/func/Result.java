package com.example.func;

public class Result {
    public Status status;
    public Integer value;
    public Integer attempts;
    public String functionName;
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
                value = Integer.parseInt(strToParse.substring(strToParse.indexOf("\t") + 1, strToParse.lastIndexOf("\t")));
            }
            attempts = Integer.parseInt(strToParse.substring(strToParse.lastIndexOf("\t") + 1, strToParse.length()));
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

    public String getReport(){
        if(status == null){
            return "Computation is ongoing, no errors have occurred as of yet";
        }else{
            if(status == Result.Status.FATAL_ERROR){
                return "Computation ended with " + status.toString() + " -> Unable to compute result in " + attempts + " attempts";
            }else if(status == Result.Status.SUCCESS){
                return "Computation ended with " + status.toString() + " -> Result is " + value + " | Attempts taken - " + attempts;
            }else{
                if(value == null){
                    return status.toString() + " has occurred, computation is ongoing -> obtaining result is possible | Attempts taken so far - " + attempts;
                }else{
                    return "Computation ended with " + status.toString() + " -> minor errors have occurred during computation but result was obtained : " + value + " | Attempts taken - " + attempts;
                }
            }
        }
    }

    public String toString() {
        return status.toString() + "\t" + value + "\t" + attempts;
    }
}
