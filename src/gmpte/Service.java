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

import gmpte.databaseinterface.TimetableInfo;
import gmpte.databaseinterface.database;

public class Service 
{
  private final int serviceId;
  private final int dailyTimetableId;
  
  public Service(int id)
  {
    // open database connection
    database.openBusDatabase();
    serviceId = id;
    dailyTimetableId = TimetableInfo.getDailyTimetableId(id);
  } // Service
  
  public int getServiceId()
  {
    return serviceId;
  } // getServiceId
  
 
  public int getDailyTimetableId()
  {
    return dailyTimetableId;
  } // getDailyTimetableId
}