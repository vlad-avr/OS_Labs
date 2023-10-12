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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class Manager {
    private PipedInputStream fInputStream = new PipedInputStream();
    private PipedInputStream gInputStream = new PipedInputStream();
    private PipedOutputStream fOutputStream = new PipedOutputStream();
    private PipedOutputStream gOutputStream = new PipedOutputStream();
    private ExecutorService executors = Executors.newFixedThreadPool(2);
    private String input;
    private List<Future<Result>> futures = new ArrayList<>();

    public void calculate(String input) {
        // BufferData bufferData = new BufferData(input);
        System.out.println("\n Calculating");
        this.input = input;
        // futures.add(calculateF());
        // futures.add(calculateG());
        while (!futures.get(0).isDone() && !futures.get(1).isDone()) {
            continue;
        }
        try {
            Result res1 = futures.get(0).get();
            Result res2 = futures.get(1).get();
            if (res1.status == Result.Status.FATAL_ERROR) {
                System.out.println("\nFatal error has occurred in Function F : unable to resume further computations");
            } else {
                System.out.println("\nFunction F finished with status : " + res1.status);
                System.out.println("Result value : " + res1.value);
            }
            if (res1.status == Result.Status.FATAL_ERROR) {
                System.out.println("\nFatal error has occurred in Function F : unable to resume further computations");
            } else {
                System.out.println("\nFunction G finished with status : " + res2.status);
                System.out.println("Result value : " + res2.value);
            }

            if (res1.status != Result.Status.FATAL_ERROR && res2.status != Result.Status.FATAL_ERROR) {
                System.out.println("\n Greatest common divisor : " + GCD(res1.value, res2.value));
            }
        } catch (InterruptedException | ExecutionException exception) {
            System.out.println(exception.getMessage());
        }
    }

    // public void cancel() {
    //     futures.get(0).cancel(true);
    //     futures.get(1).cancel(true);
    // }

    // private Future<Result> calculateF() {
    //     return executors.submit(new Callable<Result>() {
    //         public Result call() {
    //             FunctionF f = new FunctionF();
    //             f.connectToInputStream(fInputStream);
    //             f.connectToOutputStream(fOutputStream);
    //             try {
    //                 fOutputStream.write(input.getBytes(), 0, input.length());
    //                 fOutputStream.flush();
    //             } catch (IOException e) {
    //                 System.out.println(e.getMessage());
    //             }
    //             return f.compute(f.getInputFromStream(input.length()), 10);
    //         }
    //     });
    // }

    // private Future<Result> calculateG() {
    //     return executors.submit(new Callable<Result>() {
    //         public Result call() {
    //             FunctionG g = new FunctionG();
    //             g.connectToInputStream(gInputStream);
    //             g.connectToOutputStream(gOutputStream);
    //             try {
    //                 gOutputStream.write(input.getBytes(), 0, input.length());
    //                 gOutputStream.flush();
    //             } catch (IOException e) {
    //                 System.out.println(e.getMessage());
    //             }
    //             return g.compute(g.getInputFromStream(input.length()), 10);
    //         }
    //     });
    // }

    public int GCD(int n2, int n1) {
       // System.out.println("\n\t " + n2 + "\t " + n1);
        if (n2 == 0) {
            return n1;
        }
        return GCD(n2, n1 % n2);
    }

}
