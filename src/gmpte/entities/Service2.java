/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author mbgm2rm2
 */
public class Service2 {
  
  private int id;
  private ArrayList<BusStop> busStops;
  private ArrayList<Date> times;
  private ArrayList<Date> afterDelayTimes;
  private boolean delayed;
  private int delayTime;
  private boolean cancelled;
  private String delayCancelReason;
  private Route route;
  private int delayPoint;
  
  public Service2(Route route, int id) {
    this.id = id;
    this.route = route;
    
    busStops = new ArrayList<>();
    
    times = new ArrayList<>();
    
    delayed = false;
    
    cancelled = false;
  }
  
  public void addTimingPoint(BusStop stop, Date time) {
    busStops.add(stop);
    times.add(time);
  } 
  
  public void addTimingPoint(BusStop stop, int minutes) {
    // boarding time is number of minutes since midnigth
    Calendar date = new GregorianCalendar();
    // reset hour, minutes, seconds and millis
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    
    date.set(Calendar.MINUTE, minutes);
    
    addTimingPoint(stop, date.getTime());
  }
  
  public void updateBoardingDisembarkTimes(BusChange change, BusStop source, BusStop target) {
    Date boardingTime = null;
    Date disembardkTime = null;
    
    int index = 0;
    for(BusStop bStop : busStops) {
      if(boardingTime==null && bStop.equals(source))
        boardingTime = times.get(index);
      else if(boardingTime!=null && bStop.equals(target)) {
        disembardkTime = times.get(index);
        break;
      }
      
      index++;
    }
    
    change.setBoardingTime(boardingTime);
    change.setDisembarkTime(disembardkTime);
  }
  
  public ArrayList<BusStop> getBusStops() {
    return busStops;
  }
  
  public ArrayList<Date> getTimes() {
    return times;
  }
  
  public boolean doesTerminateAtTime(Date time) {
    return times.indexOf(time)==(times.size()-1);
  }
  
  public void setDelayedTime(int time, int delayPoint, String reason) {
    delayTime = time;
    delayed = true;
    delayCancelReason= reason;
    this.delayPoint = delayPoint;
    
    
    
    Calendar calendar = Calendar.getInstance();
    
    int index = 0;
    for(Date date : times) {
      calendar.setTime(date);
      
      if(busStops.get(index).getIds().get(0)>=delayPoint)
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+time);
      
      times.set(index, calendar.getTime());
      index++;
    }
  }
  
  public void setCancelled(String reason) {
    cancelled = true;
    delayCancelReason = reason;
  }
  
  public boolean isDelayed() {
    return delayed;
  }
  
  public boolean isCancelled() {
    return cancelled;
  }
  
  public int getId() {
    return id;
  }
  
  public int getDelayTime() {
    return delayTime;
  }
  
  public String getReason() {
    return delayCancelReason;
  }
  
  public Route getRoute() {
    return route;
  }
  
  public boolean equals(Object other) {
    Service2 otherService = (Service2)other;
    return id==otherService.id;
  }
  
  public int getDelayPoint() {
    return delayPoint;
  }
}
