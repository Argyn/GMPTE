/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.dailytimetable;

import gmpte.db.database;
import gmpte.entities.Service2;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import gmpte.db.TimetableInfo;

/**
 *
 * @author mbax3jw3
 */
public class DelayCancelControllerTest {
  
  public DelayCancelControllerTest() {
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
   * Test of run method, of class DelayCancelController.
   */
  @Test
  public void testAServicesAreDelayedCancelled() throws InterruptedException {
    database.openBusDatabase();
    System.out.println("run");
    DelayCancelController instance = null;
    DailyTimetableController controller;
    TimetableInfo.clearServiceAvail();
    controller = new DailyTimetableController();
    instance = new DelayCancelController(controller.getAllServices());
    instance.testRun();
    // TODO review the generated test code and remove the default call to fail.
    //Thread.sleep(61000);
    instance.interrupt();
    
    assertTrue(TimetableInfo.getNumberOfDelaysCancelations() > 0);
  } 
  
  @Test
  public void testDelayTime() throws InterruptedException {
    database.openBusDatabase();
    System.out.println("run");
    DelayCancelController instance = null;
    DailyTimetableController controller;
    TimetableInfo.clearServiceAvail();
    controller = new DailyTimetableController();
    instance = new DelayCancelController(controller.getAllServices());
    instance.testRun();
    // TODO review the generated test code and remove the default call to fail.
    //Thread.sleep(61000);
    instance.interrupt();
    // check if the delay time is anywhere between 5 - 25 mins, we assume that the
    // delays above this will be cancelled.
    assertTrue(TimetableInfo.getNumberOfDelaysBetween(5, 25) == TimetableInfo.getNumberOfDelays());
  }
  
  @Test
  public void testCancelDelayDate() throws InterruptedException {
    database.openBusDatabase();
    System.out.println("run");
    DelayCancelController instance = null;
    DailyTimetableController controller;
    TimetableInfo.clearServiceAvail();
    controller = new DailyTimetableController();
    instance = new DelayCancelController(controller.getAllServices());
    instance.testRun();
    // TODO review the generated test code and remove the default call to fail.
    //Thread.sleep(61000);
    instance.interrupt();
    assertTrue(TimetableInfo.getNumberOfDelaysCancelationsToday() > 0);
  }
 
    @Test
    public void testCancelDelayRatio() throws InterruptedException {
    database.openBusDatabase();
    System.out.println("run");
    DelayCancelController instance = null;
    DailyTimetableController controller;
    TimetableInfo.clearServiceAvail();
    controller = new DailyTimetableController();
    instance = new DelayCancelController(controller.getAllServices());
    instance.testRun();
    // TODO review the generated test code and remove the default call to fail.
    //Thread.sleep(61000);
    instance.interrupt();
    int delays = TimetableInfo.getNumberOfDelays();
    int cancels = TimetableInfo.getNumberOfCancels();
    assertTrue(delays >= cancels);
  }
  
}
