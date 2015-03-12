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
  private final int routeId;
  private final int serviceLength;
  
  public Service(int id)
  {
    // open database connection
    database.openBusDatabase();
    serviceId = id;
    dailyTimetableId = TimetableInfo.getDailyTimetableId(id);
    routeId = TimetableInfo.getRouteId(dailyTimetableId);
    serviceLength = TimetableInfo.getNewLength(routeId);
  } // Service
  
  public int getServiceId()
  {
    return serviceId;
  } // getServiceId
  
 
  public int getDailyTimetableId()
  {
    return dailyTimetableId;
  } // getDailyTimetableId
  
  public int getRoute()
  {
    return routeId;
  } // getServiceTime
  
  public int getServiceLength()
  {
    return serviceLength;
  } // getServiceTime
}