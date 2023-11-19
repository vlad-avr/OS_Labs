package com.example.scheduling;

import java.util.ArrayList;
import java.util.List;

// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.io.*;

public class SchedulingAlgorithm {

  private PrintStream out;
  private List<User> users;
  private int runtime;
  private Results result;
  private final int maxQuantum;
  private final int skipQuantum;

  public SchedulingAlgorithm(List<User> users, int runtime, Results result, int maxQuantum, int skipQuantum) {
    this.users = users;
    this.runtime = runtime;
    this.result = result;
    this.maxQuantum = maxQuantum;
    this.skipQuantum = skipQuantum;
    try {
      out = new PrintStream(new FileOutputStream(
          "D:\\Java\\OS_Labs\\Lab2\\demo\\src\\main\\java\\com\\example\\output\\Summary-Processes"));
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }

  public Results run() {
    int comptime = 0;
    int currentUserID = 0;
    int quantumLeft = maxQuantum;
    List<sProcess> blockedProcesses = new ArrayList<>();
    User curUser = null;
    // int gWeight = 0;
    // for(User user : users){
    //   gWeight += user.weight;
    // }
    // boolean isCompleted = false;
    result.schedulingType = "Preemptive";
    result.schedulingName = "Fair-Share";
    while (comptime < runtime && !users.isEmpty()) {
      curUser = users.get(currentUserID);
      if(curUser.processes.size() == 0){
        continue;
      }
      for(int i = 0; i < curUser.weight; curUser.curProcessId++){
        curUser.curProcessId = curUser.curProcessId % curUser.processes.size();
        sProcess process = curUser.processes.get(curUser.curProcessId);
        if(process.IOblocked){
          out.println("Process: " + process.id + " held by " + curUser.name + " is still IOblocked (skipped)");
          if(quantumLeft <= skipQuantum){
            quantumLeft = maxQuantum;
            i++;
          }else{
            quantumLeft -= skipQuantum;
          }
          int cSize = blockedProcesses.size();
          for(int k = 0; k < cSize; k++){
            sProcess blockedProcess = blockedProcesses.get(k);
            if(blockedProcess.DecreaseWait(skipQuantum)){
              blockedProcesses.remove(k);
              k--;
              cSize--;
            }
          }
          continue;
        }
        out.println("Process: " + process.id + " held by " + curUser.name + " registered (" + process.toString() + ")");
        int elapsedTime = Math.min(quantumLeft, Math.min(process.timeToComplete(), process.timeToIOBlock()));
        process.addTime(elapsedTime);
        if(quantumLeft == elapsedTime){
          out.println(
              "Process: " + process.id + " held by " + curUser.name + " blocked (exceeded quantum) (" + process.toString() + ")");
          quantumLeft = maxQuantum;
          i++;
        }else if(process.timeToIOBlock() == elapsedTime){
          quantumLeft -= process.timeToIOBlock();
          out.println("Process: " + process.id + " held by " + curUser.name + " I/O blocked (" + process.toString() + ")");
          process.numblocked++;
          process.IOblocked = true;
          blockedProcesses.add(process);
        }
        if(process.cpudone == process.cputime){
          out.println("Process: " + process.id + " held by " + curUser.name + " completed (" + process.toString() + ")");
          curUser.processes.remove(process);
          result.processes.add(process);
          if(curUser.processes.size() == 0){
            break;
          }
          if(curUser.curProcessId == curUser.processes.size()){
            curUser.curProcessId = -1;
          }
        }
      }
      currentUserID = (currentUserID+1)%users.size();
      if(curUser.processes.size() == 0){
        users.remove(curUser);
        if(currentUserID == users.size()){
          currentUserID = 0;
        }
      }
    }

    out.close();
    result.compuTime = comptime;
    return result;
  }
}
