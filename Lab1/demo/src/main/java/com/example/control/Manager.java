package com.example.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.example.func.FunctionContainer;
import com.example.func.FunctionF;
import com.example.func.FunctionG;
import com.example.func.Result;

public class Manager {

    private PipedInputStream inputStreamF;
    private PipedOutputStream outputStreamF;
    private PipedInputStream inputStreamG;
    private PipedOutputStream outputStreamG;
    private FunctionContainer functionF;
    private FunctionContainer functionG;
    public boolean isComputing = false;
    private boolean isCancelled = false;

    public Manager() {
        outputStreamF = new PipedOutputStream();
        inputStreamF = new PipedInputStream();
        outputStreamG = new PipedOutputStream();
        inputStreamG = new PipedInputStream();
        functionF = new FunctionContainer(new FunctionF(), 10, outputStreamF, inputStreamF);
        functionG = new FunctionContainer(new FunctionG(), 8, outputStreamG, inputStreamG);
        functionF.start();
        functionG.start();
    }

    public void Done() {
        FunctionContainer.allDone = true;
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
        if (isComputing) {
            
        }
        isComputing = true;
        putOutputToStream(value, outputStreamF);
        putOutputToStream(value, outputStreamG);
        Result resF = null;
        Result resG = null;
        while (!isCancelled) {
            try {
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
