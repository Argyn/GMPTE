package gmpte.dailytimetable;

import java.util.ArrayList;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author mbax2eu2
 */
public class DailyTimetableController2 
{
  private ArrayList<DailyTimetable> dailyTimetables;
  
  public DailyTimetableController2()
  {
    dailyTimetables = new ArrayList<>();
  }
  
  public void sortDailyTimeTable(Date date, String orderField, String order)
  {
    DailyTimetable timetable = null;
    timetable = getDailyTimeTable(date);
    if (order.equals("asc"))
      timetable.sortAsc(orderField);
    else if (order.equals("des"))
      timetable.sortDes(orderField);
  } // getTimetable
  
  public DailyTimetable getDailyTimeTable(Date date)
  {
    DailyTimetable returnTimetable = null;
    for (DailyTimetable timetable : dailyTimetables)
      if (timetable.getDate().equals(date))
        returnTimetable = timetable;
    return returnTimetable;
  }
  
  public void addDailyTimetable(Date date)
  {
    boolean newDate = true;
    for (DailyTimetable timetable : dailyTimetables)
    {
      if (timetable.getDate().equals(date))
      {
        newDate = false;
      }
    }
    if (newDate)
    {
      System.out.println("IS a new date");
      dailyTimetables.add(new DailyTimetable(date));
    }
    else
      System.out.println("is NOT a new date");
   }
}
