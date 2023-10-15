package com.example.func;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FunctionContainer extends Thread {
    private Function function;
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private PipedOutputStream outputStream;
    private PipedInputStream inputStream;
    private PipedOutputStream outputReport;
    private int minorErrorAttempts;
    private String value;
    public static boolean allDone = false;

    public FunctionContainer(Function function, int minorErrorAttempts, PipedOutputStream fromManager,
            PipedInputStream toManager, PipedInputStream toReport) {

        outputReport = new PipedOutputStream();
        outputStream = new PipedOutputStream();
        inputStream = new PipedInputStream();
        connectToOutputStream(toManager, outputStream);
        connectToOutputStream(inputStream, fromManager);
        connectToOutputStream(toReport, outputReport);
        this.function = function;
        this.function.setContainer(this);
        this.minorErrorAttempts = minorErrorAttempts;
    }

    public PipedOutputStream getReportStream() {
        return this.outputReport;
    }

    public void connectToOutputStream(PipedInputStream inputStream, PipedOutputStream outputStream) {
        try {
            inputStream.connect(outputStream);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public String getInputFromStream(PipedInputStream inputStream) {
        try {
            ObjectInputStream in = new ObjectInputStream(inputStream);
            return (String) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    public void putOutputToStream(String output, PipedOutputStream outputStream) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(output);
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
        try {
            while (inputStream.available() == 0) {
                if (allDone) {
                    service.shutdown();
                    return;
                }
                continue;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        value = getInputFromStream(inputStream);
        Future<Result> result = compute();
        while (!result.isDone() && !result.isCancelled() && !allDone) {
            try {
                if (inputStream.available() != 0) {
                    String input = getInputFromStream(inputStream);
                    switch (input) {
                        case "s":
                            result.cancel(true);
                            System.out.println("\nComputations canceled!");
                            break;
                    }
                }
            } catch (IOException | CancellationException e) {
                System.out.println(e.getMessage());
            }
        }
        if (!result.isCancelled() && !allDone) {
            try {
                putOutputToStream(result.get().toString(), outputStream);
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
            }
        }
        service.shutdown();
    }
}
