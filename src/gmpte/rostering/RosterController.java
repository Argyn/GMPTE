/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.rostering;

import gmpte.Driver;
import gmpte.Bus;
import gmpte.Service;
import gmpte.databaseinterface.DriverInfo;
import gmpte.databaseinterface.BusInfo;
import gmpte.databaseinterface.TimetableInfo;

/**
 *
 * @author mbgm2rm2
 */
public class RosterController {
  
  private Roster roster;
  
  public int generateRoster() 
  {
    int numberOfDrivers = 1; /*DriverInfo.getNumberOfDrivers()*/
    int[] driverId = DriverInfo.getDrivers();   
    Driver[] driverList = new Driver[numberOfDrivers];
    for (int index = 0; index < numberOfDrivers; index++)
    {
      driverList[index] = new Driver(driverId[index], Integer.parseInt(DriverInfo.getNumber(driverId[index])));
    } // for
    
    int numberOfBuses = 1; /*BusInfo.getNumberOfBuses()*/
    int[] busId = BusInfo.getBuses();
    Bus[] busList = new Bus[numberOfBuses];
    for (int index = 0; index < numberOfBuses; index++)
    {
      busList[index] = new Bus(busId[index]);
    } // for
    
    int numberOfServices = 1; /*TimetableInfo.getNumberOfServices()*/
    // need a getService method to return an int like of all service Ids
    /*int[] serviceId = TimetableInfo.getServices(); 
    Service[] serviceList = new Service[numberOfServices];
    for (int index = 0; index < numberOfServices; index++)
    {
      serviceList[index] = new serviceId(busId[index]);
    } // for */
    
    
    return 1;
  }
  /*
  public Roster[] getRosterForDriver(Driver driver) 
  {
  }
  
  public Roster[] getRostersForAllDrivers() 
  {
  }
  
  public Driver[] getAvailableDrivers(Driver[] driverList, D)
  {
    Driver[] availableDrivers = new Driver[driverList.length];
    int noOfAvailableDrivers = 0;
    for (int index = 0; index < driverList.length; index++)
    {
      //if (driver is available on date && driver has worked less than 50 hours && driver has worked less than 10 hours in a day && there is enough time left for a driver to do another route)
      
    }
    return availableDrivers;
  } // getAvailableDrivers
 */
}
