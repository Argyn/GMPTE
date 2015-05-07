/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author mbgm2rm2
 */
public class DateHelper {
  
  public static int getKind(Date time) {
    int kind = 0;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(time);
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    if(dayOfWeek==7)
      kind = 1;
    else if(dayOfWeek==1)
      kind = 2;
    
    return kind;
  }
  
  public static boolean isValidDateString(String stringDate, String dateFormat) {
    SimpleDateFormat format = new SimpleDateFormat(dateFormat);
    // making more strict
    format.setLenient(false);
    // try to convert it to Date, if successfull
    try {
        // means dateString corresponds to an actual Date
        Date date = format.parse(stringDate);
        return true;
    } catch(ParseException exception) {
        return false;
    }   
  }
  
  public static Date getDateFromString(String stringDate, String dateFormat) 
                                                      throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat(dateFormat);
    format.setLenient(false);

    Date date = format.parse(stringDate);

    return date;
  }
  
  public static Date afterMidnighMinsToDate(int mins) {
    // boarding time is number of minutes since midnigth
    Calendar date = new GregorianCalendar();
    // reset hour, minutes, seconds and millis
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    
    date.set(Calendar.MINUTE, mins);
    
    return date.getTime();
  }
  
  public static String formatDateToString(String format, Date date) {
    DateFormat dateFormat = new SimpleDateFormat(format);
    return dateFormat.format(date);
  }
  
  public static boolean isToday(Date date) {
    System.out.println(date);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);
    
    calendar.setTime(new Date());
    int todayDay = calendar.get(Calendar.DAY_OF_MONTH);
    int todayMonth = calendar.get(Calendar.MONTH);
    int todayYear = calendar.get(Calendar.YEAR);
    
    return day==todayDay && month==todayMonth && year==todayYear;
  }
  
  public static Date nextDayMidnight(Date now) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(now);
    
    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    
    return calendar.getTime();
  }
}
