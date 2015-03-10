/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;
import gmpte.databaseinterface.BusInfo;


/**
 *
 * @author mbax3jw3
 */
public class Bus 
{
  private final int busID;   
  private final String busNumber;   
  private boolean available;  
  
  public Bus(int id)
  {
    busID = id;
    busNumber = BusInfo.getNumber(id);
  } // Bus
  
  public int getBusId()
  {
    return busID;
  } // getBusId
  
  public String getBusNumber()
  {
    return busNumber;
  } // getBusNumber
} // class bus
