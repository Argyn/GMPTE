/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

/**
 *
 * @author mbgm2rm2
 */
public class BusReserve {
  private int startTime;
  private int endTime;
  
  public BusReserve(int startTime, int endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }
  
  public int getStartTime()
  {
    return startTime;
  }
  
  public int getEndTime()
  {
    return endTime;
  }
  
  public void setEndTime(int endTime)
  {
    this.endTime = endTime;
  }
  
  
  public boolean clashes(BusReserve other) {
    return (other.endTime > startTime && other.endTime < endTime)
            || (other.startTime > startTime && other.startTime < endTime)
            || (other.startTime < startTime && other.endTime > endTime);
  }
}
