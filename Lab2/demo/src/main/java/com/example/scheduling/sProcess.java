package com.example.scheduling;

public class sProcess {
  public int cputime;
  public int ioblocking;
  public int cpudone;
  public int ionext;
  public int numblocked;

  //priority
  public int priority;

  public sProcess (int cputime, int ioblocking, int cpudone, int ionext, int numblocked, int priority) {
    this.cputime = cputime;
    this.ioblocking = ioblocking;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
    this.priority = priority;
  } 	
}
