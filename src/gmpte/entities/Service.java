/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

import gmpte.db.TimetableInfo;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author mbax3jw3
 */
import java.util.ArrayList;


public class Service implements Comparable<Service>
{
  private final int serviceId;
  private final int dailyTimetableId;
  private final int routeId;
  private final int serviceLength;
  // start of the shift inc the time taken to get to the start point eg route 67
  private int startTime;
  // end of the shift inc the time taken to get to the end point eg route 67
  private int endTime;
  
  private Date startTimeDate;
  
  private Date endTimeDate;
  
  private ArrayList<Integer> serviceTimes;
  
  private ArrayList<Integer> serviceTimingPoints;
  
  public Service(int id)
  {
    serviceId = id;
    dailyTimetableId = TimetableInfo.getDailyTimetableId(id);
    routeId = TimetableInfo.getRouteId(dailyTimetableId);
    
    serviceTimingPoints = new ArrayList<>();
    int[] timingPoints = TimetableInfo.getRoutePath(routeId);
    for (int index = 0; index < timingPoints.length; index++)
      serviceTimingPoints.add(timingPoints[index]);
    
    serviceTimes = TimetableInfo.getServiceTimes(serviceId, serviceTimingPoints);

    try {
      TimetableInfo.getStartEndTimes(this);
    } catch (SQLException ex) {
      Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    if(startTime > endTime)
      endTime+=1440;
    
    serviceLength =  endTime - startTime;  
    
    Calendar date = new GregorianCalendar();
    // reset hour, minutes, seconds and millis
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    
    date.add(Calendar.MINUTE, endTime);
    endTimeDate = date.getTime();
    
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    
    date.add(Calendar.MINUTE, startTime);
    startTimeDate = date.getTime();

  }  
  public Service(int serviceID, int dailyTimeTableID, int routeID) {
    this.serviceId = serviceID;
    this.dailyTimetableId = dailyTimeTableID;
    this.routeId = routeID;
    
    try {
      TimetableInfo.getStartEndTimes(this);
    } catch (SQLException ex) {
      Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    if(startTime > endTime)
      endTime+=1440;
    
    serviceLength =  endTime - startTime;  
    
    Calendar date = new GregorianCalendar();
    // reset hour, minutes, seconds and millis
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    
    date.add(Calendar.MINUTE, endTime);
    endTimeDate = date.getTime();
    
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    
    date.add(Calendar.MINUTE, startTime);
    startTimeDate = date.getTime();
  }
  
  
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
  
  public Date getStartTimeDate() {
    return startTimeDate;
  }
  
  public Date getEndTimeDate() {
    return endTimeDate;
  }
  
  @Override
  public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Service ID:"+serviceId);
      builder.append("\n");
      builder.append("DailyTimeTableID:"+dailyTimetableId);
      builder.append("\n");
      builder.append("Route:"+routeId);
      builder.append("\n");
      builder.append("Start Time:"+startTime);
      builder.append("\n");
      builder.append("End Time:"+endTime);
      builder.append("\n");
      builder.append("\n");
      builder.append("\n");
      builder.append("\n");
      builder.append(serviceTimingPoints);
      builder.append("\n");
      builder.append(serviceTimes);
      return builder.toString();
      //return Integer.toString(serviceId);
  }
  
  @Override
  public int compareTo(Service other) {
    return 0;
  }
}
