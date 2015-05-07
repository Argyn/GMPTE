/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gmpte.dailytimetable;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;

/**
 *
 * @author esuysal
 */
public class CausesTest {
  
  public CausesTest() {
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
   * Test of getRandomDelay method, of class Causes.
   */
  @Test
  public void testGetRandomDelay() {
    System.out.println("getRandomDelay");
    Causes instance = new Causes();
    ArrayList<String> expResult = new ArrayList<>();
    expResult.add("a shortage of water for the windscreen wipers");
    expResult.add("a punctured tyre");
    expResult.add("heavy traffic");
    expResult.add("a street parade");
    String result = instance.getRandomDelay();
    assertThat(expResult, hasItem(result));
    fail("Random delay gives invalid delay reason.");
  }

  /**
   * Test of getRandomCancel method, of class Causes.
   */
  @Test
  public void testGetRandomCancel() {
    System.out.println("getRandomCancel");
    Causes instance = new Causes();
    ArrayList<String> expResult = new ArrayList<>();
    expResult.add("a punctured tyre");
    expResult.add("a crash");
    expResult.add("a street parade");
    String result = instance.getRandomCancel();
    assertThat(expResult, hasItem(result));
    // TODO review the generated test code and remove the default call to fail.
    fail("Random cancel gives invalid cancel reason.");
  }
  
}
