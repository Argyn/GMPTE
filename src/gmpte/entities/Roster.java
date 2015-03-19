/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

import java.util.Date;
/**
 *
 * @author mbgm2rm2
 */


public class Roster 
{
  private Driver driver;
  private Bus bus;
  private Service service;
  private int day;
  private int serviceTime;
  private Date date;
  private Route route;
  
  public Roster(Driver newDriver, Bus newBus, Route route, Service newService, int weekDay, Date date)
  {
    driver = newDriver;
    bus = newBus;
    service = newService;
    day = weekDay;
    serviceTime = service.getServiceLengthMinutes();
    this.route = route;
    this.date = date;
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
  
  public Date getDate()
  {
    return date;
  }
  
  public Route getRoute() {
    return route;
  }
  
  public void setDriver(Driver setDriver)
  {
    driver = setDriver;
  }
  
  public void setBus(Bus setBus)
  {
    bus = setBus;
  }
  
  public void setService(Service setService)
  {
    service = setService;
  }
  
  public void setDay(int setDay)
  {
    day = setDay;
  }
  
  public void setTime(int setTime)
  {
    serviceTime = setTime;
  }
  
  public void storeRoster()
  {
    
  }
  
  public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Driver: "+driver);
      builder.append("\n");
      builder.append("Bus: "+bus);
      builder.append("\n");
      builder.append("Service : "+service);
      builder.append("\n");
      return builder.toString();
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