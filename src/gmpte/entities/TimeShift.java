/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gmpte.entities;

/**
 *
 * @author argyn
 */
public class TimeShift implements Comparable<TimeShift> {
  private int startTime;
  private int endTime;
  
  public TimeShift(int startTime, int endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }
  
  public int getStartTime() {
    return startTime;
  }
  
  public int getEndTime() {
    return endTime;
  }

  @Override
  public int compareTo(TimeShift o) {
    if(startTime <= o.startTime)
      return -1;
    return 1;
  }
}
