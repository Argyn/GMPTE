/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.db;

import gmpte.entities.BusStop;
import gmpte.entities.Route;
import gmpte.entities.Service;
import gmpte.entities.Service2;
import java.util.ArrayList;
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
public class ServiceDBTest {
  
  public ServiceDBTest() {
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
   * Test of getServiceById method, of class ServiceDB.
   */
  @Test
  public void testGetServiceById() {
    System.out.println("getServiceById");
    Route route = null;
    int serviceID = 6177;
    Service2 expResult = new Service2(new Route(65), 6177);
    Service2 result = ServiceDB.getServiceById(route, serviceID);
    assertEquals(expResult, result);
  }
}
