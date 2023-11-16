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
  //Time of blocking for IO
  public int IOBlockedTime;
  //Time of blocking for IO left
  public int timeToIOBlockEnd;
  //Process is blocked for IO
  public boolean IOblocked = false;

  //priority
  public int priority;

  //process id
  public int id;

  public sProcess (int cputime, int ioblocking, int cpudone, int ionext, int numblocked, int priority, int id, int IOBlockedTime) {
    this.cputime = cputime;
    this.ioblocking = ioblocking;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
    this.priority = priority;
    this.id = id;
    this.IOBlockedTime = IOBlockedTime;
    this.timeToIOBlockEnd = IOBlockedTime;
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
