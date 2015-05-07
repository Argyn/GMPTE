/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.helpers;

import gmpte.GMPTEConstants;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mbgm2rm2
 */
public class DateHelperTest {
  
  public DateHelperTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of isValidDateString method, of class DateHelper.
   */
  @Test
  public void testIsValidDateString() {
    System.out.println("isValidDateString");
    String stringDate = "22:40:52";
    String dateFormat = "HH:mm:ss";
    boolean expResult = true;
    boolean result = DateHelper.isValidDateString(stringDate, dateFormat);
    assertEquals(expResult, result);
  }


  /**
   * Test of afterMidnighMinsToDate method, of class DateHelper.
   */
  @Test
  public void testAfterMidnighMinsToDate() {
    System.out.println("afterMidnighMinsToDate");
    int mins = 1200;
    
    // boarding time is number of minutes since midnigth
    Calendar date = new GregorianCalendar();
    // reset hour, minutes, seconds and millis
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    
    date.set(Calendar.MINUTE, mins);
   
    
    Date expResult = date.getTime();
    Date result = DateHelper.afterMidnighMinsToDate(mins);
    assertEquals(expResult, result);
  }

  /**
   * Test of formatDateToString method, of class DateHelper.
   */
  @Test
  public void testFormatDateToString() {
    System.out.println("formatDateToString");

    Date todayDate = new Date();
    DateFormat format = new SimpleDateFormat(GMPTEConstants.DATE_FORMAT);
    String expResult = format.format(todayDate);
    
    String result = DateHelper.formatDateToString(GMPTEConstants.DATE_FORMAT, todayDate);
    assertEquals(expResult, result);
  }

  /**
   * Test of isToday method, of class DateHelper.
   */
  @Test
  public void testIsToday() {
    System.out.println("isToday");
    Date date = new Date();
    boolean expResult = true;
    boolean result = DateHelper.isToday(date);
    assertEquals(expResult, result);
  }

  /**
   * Test of nextDayMidnight method, of class DateHelper.
   */
  @Test
  public void testNextDayMidnight() {
    System.out.println("nextDayMidnight");
    Date now = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(now);
    
    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    Date expResult = calendar.getTime();
   
    Date result = DateHelper.nextDayMidnight(now);
    assertEquals(expResult, result);
  }
  
}
