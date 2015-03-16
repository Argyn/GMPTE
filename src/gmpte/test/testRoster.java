/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.test;
import gmpte.rostering.Roster;
import gmpte.Driver;
import gmpte.Bus;
import gmpte.Service;
import gmpte.databaseinterface.TimetableInfo;
import gmpte.databaseinterface.database;

/**
 *
 * @author mbax3jw3
 */
public class testRoster 
{
  public static void main(String args[])
  {
    database.openBusDatabase();
    Driver driverOne = new Driver(2012, 60167);
    Bus busOne = new Bus(561);
    Service serviceOne = new Service(6177);
    Roster rosterOne = new Roster(driverOne, busOne, serviceOne, 0); 
    TimetableInfo.storeNewRoster(rosterOne);
  }
}
