/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.db;

import gmpte.entities.BusStop;
import gmpte.entities.Route;
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
public class RouteDBInfoTest {
  
  public RouteDBInfoTest() {
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
   * Test of doesRouteGoFromTo method, of class RouteDBInfo.
   */
  @Test
  public void testDoesRouteGoFromTo() {
    System.out.println("doesRouteGoFromTo");
    BusStop from = BusStopInfo.getBusStopsById(770);
    from.addId(770);
    BusStop to = BusStopInfo.getBusStopsById(771);
    to.addId(771);
    Route route = new Route(65);
    boolean result = RouteDBInfo.doesRouteGoFromTo(from, to, route);
    assertTrue(result);
  }
  
}
