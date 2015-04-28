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
public class DailyTimetableController 
{
  private ArrayList<DailyTimetable> dailyTimetables;
  
  public DailyTimetableController()
  {
    dailyTimetables = new ArrayList<>();
  }
  
  public DailyTimetable getTimetable(Date date)
  {
    boolean newDate = true;
    DailyTimetable returnTimetable = null;
    for (DailyTimetable timetable : dailyTimetables)
    {
      if (timetable.getDate().equals(date))
      {
        newDate = false;
        returnTimetable = timetable;
      }
    }
    if (newDate)
    {
      System.out.println("IS a new date");
      returnTimetable = new DailyTimetable(date);
      dailyTimetables.add(returnTimetable);
    }
    else
      System.out.println("is NOT a new date");

    return returnTimetable; 
  } // getTimetable
}
