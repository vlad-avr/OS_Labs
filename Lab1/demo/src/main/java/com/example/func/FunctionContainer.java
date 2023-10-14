package com.example.func;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FunctionContainer extends Thread {
    private Function function;
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private PipedOutputStream outputStream;
    private PipedInputStream inputStream;
    private int minorErrorAttempts;
    private String value;
    public static boolean allDone = false;

    public FunctionContainer(Function function, int minorErrorAttempts, PipedOutputStream fromManager,
            PipedInputStream toManager) {
        outputStream = new PipedOutputStream();
        inputStream = new PipedInputStream();
        connectToOutputStream(toManager, outputStream);
        connectToOutputStream(inputStream, fromManager);
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
            ObjectInputStream in = new ObjectInputStream(inputStream);
            return (String) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    public void putOutputToStream(String output) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(output);
            // out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private Future<Result> compute() {
        return service.submit(new Callable<Result>() {
            public Result call() {
                return function.compute(value, minorErrorAttempts);
            }
        });
    }

    @Override
    public void run() {
        while (!allDone) {
            try {
                while (inputStream.available() == 0) {
                    if(allDone){
                        return;
                    }
                    //System.out.println("NO INPUT");
                    continue;
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            value = getInputFromStream();
            // System.out.println("\n" + value);
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
            try {
                putOutputToStream(result.get().toString());
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
