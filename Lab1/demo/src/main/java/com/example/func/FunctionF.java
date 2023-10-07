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
            res.status = fatal_error_msg;
            return res;
        }
        try {
            Thread.sleep(rnd.nextInt(12000) + 6000);
        } catch (InterruptedException exception) {
            res.status = fatal_error_msg;
            return res;
        }
        if (val < 0) {
            for (int i = 0; i < minorErrorAttempts; i++) {
                int flip = rnd.nextInt(2);
                if (flip == 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException exception) {
                        res.status = fatal_error_msg;
                        return res;
                    }
                    continue;
                } else {
                    val = (int) Math.ceil(Math.sqrt((double) Math.abs(val)));
                    res.value = val;
                    res.status = minor_error_msg + " : attempts taken - " + i;
                    return res;
                }
            }
            res.status = fatal_error_msg;
            return res;
        }
        val = (int) Math.ceil(Math.sqrt((double) val));
        res.value = val;
        res.status = success_msg;
        return res;
    }

    // @Override
    // public void run() {
    // compute();
    // }
}
