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
  private final int maxQuantum = 250;
  private final int skipQuantum = 20;

  public SchedulingAlgorithm(List<User> users, int runtime, Results result) {
    this.users = users;
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
      int j = 0;
      for(int i = 0; i < curUser.weight; j++){
        sProcess process = curUser.processes.get(j % curUser.processes.size());
        if(process.IOblocked){
          out.println("Process: " + process.id + " held by " + curUser.name + " is still IOblocked (skipped)");
          if(quantumLeft <= skipQuantum){
            quantumLeft = maxQuantum;
            i++;
          }else{
            quantumLeft -= skipQuantum;
          }
          for(sProcess blockedProcess : blockedProcesses){
            if(blockedProcess.DecreaseWait(skipQuantum)){
              blockedProcesses.remove(blockedProcess);
            }
          }
          continue;
        }
        out.println("Process: " + process.id + " held by " + curUser.name + " registered (" + process.toString() + ")");
        int timeToIOBlock = process.timeToIOBlock();
        if(quantumLeft < timeToIOBlock){
          process.addTime(quantumLeft);
          out.println(
              "Process: " + process.id + " held by " + curUser.name + " blocked (exceeded quantum) (" + process.toString() + ")");
          quantumLeft = maxQuantum;
          i++;
        }else{
          quantumLeft -= timeToIOBlock;
          process.addTime(timeToIOBlock);
          out.println("Process: " + process.id + " held by " + curUser.name + " I/O blocked (" + process.toString() + ")");
          process.IOblocked = true;
        }

        if(process.cpudone == process.cputime){
          out.println("Process: " + process.id + " held by " + curUser.name + " completed (" + process.toString() + ")");
          curUser.processes.remove(process);
          if(curUser.processes.size() == 0){
            break;
          }
          if(j == curUser.processes.size()){
            j = -1;
          }
        }

      }

      // curUser = users.get(currentUserID);
      // if(curUser.IOblocked){
      //   curUser.DecreaseWait(skipQuantum);
      //   currentUserID = (currentUserID + 1) % users.size();
      //   fairnessCounter = 0;
      //   out.println("Process: " + curUser.id + " is still blocked (skipped)");
      //   for(sProcess p : users){
      //     if(p.IOblocked && p.id != currentUserID){
      //       p.DecreaseWait(skipQuantum);
      //     }
      //   }
      //   continue;
      // }
      // out.println("Process: " + curUser.id + " registered (" + curUser.toString() + ")");

      // int timeToIOBlock = curUser.timeToIOBlock();
      // int elapsedTime = Math.min(quantum, Math.min(curUser.timeToComplete(), timeToIOBlock));
      // curUser.addTime(elapsedTime);

      // if (curUser.cpudone == curUser.cputime) {
      //   out.println("Process: " + curUser.id + " completed (" + curUser.toString() + ")");
      //   users.remove(currentUserID);
      //   if (currentUserID == users.size()) {
      //     currentUserID = 0;
      //   }
      //   curUser = null;
      //   continue;
      // } else {
      //   if (timeToIOBlock <= elapsedTime) {
      //     out.println("Process: " + curUser.id + " I/O blocked (" + curUser.toString() + ")");
      //     curUser.numblocked++;
      //     curUser.IOblocked = true;
      //     curUser.ionext = 0;
      //   } else {
      //     out.println(
      //         "Process: " + curUser.id + " blocked (exceeded quantum) (" + curUser.toString() + ")");
      //   }
      // }

      // comptime += elapsedTime;
      // fairnessCounter++;

      // if (curUser != null && fairnessCounter == curUser.priority) {
      //   currentUserID = (currentUserID + 1) % users.size();
      //   fairnessCounter = 0;
      // } else {
      //   if (users.isEmpty()) {
      //     break;
      //   }
      // }
      // for(sProcess p : users){
      //   if(p.IOblocked){
      //     p.DecreaseWait(elapsedTime);
      //   }
      // }
    }

    out.close();
    result.compuTime = comptime;
    return result;
  }
}
