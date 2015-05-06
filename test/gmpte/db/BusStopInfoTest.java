/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.db;

import gmpte.entities.Area;
import gmpte.entities.BusStop;
import gmpte.entities.Route;
import java.sql.PreparedStatement;
import java.util.ArrayList;
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
public class BusStopInfoTest {
  
  public BusStopInfoTest() {
    database.openBusDatabase();
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
   * Test of getBusStopsById method, of class BusStopInfo.
   */
  @Test
  public void testGetBusStopsById() {
    System.out.println("getBusStopsById");
    int busStopId = 770;
    
    BusStop expResult = new BusStop(new Area(209, "Stockport"), "Bus Station");
    BusStop result = BusStopInfo.getBusStopsById(busStopId);
    
    System.out.println(result.equals(expResult));
    System.out.println(result);
    assertEquals("", expResult, result);
  }
  
}
