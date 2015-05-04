/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.dailytimetable;

import gmpte.db.AreaDBInfo;
import gmpte.db.BusStopInfo;
import gmpte.db.RouteDBInfo;
import gmpte.db.ServiceDB;
import gmpte.entities.Area;
import gmpte.entities.BusStop;
import gmpte.entities.Route;
import gmpte.entities.RouteBusStop;
import gmpte.entities.Service2;
import gmpte.helpers.DateHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author mbgm2rm2
 */
public class DailyTimetableController {
  
  private ArrayList<Route> routes;
  
  private HashMap<Route, ArrayList<BusStop>> routeBusStops;
  
  private ArrayList<BusStop> allBusStops;
  
  private ArrayList<Area> areas;
  
  private HashMap<Route, ArrayList<Service2>> routeServices;
  
  private HashMap<RouteBusStop, ArrayList<Date>> routeBusStopsTimes;
          
  private int kind;
  
  public DailyTimetableController() {
    // fetching all areas
    areas = AreaDBInfo.getAllAreas();
    
    // fetching all routes
    routes = RouteDBInfo.getAllRoutes(); 

    // fetching all bus stops
    allBusStops = BusStopInfo.getAllBusStops(areas);
    
    routeServices = new HashMap<>();
    routeBusStops = new HashMap<>();
    routeBusStopsTimes = new HashMap<>();
    
    kind = DateHelper.getKind(new Date());
    
    // map routes and bus stops
    mapRoutesAndBusStops();
    
    // map routes and services
    mapRoutesAndServices();
    
    // map routes, bus stops and times
    mapRoutesBusStopsAndTimes();
  }
  
  private BusStop getOriginalBusStop(BusStop bStop) {
    return allBusStops.get(allBusStops.indexOf(bStop));
  }
  
  private void mapRoutesAndBusStops() {
    for(Route route : routes) {
      ArrayList<BusStop> routeBStops = BusStopInfo.getDistinctBusStopsByRoute(route);
      ArrayList<BusStop> newRouteBStops = new ArrayList<>();
      for(BusStop stop : routeBStops) {
        newRouteBStops.add(getOriginalBusStop(stop));
      }
      routeBusStops.put(route, newRouteBStops);
    }
  }
  
  private void mapRoutesAndServices() {
    for(Route route : routes) {
      routeServices.put(route, ServiceDB.getServicesByRoute(route, kind));
    }
  }
  
  private void mapRoutesBusStopsAndTimes() {
    for(Route route : routes) {
      for(BusStop stop : getBusStopsOfRoute(route)) {
        RouteBusStop routeBStop = new RouteBusStop(route, stop);
        ArrayList<Date> times = ServiceDB.getTimesByRouteAndBusStop(route, stop, kind);
        
        
        routeBusStopsTimes.put(routeBStop, times);
        if(times.size()==0) {
          System.out.println("NULL TIMES!!!");
          System.out.println(route.getRouteID());
          System.out.println(stop);
        }
      }
    }
  }
  
  public ArrayList<Date> getRouteBusStopTimes(Route route, BusStop stop) {
    RouteBusStop routeBusStop = new RouteBusStop(route, stop);
    return routeBusStopsTimes.get(routeBusStop);
  }
  
  
  
  
  
  public ArrayList<BusStop> getBusStopsOfRoute(Route route) {
    return routeBusStops.get(route);
  }
  
  public ArrayList<Service2> getServicesOfRoute(Route route) {
    return routeServices.get(route);
  }
  
  public ArrayList<Route> getRoutes() {
    return routes;
  }
  
  public ArrayList<BusStop> getAllBusStops() {
    return allBusStops;
  }
  
  
  
  
}
