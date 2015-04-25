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
public class BusStop implements Comparable<BusStop> {
  
  private int busStopID;
  private int areaCode;
  private int sequence;
  private String name;
  
  public BusStop(int busStopID, int areaCode, String name, int sequence) {
    this.busStopID = busStopID;
    this.areaCode = areaCode;
    this.sequence = sequence;
    this.name = name;
  }
  
  public int getID() {
    return busStopID;
  }
  
  public int getAreaCode() {
    return areaCode;
  }
  
  public String getName() {
    return name;
  }
  
  public int getSequence() {
    return sequence;
  }
  
  public String toString() {
    return name+"("+areaCode+")";
  }
  
  @Override
  public boolean equals(Object other) {
    BusStop otheBStop = (BusStop)other;
    return otheBStop.name.equals(name) && areaCode==otheBStop.areaCode;
  }

  @Override
  public int compareTo(BusStop o) {
    if(sequence < o.sequence)
      return -1;
    else
      return 1;
    
  }
}
