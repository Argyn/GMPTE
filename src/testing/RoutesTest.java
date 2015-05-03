/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testing;

import gmpte.db.BusStopInfo;
import gmpte.db.RouteDBInfo;
import gmpte.db.database;
import gmpte.entities.BusStop;
import gmpte.entities.Route;
import java.util.ArrayList;

/**
 *
 * @author mbgm2rm2
 */
public class RoutesTest {
  public static void main(String[] args) {
    database.openBusDatabase();
    
    ArrayList<Route> routes = RouteDBInfo.getAllRoutes();
    
    for(Route route : routes) {
      ArrayList<BusStop> busStops = BusStopInfo.getBusStopsByRoute(route);
      
      System.out.println("=== Route "+route.getRouteID()+"("+route.toString()+") ===");
      for(BusStop stop : busStops) {
        System.out.println(stop);
      }
      
      System.out.println();
    }
  }
}
