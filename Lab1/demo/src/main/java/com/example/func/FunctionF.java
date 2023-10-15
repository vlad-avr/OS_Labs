package com.example.func;

public class FunctionF extends Function {
    // public FunctionF(String input, int minorErrorAttempts) {
    // super(input, minorErrorAttempts);
    // }

    public Result compute(String input, int minorErrorAttempts) {
        Result res = new Result();
        int val = 0;
        try {
            val = Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            res.status = Result.Status.FATAL_ERROR;
            res.attempts = 0;
            return res;
        }
        try {
            int sleepTime = rnd.nextInt(12000) + 6000;
            System.out.println("\n F(x) will compute for at least " + sleepTime + " ms");
            Thread.sleep(sleepTime);
        } catch (InterruptedException exception) {
            res.status = Result.Status.FATAL_ERROR;
            res.attempts = 0;
            return res;
        }
        if (val < 0) {
            for (int i = 0; i < minorErrorAttempts; i++) {
                int flip = rnd.nextInt(2);
                if (flip == 0) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException exception) {
                        res.attempts = i+1;
                        res.status = Result.Status.FATAL_ERROR;
                        return res;
                    }
                    continue;
                } else {
                    val = (int) Math.ceil(Math.sqrt((double) Math.abs(val)));
                    res.value = val;
                    res.attempts = i+1;
                    res.status = Result.Status.MINOR_ERROR;
                    return res;
                }
            }
            res.attempts = minorErrorAttempts;
            res.status = Result.Status.FATAL_ERROR;
            return res;
        }
        val = (int) Math.ceil(Math.sqrt((double) val));
        res.value = val;
        res.attempts = 1;
        res.status = Result.Status.SUCCESS;
        return res;
    }

}
