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

    private ObjectInputStream inputStreamF;
    private ObjectOutputStream outputStreamF;
    private ObjectInputStream inputStreamG;
    private ObjectOutputStream outputStreamG;
    private FunctionContainer functionF;
    private FunctionContainer functionG;

    public Manager() {
        PipedInputStream inF = new PipedInputStream();
        PipedOutputStream outF = new PipedOutputStream();
        PipedInputStream inG = new PipedInputStream();
        PipedOutputStream outG = new PipedOutputStream();
        functionF = new FunctionContainer(new FunctionF(), 10, outF, inF);
        functionG = new FunctionContainer(new FunctionG(), 8, outG, inG);
        try {
            inputStreamF = new ObjectInputStream(inF);
            outputStreamF = new ObjectOutputStream(outF);
            inputStreamG = new ObjectInputStream(inG);
            outputStreamG = new ObjectOutputStream(outG);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void putOutputToStream(String output, ObjectOutputStream outputStream) {
        try {
            outputStream.writeObject(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getInputFromStream(ObjectInputStream inputStream) {
        try {
            return (String) inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void calculate(String value) {
        functionF.setValue(value);
        functionG.setValue(value);
        functionF.start();
        functionG.start();
        Result resF = null;
        Result resG = null;
        while (true) {
            try {
                if (resF != null && resG != null) {
                    break;
                }
                if (inputStreamF.available() != 0) {
                    resF = new Result(getInputFromStream(inputStreamF));
                    resF.setFunctionName("F(x)");
                }
                if (inputStreamG.available() != 0) {
                    resG = new Result(getInputFromStream(inputStreamG));
                    resG.setFunctionName("G(x)");
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        System.out.println(resF.toString());
        System.out.println(resG.toString());
    }

    public int GCD(int n2, int n1) {
        // System.out.println("\n\t " + n2 + "\t " + n1);
        if (n2 == 0) {
            return n1;
        }
        return GCD(n2, n1 % n2);
    }

}
