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
import gmpte.Shift;
import gmpte.databaseinterface.BusInfo;
import gmpte.databaseinterface.DriverInfo;
import java.util.Date;
import java.util.ArrayList;

public class Roster 
{
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

  
  private static void addDriversToShifts(int[] drivers, ArrayList<Shift> shifts)
  {
    int driverIndex = drivers.length - 1;
    for(int index = shifts.size() - 1; index >= 0; index--)
    {
      shifts.get(index).setDriverID(drivers[index]);
      if (DriverInfo.getHoursThisWeek(drivers[driverIndex]) + shifts.get(index).getShiftDuration() < 3000)
        DriverInfo.setHoursThisWeek(drivers[driverIndex],
         DriverInfo.getHoursThisWeek(drivers[driverIndex]) + shifts.get(index).getShiftDuration());
      driverIndex--;
    } // for
  } // addDriversToShifts


  private static void addBusesToShifts(int[] buses, ArrayList<Shift> shifts)
  {
    for(int index = 0; index < shifts.size(); index++)
    {
      shifts.get(index).setBusID(buses[index]);
    } // for
  } // addDriversToShifts


  public static ArrayList<Shift> generateRoster(ArrayList<Shift> shifts, Date date)
  {
    //for (int day = 1; day <= 7; day++)
    //{
      System.out.println("\n\n----------" + date + "----------");

      int[] drivers = getAvailableDrivers(date);
      int[] buses   = getAvailableBuses(date);

      addDriversToShifts(drivers, shifts);
      addBusesToShifts(buses, shifts);

    for (Shift shift : shifts) {
      System.out.println(shift);
    }

      date = new Date();
      date.setDate(date.getDate() + 1);
    //} // for

    return shifts;
  } // generateRoster
}
/*  private Driver driver;
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
*/

/*pseudo code for the roster.... still needs a shift class which stores an array of shifts 
derived from serivces  endTime - startTime

Since we have shifts now, we can assign buses to Shifts, and drivers shifts. 
*/ 



