/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

import gmpte.db.BusInfo;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author mbax3jw3
 */
public class Bus
{
  private final int busID;   
  private final int busNumber; 
  
  private int occupiedStartTime;
  private int occupiedEndTime;
  private ArrayList<BusReserve> reservations;
          
  public Bus(int busID, int busNumber)
  {
    this.busID = busID;
    this.busNumber = busNumber;
    flashReservations();
  } // Bus
  
  public int getBusId()
  {
    return busID;
  } // getBusId
  
  public int getBusNumber()
  {
    return busNumber;
  } // getBusNumber
  
  public String toString() {
      return "BusID: "+busID+", BusNumber: "+busNumber;
  }
  
  public boolean isAvailable(int startTime, int endTime) {
    BusReserve candidat = new BusReserve(startTime, endTime);
    Iterator<BusReserve> busReservesIt = reservations.iterator();
    while(busReservesIt.hasNext()) {
      if(busReservesIt.next().clashes(candidat))
        return false;
    }
    
    return true;
  }
  
  public boolean reserve(int startTime, int endTime) {
    if(isAvailable(startTime, endTime)) {
      reservations.add(new BusReserve(startTime, endTime));
      return true;
    }
    return false;
  }
  
  public void flashReservations() {
    reservations = new ArrayList<BusReserve>();
  }
} // class bus
