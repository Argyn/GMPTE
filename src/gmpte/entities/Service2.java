/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

import java.util.ArrayList;
import java.util.Calendar;
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
  
  public Service2(int id) {
    this.id = id;
    
    busStops = new ArrayList<>();
    times = new ArrayList<>();
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
}
