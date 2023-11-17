package com.example.scheduling;

// This file contains the main() function for the Scheduling
// simulation.  Init() initializes most of the variables by
// reading from a provided file.  SchedulingAlgorithm.Run() is
// called from main() to run the simulation.  Summary-Results
// is where the summary results are written, and Summary-Processes
// is where the process scheduling summary is written.

// Created by Alexander Reeder, 2001 January 06

import java.io.*;
import java.util.*;
// import sProcess;
// import Common;
// import Results;
// import SchedulingAlgorithm;

public class Scheduling {

  //private static int processnum = 5;
  private static int runTimeAverage = 1000;
  private static int runTimeStdDev = 100;
  private static int runtime = 1000;
  private static List<User> userList = new ArrayList<>();
  private static List<sProcess> processList = new ArrayList<>();
  private static Results result = new Results("null", "null", 0);
  private static String resultsFile = "D:\\Java\\OS_Labs\\Lab2\\demo\\src\\main\\java\\com\\example\\output\\Summary-Results";

  private static void Init(String file) {
    File f = new File(file);
    String line;
    int cputime = 0;
    int ioblocking = 0;
    double X = 0.0;
    Scanner in;
    try {
      in = new Scanner(f);
      while (in.hasNextLine()) {
        line = in.nextLine();
        // if (line.startsWith("numprocess")) {
        //   StringTokenizer st = new StringTokenizer(line);
        //   st.nextToken();
        //   processnum = Common.s2i(st.nextToken());
        // }
        if (line.startsWith("meandev")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          runTimeAverage = Common.s2i(st.nextToken());
        }
        if (line.startsWith("standdev")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          runTimeStdDev = Common.s2i(st.nextToken());
        }
        if(line.startsWith("user")){
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          int pNum = Common.s2i(st.nextToken());
          int w = Common.s2i(st.nextToken());
          String name = st.nextToken();
          userList.add(new User(pNum, w, name));
        }
        if (line.startsWith("process")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          ioblocking = Common.s2i(st.nextToken());
          X = Common.R1();
          while (X == -1.0) {
            X = Common.R1();
          }
          X = X * runTimeStdDev;
          cputime = (int) X + runTimeAverage;
          int IOBlockedTime = 0;
          try{
            IOBlockedTime = Integer.parseInt(st.nextToken().trim());
          }catch(NumberFormatException e){
            e.printStackTrace();
          }
          processList.add(new sProcess(cputime, ioblocking, 0, 0, 0, processList.size(), IOBlockedTime));
        }
        if (line.startsWith("runtime")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          runtime = Common.s2i(st.nextToken());
        }
      }
      in.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }


  public static void main(String[] args) {
    int i = 0;
    String filepath = "D:\\Java\\OS_Labs\\Lab2\\demo\\src\\main\\java\\com\\example\\scheduling\\scheduling.conf";
    File f = new File(filepath);
    if (!(f.exists())) {
      System.out.println("Scheduling: error, file '" + f.getName() + "' does not exist.");
      System.exit(-1);
    }
    if (!(f.canRead())) {
      System.out.println("Scheduling: error, read of " + f.getName() + " failed.");
      System.exit(-1);
    }
    System.out.println("Working...");
    Init(filepath);
    int processnum = 0;
    for(User user : userList){
      processnum += user.processNum;
    }
    if (processList.size() < processnum) {
      i = 0;
      while (processList.size() < processnum) {
        double X = Common.R1();
        while (X == -1.0) {
          X = Common.R1();
        }
        X = X * runTimeStdDev;
        int cputime = (int) X + runTimeAverage;
        processList.add(new sProcess(cputime, i * 100, 0, 0, 0, processList.size(), 0));
        i++;
      }
    }
    int j = 0;
    for(User user : userList){
      for(int k = 0; k < user.processNum; k++){
        user.processes.add(processList.get(j));
        j++;
      }
    } 

    SchedulingAlgorithm scheduler = new SchedulingAlgorithm(userList, runtime, result);
    result = scheduler.run();
    try {
      // BufferedWriter out = new BufferedWriter(new FileWriter(resultsFile));
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      out.println("Scheduling Type: " + result.schedulingType);
      out.println("Scheduling Name: " + result.schedulingName);
      out.println("Simulation Run Time: " + result.compuTime);
      out.println("Mean: " + runTimeAverage);
      out.println("Standard Deviation: " + runTimeStdDev);
      out.println("Process\tCPU Time\tIO Blocking\tCPU Completed\tCPU Blocked");
      for (i = 0; i < result.processes.size(); i++) {
        sProcess process = (sProcess) result.processes.get(i);
        out.print(Integer.toString(i));
        if (i < 100) {
          out.print("\t\t");
        } else {
          out.print("\t");
        }
        out.print(Integer.toString(process.cputime));
        if (process.cputime < 100) {
          out.print(" (ms)\t\t");
        } else {
          out.print(" (ms)\t");
        }
        out.print(Integer.toString(process.ioblocking));
        if (process.ioblocking < 100) {
          out.print(" (ms)\t\t");
        } else {
          out.print(" (ms)\t");
        }
        out.print(Integer.toString(process.cpudone));
        if (process.cpudone < 100) {
          out.print(" (ms)\t\t");
        } else {
          out.print(" (ms)\t");
        }
        out.println(process.numblocked + " times\n");
      }
      out.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    System.out.println("Completed.");
  }
}
