package com.example.scheduling;

import java.util.ArrayList;
import java.util.List;

// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.io.*;

public class SchedulingAlgorithm {

  private List<sProcess> processes;
  private int runtime;
  private Results result;

  public SchedulingAlgorithm(List<sProcess> processes, int runtime, Results result){
    this.processes = processes;
    this.runtime = runtime;
    this.result = result;
  }

  public Results run() {
    int i = 0;
    int comptime = 0;
    int currentProcess = 0;
    int previousProcess = 0;
    int size = processes.size();
    int completed = 0;
    String resultsFile = "D:\\Java\\OS_Labs\\Lab2\\demo\\src\\main\\java\\com\\example\\scheduling\\Summary-Processes";

    result.schedulingType = "Preemptive";
    result.schedulingName = "Fair-Share"; 
    try {
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      sProcess process = (sProcess) processes.get(currentProcess);
      out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
      while (comptime < runtime) {
        if (process.cpudone == process.cputime) {
          completed++;
          out.println("Process: " + currentProcess + " completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
          if (completed == size) {
            result.compuTime = comptime;
            out.close();
            return result;
          }
          for (i = size - 1; i >= 0; i--) {
            process = (sProcess) processes.get(i);
            if (process.cpudone < process.cputime) { 
              currentProcess = i;
            }
          }
          process = (sProcess) processes.get(currentProcess);
          out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
        }      
        if (process.ioblocking == process.ionext) {
          out.println("Process: " + currentProcess + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
          process.numblocked++;
          process.ionext = 0; 
          previousProcess = currentProcess;
          for (i = size - 1; i >= 0; i--) {
            process = (sProcess) processes.get(i);
            if (process.cpudone < process.cputime && previousProcess != i) { 
              currentProcess = i;
            }
          }
          process = (sProcess) processes.get(currentProcess);
          out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
        }        
        process.cpudone++;       
        if (process.ioblocking > 0) {
          process.ionext++;
        }
        comptime++;
      }
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
    result.compuTime = comptime;
    return result;
  }
}
