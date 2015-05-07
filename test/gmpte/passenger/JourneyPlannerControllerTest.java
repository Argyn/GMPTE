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
import java.util.Collection;
import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author mbgm2rm2
 */
public class JourneyPlannerControllerTest {

  
  public JourneyPlannerControllerTest(){
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
  /**
   * Test of getJourneyPlan method, of class JourneyPlannerController.
   */
  @Test
  public void testGetJourneyPlan() {
    System.out.println("getJourneyPlan");
    JourneyPlannerController instance = new JourneyPlannerController();
    
    BusStop from = new BusStop(new Area(209, "Stockport"), "Bus Station");
    BusStop to = new BusStop(new Area(221, "Glossop"), "Henry Street");
    System.out.println("Testing between " + from + " to " + to);
    Path result = instance.getJourneyPlan(from, to, new Date());
    assertEquals(result.getFullPath().size(), 1);
    
    from = new BusStop(new Area(210, "Romiley"), "Train Station");
    to = new BusStop(new Area(212, "Marple"), "Offerton Fold");
    System.out.println("Testing between " + from + " to " + to);
    result = instance.getJourneyPlan(from, to, new Date());
    assertEquals(result.getFullPath().size(), 2);
    
    from = new BusStop(new Area(212, "Marple"), "Navigation Hotel");
    to = new BusStop(new Area(221, "Glossop"), "Grouse Inn");
    System.out.println("Testing between " + from + " to " + to);
    result = instance.getJourneyPlan(from, to, new Date());
    assertEquals(result.getFullPath().size(), 1);
    
    from = new BusStop(new Area(212, "Marple"), "Offerton Fold");
    to = new BusStop(new Area(212, "Marple"), "Back of Beyond");
    System.out.println("Testing between " + from + " to " + to);
    result = instance.getJourneyPlan(from, to, new Date());
    assertEquals(result.getFullPath().size(), 2);
    
    from = new BusStop(new Area(216, "Birch Vale"), "Grouse Hotel");
    to = new BusStop(new Area(209, "Stockport"), "Intermediate Road");
    System.out.println("Testing between " + from + " to " + to);
    result = instance.getJourneyPlan(from, to, new Date());
    assertEquals(result.getFullPath().size(), 2);
    
    from = new BusStop(new Area(210, "Romiley"), "Corcoran Drive");
    to = new BusStop(new Area(212, "Marple"), "Norfolk Arms");
    System.out.println("Testing between " + from + " to " + to);
    result = instance.getJourneyPlan(from, to, new Date());
    assertEquals(result.getFullPath().size(), 1); 
  }
}
