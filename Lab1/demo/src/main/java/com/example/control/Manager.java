package com.example.control;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.example.func.Result;

import java.util.concurrent.ExecutorService;

public class Manager {
    // private PipedInputStream f_input_stream;
    // private PipedOutputStream f_output_stream;
    // private PipedInputStream g_input_stream;
    // private PipedOutputStream g_output_stream;
    // private PipedInputStream controller_input_stream;
    // private PipedOutputStream controller_output_stream;
    private ExecutorService executors = Executors.newFixedThreadPool(3);

    public void start(){

    }

    private void setup(){

    }

    // private Future<Result> calculate_f(){

    // }

    // private Future<Result> calculate_g(){

    // }
}
