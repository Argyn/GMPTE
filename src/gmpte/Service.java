/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;

/**
 *
 * @author mbax3jw3, mbax2mf2
 */

import gmpte.databaseinterface.TimetableInfo;
import gmpte.databaseinterface.database;

public class Service 
{
  private final int serviceId;
  //private final int dailyTimetableId;
  //private final int routeId;
  //private final int serviceLength;
  private final int startTime;
  private final int endTime;
  private boolean assigned;
  
 /* public Service(int id)
  {
    serviceId = id;
    dailyTimetableId = TimetableInfo.getDailyTimetableId(id);
    routeId = TimetableInfo.getRouteId(dailyTimetableId);
    int[] serviceTimingPoints = TimetableInfo.getServiceTimingPoints(id);
    int[] routePath = TimetableInfo.getRoutePath(routeId);
    if (serviceTimingPoints[0] == routePath[0] &&
      serviceTimingPoints[serviceTimingPoints.length - 1] == routePath[routePath.length - 1])
      serviceLength = TimetableInfo.getRouteLength(id);
    else
      serviceLength = TimetableInfo.getNewLength(routeId);
  } // Service
  */
  public int getServiceId()
  {
    return serviceId;
  } // getServiceId
  
 
 /* public int getDailyTimetableId()
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
  */
  public int getStartTime()
  {
    return startTime;
  } // getStartTime


  public int getEndTime()
  {
    return endTime;
  } // getEndTime


  public boolean isAssigned()
  {
    return assigned;
  } // isAssigned


  public void setAssigned(boolean assignement)
  {
    assigned = assignement;
  } // setAssigned


  Service(int service, int serviceStart, int serviceEnd)
  {
    serviceId = service;
    startTime = serviceStart;
    endTime = serviceEnd;
    assigned = false;
  } // Service


  @Override
  public String toString()
  {
    return "Service: " + serviceId + " | Start Time: " + startTime
             + " | End Time: " + endTime;
  } // toString


  public int compareTo(Service other)
  {
    return this.getStartTime() - other.getStartTime();
  } // compareTo
  /*
}
*/
