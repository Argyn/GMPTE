/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.rostering;
import gmpte.databaseinterface.DriverInfo;
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
  
  public Roster(Driver newDriver, Bus newBus, Service newService, int weekDay)
  {
    driver = newDriver;
    bus = newBus;
    service = newService;
    day = weekDay;
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
}
