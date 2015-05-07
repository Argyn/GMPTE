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
  
  private ArrayList<Service2> allServicesToday;
  
  private HashMap<Route, ArrayList<Service2>> routeServices;
  
  private HashMap<RouteBusStop, ArrayList<Date>> routeBusStopsTimes;
          
  private int kind;
  
  private ArrayList<Service2> delayedServices;
  
  private ArrayList<Service2> cancelledServices;
  
  public DailyTimetableController() {
    // fetching all areas
    areas = AreaDBInfo.getAllAreas();
    
    // fetching all routes
    routes = RouteDBInfo.getAllRoutes(); 

    // fetching all bus stops
    allBusStops = BusStopInfo.getAllBusStops(areas);
    
    Date todayDate = new Date();
    
    kind = DateHelper.getKind(todayDate);
    
    allServicesToday = ServiceDB.getAllServices(todayDate);
    // map routes and bus stops
    mapRoutesAndBusStops();
    
    // map routes and services
    mapRoutesAndServices();
    
  }
  
  private BusStop getOriginalBusStop(BusStop bStop) {
    return allBusStops.get(allBusStops.indexOf(bStop));
  }
  
  private void mapRoutesAndBusStops() {
    routeBusStops = new HashMap<>();
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
    routeServices = new HashMap<>();
    delayedServices = new ArrayList<>();
    cancelledServices = new ArrayList<>();
    
    for(Route route : routes) {
      ArrayList<Service2> services = ServiceDB.getServicesByRoute(route, kind);
      
      for(Service2 service : services) {
        if(service.isDelayed()) {
          delayedServices.add(service);
        } else if(service.isCancelled()) {
          cancelledServices.add(service);
        }
      }
      routeServices.put(route, services);
    }
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
  
  public ArrayList<Service2> getDelayedServices() {
    return delayedServices;
  }
  
  public ArrayList<Service2> getCancelledServices() {
    return cancelledServices;
  }
  
  public ArrayList<Service2> getAllServices() {
    return allServicesToday;
  }
  
  public void refresh() {
    mapRoutesAndServices();
  }

}
