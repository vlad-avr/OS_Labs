package com.example.func;

public class FunctionG extends Function {

    public Result compute(String input, int minorErrorAttempts) {
        Result res = new Result();
        container.putOutputToStream(res.getReport(), container.getReportStream());
        int val = 0;
        try {
            val = Integer.parseInt(input);
            if (val == 0) {
                NumberFormatException exception = new NumberFormatException();
                throw exception;
            }
        } catch (NumberFormatException exception) {
            res.attempts = 0;
            res.status = Result.Status.FATAL_ERROR;
            container.putOutputToStream(res.getReport(), container.getReportStream());
            return res;
        }

        int sleepTime = rnd.nextInt(10000) + 5000;
        int allTime = sleepTime;
        int i = 0;
        System.out.println("\n G(x) will compute for at least " + sleepTime + " ms");

        if (val < 0) {
            for (; i < minorErrorAttempts; i++) {
                int flip = rnd.nextInt(2);
                if (flip == 0) {
                    try {
                        Thread.sleep(400);
                        allTime += 400;
                    } catch (InterruptedException exception) {
                        res.attempts = i + 1;
                        res.status = Result.Status.FATAL_ERROR;
                        container.putOutputToStream(res.getReport(), container.getReportStream());
                        return res;
                    }
                    continue;
                } else {
                    val = (int) Math.ceil(Math.log((double) Math.abs(val)));
                    res.value = val;
                    res.attempts = i + 1;
                    res.status = Result.Status.MINOR_ERROR;
                    container.putOutputToStream(res.getReport(), container.getReportStream());
                    break;
                }
            }
            if (i >= minorErrorAttempts) {
                res.attempts = minorErrorAttempts;
                res.status = Result.Status.FATAL_ERROR;
                container.putOutputToStream(res.getReport(), container.getReportStream());
                return res;
            }
        }
        try {
            if (allTime <= maxTimeCount) {
                Thread.sleep(sleepTime);
                if (val < 0) {
                    return res;
                }
            } else {
                Thread.sleep(maxTimeCount);
                res.status = Result.Status.FATAL_ERROR;
                res.attempts = i;
                container.putOutputToStream("Computation cancelled -> time limit reached",
                        container.getReportStream());
                return res;
            }
        } catch (InterruptedException exception) {
            res.status = Result.Status.FATAL_ERROR;
            res.attempts = 0;
            container.putOutputToStream(res.getReport(), container.getReportStream());
            return res;
        }
        val = (int) Math.ceil(Math.log((double) val));
        res.value = val;
        res.attempts = 1;
        res.status = Result.Status.SUCCESS;
        container.putOutputToStream(res.getReport(), container.getReportStream());
        return res;
    }
}
