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

    public Manager() {
        outputStreamF = new PipedOutputStream();
        inputStreamF = new PipedInputStream();
        outputStreamG = new PipedOutputStream();
        inputStreamG = new PipedInputStream();
        functionF = new FunctionContainer(new FunctionF(), 10, outputStreamF, inputStreamF);
        functionG = new FunctionContainer(new FunctionG(), 8, outputStreamG, inputStreamG);
        // try {
        // outputStreamF = new ObjectOutputStream(outF);
        // inputStreamF = new ObjectInputStream(inF);
        // outputStreamG = new ObjectOutputStream(outG);
        // inputStreamG = new ObjectInputStream(inG);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }

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
    // public String getInputFromStreamG() {
    //     try {
    //         ObjectInputStream ois = new ObjectInputStream(inputStreamG);
    //         return ois.readUTF();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return "";
    //     }
    // }
    public void calculate(String value) {
        if (functionF.isAlive() || functionG.isAlive()) {
            System.out.println(
                    "\nThere is an ongoing computation present right now. You can not start another one - cancel first!\n");
            return;
        }
        putOutputToStream(value, outputStreamF);
        putOutputToStream(value, outputStreamG);
        functionF.start();
        functionG.start();
        Result resF = null;
        Result resG = null;
        while (true) {
            try {
                if (resF != null && resG != null) {
                    break;
                }
                if(inputStreamF.available() > 0){
                    String str = getInputFromStream(inputStreamF);
                    System.out.println(str);
                    resF = new Result(str);
                    resF.setFunctionName("F(x)");
                }
                if(inputStreamG.available() > 0){
                    String str = getInputFromStream(inputStreamG);
                    System.out.println(str);
                    resG = new Result(str);
                    resG.setFunctionName("G(x)");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        resF.show();
        resG.show();
        System.out.println(GCD(resF.value, resG.value));
    }

    public int GCD(int n1, int n2) {
        // System.out.println("\n\t " + n2 + "\t " + n1);
        if (n2 == 0) {
            return n1;
        }
        return GCD(n2, n1 % n2);
    }

}
