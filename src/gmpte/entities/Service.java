/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

import gmpte.db.TimetableInfo;
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
  // start of the shift inc the time taken to get to the start point eg route 67
  private int startTime;
  // end of the shift inc the time taken to get to the end point eg route 67
  private int endTime;
  
  public Service(int id)
  {
    serviceId = id;
    dailyTimetableId = TimetableInfo.getDailyTimetableId(id);
    routeId = TimetableInfo.getRouteId(dailyTimetableId);
    TimetableInfo.getStartEndTimes(this);
    serviceLengthMinutes =  endTime - startTime;  
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
  
  public int getStartTime()
  {
      return startTime;
  }
  
  public int getEndTime()
  {
      return endTime;
  }
  
  public void setStartTime(int startTime)
  {
    this.startTime = startTime;
  }
  
  public void setEndTime(int endTime)
  {
    this.endTime = endTime;
  }
  
  public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Service ID:"+serviceId);
      builder.append("\n");
      builder.append("DailyTimeTableID:"+dailyTimetableId);
      builder.append("\n");
      builder.append("Route:"+routeId);
      builder.append("\n");
      builder.append("Length(min):"+serviceLengthMinutes);
      builder.append("\n");
      builder.append("Length(hrs):"+serviceLengthHours);
      builder.append("\n");
      builder.append("Start Time:"+startTime);
      builder.append("\n");
      builder.append("End Time:"+endTime);
      builder.append("\n");
      return builder.toString();
  }
}