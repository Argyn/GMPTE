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
}
