package com.example.func;

public class FunctionG extends Function {
    // public FunctionG(String input, int minorErrorAttempts) {
    //     super(input, minorErrorAttempts);
    // }

    public Result compute(String input, int minorErrorAttempts) {
        Result res = new Result();

        int val = 0;
        try {
            val = Integer.parseInt(input);
            if (val == 0) {
                NumberFormatException exception = new NumberFormatException();
                throw exception;
            }
        } catch (NumberFormatException exception) {
            res.status = fatal_error_msg;
            return res;
        }
        try {
            Thread.sleep(rnd.nextInt(10000) + 5000);
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
                    val = (int) Math.ceil(Math.log((double) Math.abs(val)));
                    res.value = val;
                    res.status = minor_error_msg + " : attempts taken - " + i;
                    return res;
                }
            }
            res.status = fatal_error_msg;
            return res;
        }
        val = (int) Math.ceil(Math.log((double) val));
        res.value = val;
        res.status = success_msg;
        return res;
    }

    // @Override
    // public void run() {
    //     compute();
    // }
}
