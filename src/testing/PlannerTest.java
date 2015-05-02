/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testing;

import gmpte.db.BusStopInfo;
import gmpte.db.database;
import gmpte.entities.BusStop;

/**
 *
 * @author mbgm2rm2
 */
public class PlannerTest {

  public static void main(String[] args) {
    database.openBusDatabase();
    Planner planner = new Planner();
    
    BusStop source = planner.getBusStops().get(2);
    BusStop target = planner.getBusStops().get(17);
    
    System.out.println(source);
    System.out.println(target);
    
    //System.out.println(BusStopInfo.getTimeBetweenBusStops(source, target));*/
    
    //System.out.println(planner.getJourneyPlan(source, target));
    //System.out.println(planner.getBusStops());
    planner.printNetwork();
  }
}
