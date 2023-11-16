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
  private final int quantum = 250;
  private final int skipQuantum = 20;

  public SchedulingAlgorithm(List<sProcess> processes, int runtime, Results result) {
    this.processes = processes;
    this.runtime = runtime;
    this.result = result;
    try {
      out = new PrintStream(new FileOutputStream(
          "D:\\Java\\OS_Labs\\Lab2\\demo\\src\\main\\java\\com\\example\\output\\Summary-Processes"));
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }

  public Results run() {
    int comptime = 0;
    int currentProcessId = 0;
    int fairnessCounter = 0;
    sProcess currentProcess = null;
    // boolean isCompleted = false;
    result.schedulingType = "Preemptive";
    result.schedulingName = "Fair-Share";
    while (comptime < runtime && !processes.isEmpty()) {

      currentProcess = processes.get(currentProcessId);
      if(currentProcess.IOblocked){
        currentProcess.DecreaseWait(skipQuantum);
        out.println("Process: " + currentProcess.id + " is still blocked (skipped)");
        for(sProcess p : processes){
          if(p.IOblocked && p.id != currentProcessId){
            p.DecreaseWait(skipQuantum);
          }
        }
        continue;
      }
      out.println("Process: " + currentProcess.id + " registered (" + currentProcess.toString() + ")");

      int timeToIOBlock = currentProcess.timeToIOBlock();
      int elapsedTime = Math.min(quantum, Math.min(currentProcess.timeToComplete(), timeToIOBlock));
      currentProcess.addTime(elapsedTime);

      if (currentProcess.cpudone == currentProcess.cputime) {
        out.println("Process: " + currentProcess.id + " completed (" + currentProcess.toString() + ")");
        processes.remove(currentProcessId);
        if (currentProcessId == processes.size()) {
          currentProcessId = 0;
        }
        currentProcess = null;
        continue;
      } else {
        if (timeToIOBlock <= elapsedTime) {
          out.println("Process: " + currentProcess.id + " I/O blocked (" + currentProcess.toString() + ")");
          currentProcess.numblocked++;
          currentProcess.IOblocked = true;
          currentProcess.ionext = 0;
        } else {
          out.println(
              "Process: " + currentProcess.id + " blocked (exceeded quantum) (" + currentProcess.toString() + ")");
        }
      }

      comptime += elapsedTime;
      fairnessCounter++;

      if (currentProcess != null && fairnessCounter == currentProcess.priority) {
        currentProcessId = (currentProcessId + 1) % processes.size();
        fairnessCounter = 0;
      } else {
        if (processes.isEmpty()) {
          break;
        }
      }
      for(sProcess p : processes){
        if(p.IOblocked){
          p.DecreaseWait(elapsedTime);
        }
      }
    }

    out.close();
    result.compuTime = comptime;
    return result;
  }
}
