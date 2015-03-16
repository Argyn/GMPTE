/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;
import gmpte.databaseinterface.BusInfo;
import gmpte.databaseinterface.database;


/**
 *
 * @author mbax3jw3
 */
public class Bus 
{
  private final int busID;   
  private final String busNumber;   
  
  public Bus(int id)
  {
    // open database connection
    database.openBusDatabase();
    busID = id;
    busNumber = BusInfo.busNumber(id);
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
