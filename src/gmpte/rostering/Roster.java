/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.rostering;
/**
 *
 * @author mbgm2rm2
 */

import gmpte.Driver;
import gmpte.Bus;
import gmpte.Service;

public class Roster 
{
  private Driver driver;
  private Bus bus;
  private Service service;
  private int day;
  private int serviceTime;
  
  public Roster(Driver newDriver, Bus newBus, Service newService, int weekDay)
  {
    driver = newDriver;
    bus = newBus;
    service = newService;
    day = weekDay;
    serviceTime = service.getServiceLength();
  } // Roster
  
  public Driver getDriver()
  {
    return driver;
  } // getDriver
  
  public Bus getBus()
  {
    return bus;
  } // getBus
    
  public Service getService()
  {
    return service;
  } // getService
      
  public int getDay()
  {
    return day;
  } // getDay
  
  public int getServiceTime()
  {
    return serviceTime;
  }  
  
  public void storeRoster()
  {
    
  }
}

/*
pseudo code for the roster.... still needs a shift class which stores an array of shifts 
derived from serivces  endTime - startTime

Since we have shifts now, we can assign buses to Shifts, and drivers shifts. 

public static int[] getAvailableDrivers(Date date)
  {
    int[] drivers = DriverInfo.getDrivers();
    int[] availableDrivers = new int[drivers.length - DriverInfo.getNumberOfUnavailableDrivers(date)];
    
    int availableDriversIndex = 0;
    for (int index = 0; index < drivers.length; index++)
      if (DriverInfo.isAvailable(drivers[index], date))
        availableDrivers[availableDriversIndex++] = drivers[index];
      
    return availableDrivers;     
  } // getAvailableDrivers


  public static int[] getAvailableBuses(Date date)
  {
    int[] buses = BusInfo.getBuses();
    int[] availableBuses = new int[buses.length - BusInfo.getNumberOfUnavailableBuses(date)];
    
    int availableBusesIndex = 0;
    for (int index = 0; index < buses.length; index++)
      if (BusInfo.isAvailable(buses[index], date))
        availableBuses[availableBusesIndex++] = buses[index];
      
    return availableBuses;
  } // getAvailableBuses  

  
  private static void addDriversToShifts()
  {
    
  } // addDriversToShifts


  private static void addBusesToShifts()
  {
    
  } // addDriversToShifts


  public static  generateRoster()
  {
    
  } // generateRoster


*/