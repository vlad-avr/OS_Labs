package com.example.scheduling;

public class sProcess {
  //Time to complete
  public int cputime;
  //Time between IO blocks
  public int ioblocking;
  //Time passed overall
  public int cpudone;
  //Time to next IO block
  public int ionext;
  //Number of times blocked
  public int numblocked;

  //priority
  public int priority;

  //process id
  public int id;

  public sProcess (int cputime, int ioblocking, int cpudone, int ionext, int numblocked, int priority, int id) {
    this.cputime = cputime;
    this.ioblocking = ioblocking;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
    this.priority = priority;
    this.id = id;
  }
  
  public String toString() {
    return String.format("done: %d/%d, I/O blocks: %d, I/O interval: %d", cpudone, cputime, numblocked, ioblocking);
  }

  public void addTime(int time) {
    cpudone += time;
    ionext += time;
  }

  public int timeToComplete(){
    return cputime - cpudone;
  }

  public int timeToIOBlock(){
    return ioblocking - ionext;
  }
}
