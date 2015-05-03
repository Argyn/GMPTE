package gmpte.dailytimetable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import gmpte.db.database;
import java.text.SimpleDateFormat;
import java.text.ParseException;



/**
 *
 * @author mbax3jw3
 */
public class testController 
{
  public static void main(String args[])  throws ParseException
  {
    database.openBusDatabase();
    DailyTimetableController2 controller = new DailyTimetableController2();
    // adding new dates
    controller.addDailyTimetable(new SimpleDateFormat("dd/M/yyyy").parse("23/04/2015"));
    controller.addDailyTimetable(new SimpleDateFormat("dd/M/yyyy").parse("24/04/2015"));
    controller.addDailyTimetable(new SimpleDateFormat("dd/M/yyyy").parse("23/05/2015"));
    controller.addDailyTimetable(new SimpleDateFormat("dd/M/yyyy").parse("23/04/2014"));
    // trying to add an existing date
    controller.addDailyTimetable(new SimpleDateFormat("dd/M/yyyy").parse("23/04/2015"));
    // print a list of services on that date
    System.out.println(controller.getDailyTimeTable(new SimpleDateFormat("dd/M/yyyy").parse("23/04/2015")).getServices());
  }
}
