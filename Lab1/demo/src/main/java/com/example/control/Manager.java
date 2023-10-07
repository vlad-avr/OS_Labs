package com.example.control;

import java.io.IOError;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.example.func.FunctionF;
import com.example.func.FunctionG;
import com.example.func.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class Manager {
    // private PipedInputStream f_input_stream;
    // private PipedOutputStream f_output_stream;
    // private PipedInputStream g_input_stream;
    // private PipedOutputStream g_output_stream;
    // private PipedInputStream controller_input_stream;
    // private PipedOutputStream controller_output_stream;
    private PipedInputStream fInputStream = new PipedInputStream();
    private PipedInputStream gInputStream = new PipedInputStream();
    private PipedOutputStream fOutputStream = new PipedOutputStream();
    private PipedOutputStream gOutputStream = new PipedOutputStream();
    private ExecutorService executors = Executors.newFixedThreadPool(2);
    private String input;
    private List<Future<Result>> futures = new ArrayList<>();

    private void calculate(String input) {
        // BufferData bufferData = new BufferData(input);
        this.input = input;
        futures.add(calculateF());
        futures.add(calculateG());
    }

    private Future<Result> calculateF() {
        return executors.submit(new Callable<Result>() {
            public Result call() {
                FunctionF f = new FunctionF();
                f.connectToInputStream(fInputStream);
                f.connectToOutputStream(fOutputStream);
                try {
                    fOutputStream.write(input.getBytes(), 0, input.length());
                    fOutputStream.flush();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                return f.compute(f.getInputFromStream(input.length()), 10);
            }
        });
    }

    private Future<Result> calculateG() {
        return executors.submit(new Callable<Result>() {
            public Result call() {
                FunctionG g = new FunctionG();
                g.connectToInputStream(gInputStream);
                g.connectToOutputStream(gOutputStream);
                try {
                    gOutputStream.write(input.getBytes(), 0, input.length());
                    gOutputStream.flush();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                return g.compute(g.getInputFromStream(input.length()), 10);
            }
        });
    }

}
