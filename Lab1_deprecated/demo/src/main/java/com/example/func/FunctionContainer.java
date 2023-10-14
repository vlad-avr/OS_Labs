package com.example.func;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FunctionContainer extends Thread {
    private Function function;
    private ExecutorService service = Executors.newSingleThreadExecutor();
    // private PipedInputStream inputStream = new PipedInputStream();
    // private PipedOutputStream outputStream = new PipedOutputStream();
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private int minorErrorAttempts;
    private String value;

    public FunctionContainer(Function function, int minorErrorAttempts, PipedOutputStream fromManager,
            PipedInputStream toManager) {
        PipedOutputStream output = new PipedOutputStream();
        PipedInputStream input = new PipedInputStream();
        connectToOutputStream(toManager, output);
        connectToOutputStream(input, fromManager);
        try {
            outputStream = new ObjectOutputStream(output);
            inputStream = new ObjectInputStream(input);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        this.function = function;
        this.minorErrorAttempts = minorErrorAttempts;
    }

    public void connectToOutputStream(PipedInputStream inputStream, PipedOutputStream outputStream) {
        try {
            inputStream.connect(outputStream);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public String getInputFromStream() {
        try {
            return inputStream.readUTF();
            //return (String) ois.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void putOutputToStream(String output) {
        try {
            outputStream.writeUTF(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public void setValue(String value) {
    // this.value = value;
    // }

    private Future<Result> compute() {
        return service.submit(new Callable<Result>() {
            public Result call() {
                return function.compute(value, minorErrorAttempts);
            }
        });
    }

    @Override
    public void run() {
        while (true) {
            value = getInputFromStream();
            if (value != "") {
                break;
            }
        }
        Future<Result> result = compute();
        while (!result.isDone()) {
            try {
                if (inputStream.available() != 0) {
                    String input = getInputFromStream();
                    switch (input) {
                        case "s": // cancel
                            break;
                        case "r": // report
                            break;
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        putOutputToStream(result.toString());
    }
}
