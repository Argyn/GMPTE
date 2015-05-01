package gmpte.dailytimetable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import gmpte.dailytimetable.DailyTimetable;
import gmpte.db.database;
import gmpte.entities.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



/**
 *
 * @author mbax3jw3
 */
public class testDailyTimetable 
{
  public static void main(String args[])  throws ParseException
  {
    database.openBusDatabase();
    // create a new timetable with a weekday
    DailyTimetable timetable = new DailyTimetable(new SimpleDateFormat("dd/M/yyyy").parse("23/04/2015"));
    // print the services
    System.out.println(timetable.getServices());
    // sort services ascending by ...
    timetable.sortAsc("endDate");
    // sort services descending by ...
    timetable.sortDes("endDate");  
  }
}
