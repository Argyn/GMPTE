/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import gmpte.entities.Service;
import java.util.Date;

/**
 *
 * @author mbax2eu2
 */
public class DailyTimetableNode {
  
  private Service service;
  private int time;
  private int timingPoint;
  
  public DailyTimetableNode(Service newService, int newTime, int newTimingPoint)
  {
    service = newService;
    time    = newTime;
    timingPoint = newTimingPoint;
  } // DailyTimetable

  public Service getService()
  {
    return service;
  } // getService
      
  public int getTime()
  {
    return time;
  } //getTime
  
  public int getTimingPoint(){
    return timingPoint;
  }
  
}
