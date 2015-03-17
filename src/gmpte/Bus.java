/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;


/**
 *
 * @author mbax3jw3
 */
public class Bus 
{
  private final int busID;   
  private final int busNumber;   
  
  public Bus(int busID, int busNumber)
  {
    this.busID = busID;
    this.busNumber = busNumber;
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
} // class bus
