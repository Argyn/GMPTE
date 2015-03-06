/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.rostering;

import gmpte.Driver;

/**
 *
 * @author mbgm2rm2
 */
public class RosterController {
  
  public RosterGenerationResponse generateRoster() 
  {
    Driver[] driverList = new Driver[5]
    return new RosterGenerationResponse();
  }
  
  public Roster getRosterForDriver(Driver driver) {
    return new Roster();
  }
  
  public Roster getRostersForAllDrivers() {
    return new Roster();
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
  
}
