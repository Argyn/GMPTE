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

/**
 *
 * @author mbgm2rm2
 */
@RunWith(Parameterized.class)
public class JourneyPlannerControllerTest {
  
  private BusStop from;
  private BusStop to;
  private int expRes;
  
  public JourneyPlannerControllerTest(BusStop from, BusStop to, int expRes){
    database.openBusDatabase();
    this.from = from;
    this.to = to;
    this.expRes = expRes;
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
  @Parameters
   public static Collection<Object[]> generateData(){
    
    return Arrays.asList(new Object[][]{
      {new BusStop(new Area(209, "Stockport"), "Bus Station"),
        new BusStop(new Area(221, "Glossop"), "Henry Street"),
        0},
      {new BusStop(new Area(210, "Romiley"), "Train Station"),
        new BusStop(new Area(212, "Marple"), "Offerton Fold"),
        1},
      {new BusStop(new Area(212, "Marple"), "Navigation Hotel"),
        new BusStop(new Area(221, "Glossop"), "Grouse Inn"),
        1},
      {new BusStop(new Area(212, "Marple"), "Offerton Fold"),
        new BusStop(new Area(212, "Marple"), "Back of Beyond"),
        1},
      {new BusStop(new Area(212, "Birch Vale"), "Grouse Hotel"),
        new BusStop(new Area(209, "Stockport"), "Intermediate Road"),
        1},
      {new BusStop(new Area(210, "Romiley"), "Corcoran Drive"),
        new BusStop(new Area(212, "Marple"), "Norfolk Arms"),
        0},
      
    });
  }
  /**
   * Test of getJourneyPlan method, of class JourneyPlannerController.
   */
  @Test
  public void testGetJourneyPlan() {
    System.out.println("getJourneyPlan");
    JourneyPlannerController instance = new JourneyPlannerController();
    Path result = instance.getJourneyPlan(this.from, this.to);
    assertEquals(result.getFullPath().size(), this.expRes);
    // TODO review the generated test code and remove the default call to fail.
    fail("The given journey is wrong.");
  }
}
