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
import java.util.Iterator;
import java.util.Comparator;


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
  
  private boolean delayed;
  
  private String delayReason;
   
  private boolean cancelled;
  
  private String cancelReason;

  private int delayTime; 
  
  public Service(int id)
  {
    serviceId = id;
    dailyTimetableId = TimetableInfo.getDailyTimetableId(id);
    routeId = TimetableInfo.getRouteId(dailyTimetableId);
    
    serviceTimingPoints = new ArrayList<>();
    int[] timingPoints = TimetableInfo.getRoutePath(routeId);
    for (int index = 0; index < timingPoints.length; index++)
      serviceTimingPoints.add(timingPoints[index]);
    
    try {
      TimetableInfo.getStartEndTimes(this);
    } catch (SQLException ex) {
      Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    if(startTime > endTime)
      endTime+=1440;
    
    serviceLength =  endTime - startTime;  
    
    serviceTimes = TimetableInfo.getServiceTimes(serviceId, serviceTimingPoints,startTime,endTime);

    
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
    
    delayed      = false;
    cancelled    = false;
    cancelReason = "";
    delayReason  = "";
    delayTime    = 0;
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

    delayed      = false;
    cancelled    = false;
    cancelReason = "";
    delayReason  = "";
    delayTime    = 0;
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
  
  public ArrayList<Integer> getServiceTimes()
  {
    return serviceTimes;
  } // getServiceTimes
  
  public ArrayList<Integer> getServiceTimingPoints()
  {
    return serviceTimingPoints;
  } // getServiceTimingPoints

  public int getDelayTime(){
    return delayTime;
  }
  
  public boolean isDelayed()
  {
    return delayed;
  }
  
  public String getDelayMessage()
  {
    return delayReason;
  }
   
  public boolean isCancelled()
  {
    return cancelled;
  }
  
  public String getCancelMessage()
  {
    return cancelReason;
  }
  
  public void introduceDelay(int delayPoint, int delay, String cause)
  {
    int delayStart = serviceTimes.get(delayPoint);
    int index = 0;
    for (Integer serviceTime : serviceTimes) 
    {
      if (serviceTime >= delayStart)
        serviceTimes.set(index, (serviceTime + delay));
      index++;
    }
    delayed = true;
    delayTime = delay;
    StringBuilder builder = new StringBuilder();
    builder.append("The "+serviceId+" service");
    builder.append(" is delayed by approximately "+delay+" minutes due to a ");
    builder.append(cause);
    builder.append(" and will arrive at bus station at approximately "+serviceTimes.get(serviceTimes.size() - 1));
    builder.append(".We apologize for the delay in your journey.");
    delayReason = builder.toString();
  }
  
  public void cancel(String cause)
  {
    cancelled = true;  
    StringBuilder builder = new StringBuilder();
    builder.append("The "+serviceId+" service");
    builder.append(" has been cancelled due to a ");
    builder.append(cause);
    builder.append(".We apologize for any inconvenience this causes.");
    cancelReason = builder.toString();
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
  
 public static Comparator<Service> idCompDes = new Comparator<Service>() {
 
        @Override
	    public int compare(Service serviceOne, Service serviceTwo) {
 
	      int idOne = serviceOne.getServiceId();
	      int idTwo = serviceTwo.getServiceId();
          return compareIds(idTwo, idOne);
	    }
 
	};
 
 public static Comparator<Service> timetableCompDes = new Comparator<Service>() {
 
        @Override
	    public int compare(Service serviceOne, Service serviceTwo) {
	      int idOne = serviceOne.getDailyTimetableId();
	      int idTwo = serviceTwo.getDailyTimetableId();
          return compareIds(idTwo, idOne);
	    }
 
	};
 
 public static Comparator<Service> routeCompDes = new Comparator<Service>() {
 
        @Override
	    public int compare(Service serviceOne, Service serviceTwo) {
 
	      int idOne = serviceOne.getRoute();
	      int idTwo = serviceTwo.getRoute();
          return compareIds(idTwo, idOne);
	    }
 
	};
 
 public static Comparator<Service> lengthCompDes = new Comparator<Service>() {
 
        @Override
	    public int compare(Service serviceOne, Service serviceTwo) {
 
	      int idOne = serviceOne.getServiceLength();
	      int idTwo = serviceTwo.getServiceLength();
          return compareIds(idTwo, idOne);
	    }
 
	};
 
 public static Comparator<Service> startTimeCompDes = new Comparator<Service>() {
 
        @Override
	    public int compare(Service serviceOne, Service serviceTwo) {
 
	      int idOne = serviceOne.getStartTime();
	      int idTwo = serviceTwo.getStartTime();
          return compareIds(idTwo, idOne);
	    }
 
	};
 
 public static Comparator<Service> endTimeCompDes = new Comparator<Service>() {
 
        @Override
	    public int compare(Service serviceOne, Service serviceTwo) {
 
	      int idOne = serviceOne.getEndTime();
	      int idTwo = serviceTwo.getEndTime();
          return compareIds(idTwo, idOne);
	    }
 
	};
 
 public static Comparator<Service> startDateCompDes = new Comparator<Service>() {
 
        @Override
	    public int compare(Service serviceOne, Service serviceTwo) {
 
	      Date dateOne = serviceOne.getStartTimeDate();
	      Date  dateTwo = serviceTwo.getStartTimeDate();
          return dateTwo.compareTo(dateOne);
	    }
 
	};
 
 public static Comparator<Service> endDateCompDes = new Comparator<Service>() {
 
        @Override
	    public int compare(Service serviceOne, Service serviceTwo) {
 
	      Date dateOne = serviceOne.getEndTimeDate();
	      Date  dateTwo = serviceTwo.getEndTimeDate();
          return dateTwo.compareTo(dateOne);
	    }
 
	};
 
  public static Comparator<Service> idCompAsc = new Comparator<Service>() {
 
        @Override
	    public int compare(Service serviceOne, Service serviceTwo) {
 
	      int idOne = serviceOne.getServiceId();
	      int idTwo = serviceTwo.getServiceId();
          return compareIds(idOne, idTwo);
	    }
 
	};
  
 public static Comparator<Service> timetableCompAsc = new Comparator<Service>() {
 
        @Override
	    public int compare(Service serviceOne, Service serviceTwo) {
 
	      int idOne = serviceOne.getDailyTimetableId();
	      int idTwo = serviceTwo.getDailyTimetableId();
          return compareIds(idOne, idTwo);
	    }
 
	};
 
 public static Comparator<Service> routeCompAsc = new Comparator<Service>() {
 
        @Override
	    public int compare(Service serviceOne, Service serviceTwo) {
 
	      int idOne = serviceOne.getRoute();
	      int idTwo = serviceTwo.getRoute();
          return compareIds(idOne, idTwo);
	    }
 
	};
 
 public static Comparator<Service> lengthCompAsc = new Comparator<Service>() {
 
        @Override
	    public int compare(Service serviceOne, Service serviceTwo) {
 
	      int idOne = serviceOne.getServiceLength();
	      int idTwo = serviceTwo.getServiceLength();
          return compareIds(idOne, idTwo);
	    }
 
	};
 
 public static Comparator<Service> startTimeCompAsc = new Comparator<Service>() {
 
        @Override
	    public int compare(Service serviceOne, Service serviceTwo) {
 
	      int idOne = serviceOne.getStartTime();
	      int idTwo = serviceTwo.getStartTime();
          return compareIds(idOne, idTwo);
	    }
 
	};
 
 public static Comparator<Service> endTimeCompAsc = new Comparator<Service>() {
 
        @Override
	    public int compare(Service serviceOne, Service serviceTwo) {
 
	      int idOne = serviceOne.getEndTime();
	      int idTwo = serviceTwo.getEndTime();
          return compareIds(idOne, idTwo);
	    }
 
	};
 
 public static Comparator<Service> startDateCompAsc = new Comparator<Service>() {
 
        @Override
	    public int compare(Service serviceOne, Service serviceTwo) {
 
	      Date dateOne = serviceOne.getStartTimeDate();
	      Date  dateTwo = serviceTwo.getStartTimeDate();
          return dateOne.compareTo(dateTwo);
	    }
 
	};
 
 public static Comparator<Service> endDateCompAsc = new Comparator<Service>() {
 
        @Override
	    public int compare(Service serviceOne, Service serviceTwo) {
 
	      Date dateOne = serviceOne.getEndTimeDate();
	      Date  dateTwo = serviceTwo.getEndTimeDate();
          return dateOne.compareTo(dateTwo);
	    }
 
	};
  
  public static int compareIds(int idOne, int idTwo)
  {
    if (idOne < idTwo)
      return -1;
    else if (idOne > idTwo)
      return 1;
    else
      return 0;
  }
}
