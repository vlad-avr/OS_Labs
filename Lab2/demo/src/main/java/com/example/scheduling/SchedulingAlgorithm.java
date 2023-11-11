package com.example.scheduling;

import java.util.List;

// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.io.*;

public class SchedulingAlgorithm {

  private PrintStream out;
  private List<sProcess> processes;
  private int runtime;
  private Results result;
  private final int quantum = 50;

  public SchedulingAlgorithm(List<sProcess> processes, int runtime, Results result) {
    this.processes = processes;
    this.runtime = runtime;
    this.result = result;
    try {
      out = new PrintStream(new FileOutputStream(
          "D:\\Java\\OS_Labs\\Lab2\\demo\\src\\main\\java\\com\\example\\scheduling\\Summary-Processes"));
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }

  public Results run() {
    int comptime = 0;
    int currentProcessId = 0;
    boolean isCompleted = false;
    result.schedulingType = "Preemptive";
    result.schedulingName = "Fair-Share";
    while (comptime < runtime && !processes.isEmpty()) {
      sProcess currentProcess = processes.get(currentProcessId);
      out.println("Process: " + currentProcess.id + " registered (" + currentProcess.toString() + ")");

      for (int i = 0; i < currentProcess.priority; i++) {
        int timeToIOBlock = currentProcess.timeToIOBlock();
        int elapsedTime = Math.min(quantum, Math.min(currentProcess.timeToComplete(), timeToIOBlock));
        currentProcess.addTime(elapsedTime);
        if (currentProcess.cpudone == currentProcess.cputime) {
          out.println("Process: " + currentProcess.id + " completed (" + currentProcess.toString() + ")");
          processes.remove(currentProcessId);
          if(currentProcessId == processes.size()){
            currentProcessId = 0;
          }
          isCompleted = true;
          i = currentProcess.priority;
          continue;
        } else {
          if (timeToIOBlock <= elapsedTime) {
            out.println("Process: " + currentProcess.id + " I/O blocked (" + currentProcess.toString() + ")");
            currentProcess.numblocked++;
            currentProcess.ionext = 0;
          } else {
            out.println(
                "Process: " + currentProcess.id + " blocked (exceeded quantum) (" + currentProcess.toString() + ")");
          }
        }
        comptime += elapsedTime;
      }
      if (!isCompleted) {
        currentProcessId = (currentProcessId + 1) % processes.size();
      } else {
        if (processes.isEmpty()) {
          break;
        }
        isCompleted = false;
      }
    }
    
    out.close();
    result.compuTime = comptime;
    return result;
  }
}
