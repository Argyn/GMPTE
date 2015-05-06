/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.passenger;

import gmpte.db.database;
import gmpte.entities.Area;
import gmpte.entities.BusStop;
import gmpte.entities.Path;
import gmpte.entities.Route;
import java.util.ArrayList;
import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author mbgm2rm2
 */
public class JourneyPlannerControllerTest {
  
  public JourneyPlannerControllerTest() {
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
   * Test of buildNetwork method, of class JourneyPlannerController.
   */
  @Test
  public void testBuildNetwork() {
    System.out.println("buildNetwork");
    JourneyPlannerController instance = new JourneyPlannerController();
    Graph expResult = null;
    Graph result = instance.buildNetwork(instance.getRoutes());
    int expNumberOfVertices = 22;
    int expNumberOfEdges = 42;
    int numberOfVertices = instance.getNetwork().getVertices().size();
    int numberOfEdges = instance.getNetwork().getEdges().size();
    
    // assert equal number of vertices
    assertEquals(numberOfVertices, expNumberOfVertices);
    // asser equal number of edges
    assertEquals(numberOfEdges, expNumberOfEdges);

  }
  
  

  
  
}
