/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;

import gmpte.databaseinterface.TimetableInfo;
/**
 *
 * @author mbax3jw3
 */


public class Service
{
  private final int serviceId;
  private final int dailyTimetableId;
  private final int routeId;
  private final int serviceLengthMinutes;
  private final int serviceLengthHours;
 
  
  public Service(int id)
  {
    serviceId = id;
    dailyTimetableId = TimetableInfo.getDailyTimetableId(id);
    routeId = TimetableInfo.getRouteId(dailyTimetableId);
    int[] serviceTimingPoints = TimetableInfo.getServiceTimingPoints(id);
    int[] routePath = TimetableInfo.getRoutePath(routeId);
    if (serviceTimingPoints[0] == routePath[0] &&
      serviceTimingPoints[serviceTimingPoints.length - 1] == routePath[routePath.length - 1])
      serviceLengthMinutes = TimetableInfo.getRouteLength(id);
    else
      serviceLengthMinutes = TimetableInfo.getNewLength(routeId);
    
    serviceLengthHours = (int)Math.ceil((double) serviceLengthMinutes / 60);
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
  
  public int getServiceLengthMinutes()
  {
    return serviceLengthMinutes;
  } // getServiceTime
  
  
  public int getServiceLengthHours() {
      return serviceLengthHours;
  }
  
  public String toString() {
      return "Service ID:"+serviceId+", DailyTimetableID: "+dailyTimetableId+", Route: "+routeId;
  }
}