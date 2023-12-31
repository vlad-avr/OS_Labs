package com.example.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashMap;

import com.example.func.FunctionContainer;
import com.example.func.FunctionF;
import com.example.func.FunctionG;
import com.example.func.Result;

import javafx.util.Pair;

public class Manager {

    private PipedInputStream inputStreamF;
    private PipedOutputStream outputStreamF;
    private PipedInputStream inputStreamG;
    private PipedOutputStream outputStreamG;
    private PipedInputStream inputReportF;
    private PipedInputStream inputReportG;
    private FunctionContainer functionF;
    private FunctionContainer functionG;
    public boolean isComputing = false;
    private boolean isCancelled = false;
    private String reportF;
    private String reportG;
    private HashMap<String, Pair<Result, Result>> memo = new HashMap<>();

    public Manager() {

    }

    public void Done() {
        FunctionContainer.allDone = true;
    }

    public void printReport() {
        System.out.println("\nREPORT:\n");
        System.out.println("Report on F(x) : " + reportF);
        System.out.println("Report on G(x) : " + reportG);
    }

    public void cancel() {
        putOutputToStream("s", outputStreamF);
        putOutputToStream("s", outputStreamG);
        isCancelled = true;
    }

    public void putOutputToStream(String output, PipedOutputStream outputStream) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(output);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getInputFromStream(PipedInputStream inputStream) {
        try {
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            return (String) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    public void calculate(String value) {
        isComputing = true;
        Result resF = null;
        Result resG = null;
        if (!memo.containsKey(value)) {
            outputStreamF = new PipedOutputStream();
            inputStreamF = new PipedInputStream();
            outputStreamG = new PipedOutputStream();
            inputStreamG = new PipedInputStream();
            inputReportF = new PipedInputStream();
            inputReportG = new PipedInputStream();
            functionF = new FunctionContainer(new FunctionF(), 10, outputStreamF, inputStreamF, inputReportF);
            functionG = new FunctionContainer(new FunctionG(), 8, outputStreamG, inputStreamG, inputReportG);
            functionF.start();
            functionG.start();
            putOutputToStream(value, outputStreamF);
            putOutputToStream(value, outputStreamG);
            while (!isCancelled) {
                try {
                    if (inputReportF.available() > 0) {
                        reportF = getInputFromStream(inputReportF);
                    }
                    if (inputReportG.available() > 0) {
                        reportG = getInputFromStream(inputReportG);
                    }
                    if (resF != null && resG != null) {
                        break;
                    }
                    if (inputStreamF.available() > 0) {
                        String str = getInputFromStream(inputStreamF);
                        resF = new Result(str);
                        resF.setFunctionName("F(x)");
                    }
                    if (inputStreamG.available() > 0) {
                        String str = getInputFromStream(inputStreamG);
                        resG = new Result(str);
                        resG.setFunctionName("G(x)");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (!isCancelled) {
                memo.put(value, new Pair<Result, Result>(resF, resG));
            }
        } else {
            System.out.println("\nValue " + value + " has already been computed before");
            reportF = "Computations weren`t needed -> result loaded from memoization map";
            reportG = "Computations weren`t needed -> result loaded from memoization map";
            Pair<Result, Result> results = memo.get(value);
            resF = results.getKey();
            resG = results.getValue();
        }
        if (isCancelled) {
            reportF = "Computation was cancelled by user";
            reportG = "Computation was cancelled by user";
        }
        if (!isCancelled) {
            resF.show();
            resG.show();
            if (resF.status == Result.Status.FATAL_ERROR || resG.status == Result.Status.FATAL_ERROR) {
                System.out.println(
                        "\nUnable to compute GCD because one or more functions caught Fatal Errors during computation!");
            } else {
                System.out.println("\n GCD : " + GCD(resF.value, resG.value));
            }
        } else {
            System.out.println("\n All computations have been cancelled by user : unable to compute GCD");
        }
        isComputing = false;
        isCancelled = false;
    }

    public int GCD(int n1, int n2) {
        if (n2 == 0) {
            return n1;
        }
        return GCD(n2, n1 % n2);
    }

}
