package com.example.ontko;

public class sProcess {
  public int index;
  /**
   * The time the process should run to complete
   */
  public int cputime;
  /**
   * The time between I/O blocks of the process
   */
  public int ioblocking;
  /**
   * The time the process worked
   */
  public int cpudone;
  /**
   * Counter for I/O blocks
   */
  public int ionext;
  /**
   * Number of times the process was I/O blocked
   */
  public int numblocked;
  /**
   * Priority weight
   */
  public int weight = 2;

  public sProcess (int index, int cputime, int ioblocking, int cpudone, int ionext, int numblocked) {
    this.index = index;
    this.cputime = cputime;
    this.ioblocking = ioblocking;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
  }

  public String toString() {
    return String.format("done: %d/%d, I/O blocks: %d, I/O interval: %d", cpudone, cputime, numblocked, ioblocking);
  }

  public int timeToComplete() {
    return cputime - cpudone;
  }

  public int timeToIOBreak() {
    return ioblocking - ionext;
  }

  /**
   * add spent time
   */
  public void proceed(int time) {
    cpudone += time;
    ionext += time;
  }
}