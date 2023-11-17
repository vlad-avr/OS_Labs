package com.example.scheduling;

import java.util.ArrayList;
import java.util.List;

public class Results {
  public String schedulingType;
  public String schedulingName;
  public int compuTime;
  public List<sProcess> processes = new ArrayList<>();

  public Results (String schedulingType, String schedulingName, int compuTime) {
    this.schedulingType = schedulingType;
    this.schedulingName = schedulingName;
    this.compuTime = compuTime;
  } 	
}
