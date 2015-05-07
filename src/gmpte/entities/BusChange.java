/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 *
 * @author mbgm2rm2
 */
public class BusChange {
  
  private Route route;
  private int service;
  
  private Date boardingTime;
  private Date disembarkTime;
  
  private Date date;
  
  private LinkedList<BusStop> path;
  
  public BusChange(Route route) {
    this.route = route;
    path = new LinkedList<>();
  }
  
  public void addBusStation(BusStop bStop) {
    path.add(bStop);
  }
  
  public LinkedList<BusStop> getPath() {
    return path;
  }

  public Route getRoute() {
    return route;
  }

  public int getService() {
    return service;
  }
  
  public void setBoardingTime(Date boardingTime) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(boardingTime);
    Calendar calendar2 = Calendar.getInstance();
    calendar2.setTime(date);
    
    calendar2.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
    calendar2.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
    
    this.boardingTime = calendar2.getTime();
  }
  
  public void setBoardingTime(int boardingTime) {
    // boarding time is number of minutes since midnigth
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    // reset hour, minutes, seconds and millis
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    
    calendar.set(Calendar.MINUTE, boardingTime);
    
    setBoardingTime(calendar.getTime());
  }
  
  public void setDisembarkTime(Date time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(time);
    Calendar calendar2 = Calendar.getInstance();
    calendar2.setTime(date);
    
    calendar2.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
    calendar2.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
    
    this.disembarkTime = calendar2.getTime();
    
    System.out.println(disembarkTime);
  }
  
  public void setDisembarkTime(int time) {
    // boarding time is number of minutes since midnigth
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    // reset hour, minutes, seconds and millis
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    
    calendar.set(Calendar.MINUTE, time);
    
    setDisembarkTime(calendar.getTime());
  }
  
  public Date getBoardingTime() {
    return boardingTime;
  }
  
  public Date getDisembarkTime() {
    return disembarkTime;
  }
  
  public void setDate(Date date) {
    System.out.println("Change date = "+date);
    this.date = date;
  }
  public void setBusStopTimes(Service2 service) {
    service.updateBoardingDisembarkTimes(this, path.peekFirst(), path.peekLast());
  }
  
}
