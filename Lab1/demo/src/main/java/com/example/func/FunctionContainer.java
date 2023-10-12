package com.example.func;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FunctionContainer {
    private Function function;
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private PipedInputStream inputStream = new PipedInputStream();
    private PipedOutputStream outputStream = new PipedOutputStream();

    public FunctionContainer(Function function) {
        this.function = function;
    }

     public void connectToInputStream(PipedInputStream inputStream) {
        try {
            this.outputStream.connect(inputStream);
            inputStream.connect(this.outputStream);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void connectToOutputStream(PipedOutputStream outputStream) {
        try {
            this.inputStream.connect(outputStream);
            outputStream.connect(this.inputStream);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public String getInputFromStream(int inputLen) {
        try {
            String res = "";
            for(int i = 0; i < inputLen; i++){
                res += (char)inputStream.read();
            }
            return res;
        } catch (IOException exception) {
            //Should not trigger
            System.out.println(exception.getMessage());
            return "";
        }
    }

    public void putOutputToStream(byte data){
        try {
            outputStream.write(data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
