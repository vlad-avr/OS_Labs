package com.example.func;

public class FunctionF extends Function {

    public Result compute(String input, int minorErrorAttempts) {
        Result res = new Result();
        container.putOutputToStream(res.getReport(), container.getReportStream());
        int val = 0;
        try {
            val = Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            res.status = Result.Status.FATAL_ERROR;
            res.attempts = 0;
            container.putOutputToStream(res.getReport(), container.getReportStream());
            return res;
        }
        int sleepTime = rnd.nextInt(12000) + 6000;
        int allTime = sleepTime;
        int i = 0;
        System.out.println("\n F(x) will compute for at least " + sleepTime + " ms");
        if (val < 0) {
            for (; i < minorErrorAttempts; i++) {
                int flip = rnd.nextInt(2);
                if (flip == 0) {
                    try {
                        Thread.sleep(500);
                        allTime += 500;
                    } catch (InterruptedException exception) {
                        res.attempts = i + 1;
                        res.status = Result.Status.FATAL_ERROR;
                        container.putOutputToStream(res.getReport(), container.getReportStream());
                        return res;
                    }
                    continue;
                } else {
                    val = (int) Math.ceil(Math.sqrt((double) Math.abs(val)));
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
                container.putOutputToStream("Computation cancelled -> time limit reached", container.getReportStream());
                return res;
            }
        } catch (InterruptedException exception) {
            res.status = Result.Status.FATAL_ERROR;
            res.attempts = 0;
            container.putOutputToStream(res.getReport(), container.getReportStream());
            return res;
        }
        val = (int) Math.ceil(Math.sqrt((double) val));
        res.value = val;
        res.attempts = 1;
        res.status = Result.Status.SUCCESS;
        container.putOutputToStream(res.getReport(), container.getReportStream());
        return res;
    }

}
