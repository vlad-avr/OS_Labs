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
            oos.writeUTF(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getInputFromStream(PipedInputStream inputStream) {
        try {
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            return (String) ois.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

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
                String str = getInputFromStream(inputStreamF);
                if (str != "") {
                    resF = new Result(str);
                    resF.setFunctionName("F(x)");
                }

                str = getInputFromStream(inputStreamG);
                if (str != "") {
                    resG = new Result(str);
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
