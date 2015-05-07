/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.helpers;

import java.util.Date;
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
   * Test of getKind method, of class DateHelper.
   */
  @Test
  public void testGetKind() {
    System.out.println("getKind");
    Date time = null;
    int expResult = 0;
    int result = DateHelper.getKind(time);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of isValidDateString method, of class DateHelper.
   */
  @Test
  public void testIsValidDateString() {
    System.out.println("isValidDateString");
    String stringDate = "";
    String dateFormat = "";
    boolean expResult = false;
    boolean result = DateHelper.isValidDateString(stringDate, dateFormat);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDateFromString method, of class DateHelper.
   */
  @Test
  public void testGetDateFromString() throws Exception {
    System.out.println("getDateFromString");
    String stringDate = "";
    String dateFormat = "";
    Date expResult = null;
    Date result = DateHelper.getDateFromString(stringDate, dateFormat);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of afterMidnighMinsToDate method, of class DateHelper.
   */
  @Test
  public void testAfterMidnighMinsToDate() {
    System.out.println("afterMidnighMinsToDate");
    int mins = 0;
    Date expResult = null;
    Date result = DateHelper.afterMidnighMinsToDate(mins);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of formatDateToString method, of class DateHelper.
   */
  @Test
  public void testFormatDateToString() {
    System.out.println("formatDateToString");
    String format = "";
    Date date = null;
    String expResult = "";
    String result = DateHelper.formatDateToString(format, date);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of isToday method, of class DateHelper.
   */
  @Test
  public void testIsToday() {
    System.out.println("isToday");
    Date date = null;
    boolean expResult = false;
    boolean result = DateHelper.isToday(date);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of nextDayMidnight method, of class DateHelper.
   */
  @Test
  public void testNextDayMidnight() {
    System.out.println("nextDayMidnight");
    Date now = null;
    Date expResult = null;
    Date result = DateHelper.nextDayMidnight(now);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
