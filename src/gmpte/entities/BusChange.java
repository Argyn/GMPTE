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
    this.boardingTime = boardingTime;
  }
  
  public void setBoardingTime(int boardingTime) {
    System.out.println("Boarding time="+boardingTime);
    // boarding time is number of minutes since midnigth
    Calendar date = new GregorianCalendar();
    // reset hour, minutes, seconds and millis
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    
    date.set(Calendar.MINUTE, boardingTime);
    
    setBoardingTime(date.getTime());
  }
  
  public void setDisembarkTime(Date time) {
    this.disembarkTime = time;
  }
  
  public void setDisembarkTime(int time) {
    // boarding time is number of minutes since midnigth
    Calendar date = new GregorianCalendar();
    // reset hour, minutes, seconds and millis
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    
    date.set(Calendar.MINUTE, time);
    
    setDisembarkTime(date.getTime());
  }
  
  public Date getBoardingTime() {
    return boardingTime;
  }
  
  public Date getDisembarkTime() {
    return disembarkTime;
  }
  
  public void setBusStopTimes(Service2 service) {
    service.updateBoardingDisembarkTimes(this, path.peekFirst(), path.peekLast());
  }
  
}
