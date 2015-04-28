/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

import gmpte.db.ServiceDB;
import gmpte.helpers.ListHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author mbgm2rm2
 */
public class Path {
  
  private final LinkedList<BusStop> fullPath;
  private final LinkedList<BusChange> changes;
  
  public Path(LinkedList<BusStop> fullPath) {
    this.fullPath = fullPath;
    changes = new LinkedList<>();
    
    Route currentRoute = chooseRoute(fullPath.peek(), fullPath.get(1));
    System.out.println(fullPath.get(1));
    System.out.println(currentRoute);
    
    BusChange change = new BusChange(currentRoute);
    // add starting bus station
    
    BusStop startBStop = fullPath.poll();
    
    change.setBoardingTime(ServiceDB.getNearestServiceTime(currentRoute, startBStop, startBStop, new Date()));
    
    System.out.println("Start on "+startBStop+" Route:"+currentRoute.getRouteID());
    change.addBusStation(startBStop);
    
    while(fullPath.peek()!=null) {
      BusStop current = fullPath.poll();
      BusStop next = null;
      
      if(fullPath.size()>0)
        next = fullPath.get(0);
      
      if(next!=null && !next.hasRoute(currentRoute)) {
        changes.add(change);
        System.out.println("Choosing common root betwen "+current+current.getRoutesString()+" and "+next+next.getRoutesString());
        currentRoute = chooseRoute(current, next);
        System.out.println("Chose "+currentRoute.getRouteID());
        change = new BusChange(currentRoute);
      }
      
      System.out.println(current+" Route:"+currentRoute.getRouteID());
      
      change.addBusStation(current);
    }
    
    changes.add(change);
    
  }
  
  /*private ArrayList<BusStop> fullPath;
  private LinkedList<BusChange> changes;
  
  public Path(ArrayList<BusStop> fullPath) {
    this.fullPath = fullPath;
    changes = new LinkedList<>();
    
    Iterator<BusStop> it = fullPath.iterator();
    Iterator<BusStop> it2 = fullPath.iterator();
    
    Route currentRoute = chooseRoute(fullPath.get(0), fullPath.get(1));
   
    //System.out.println(fullPath.get(1));
    //System.out.println(currentRoute);
    
    BusChange change = new BusChange(currentRoute);
    // add starting bus station
    
    BusStop startBStop = it.next();
    it2.next();
    
    System.out.println("Start on "+startBStop+" Route:"+currentRoute.getRouteID());
    change.addBusStation(startBStop);
    
    while(it.hasNext()) {
      BusStop current = it.next();
 
      BusStop next = null;
      
      if(it2.hasNext() && it2.next()!=null && it2.hasNext()) {
        next = it2.next();
        System.out.println("Current:"+current);
        System.out.println("Next:"+next);
      }
      
      if(next!=null && !next.hasRoute(currentRoute)) {
        changes.add(change);
        System.out.println("Choosing common root betwen "+current+current.getRoutesString()+" and "+next+next.getRoutesString());
        currentRoute = chooseRoute(current, next);
        System.out.println("Chose "+currentRoute.getRouteID());
        change = new BusChange(currentRoute);
      }
      
      System.out.println(current+" Route:"+currentRoute.getRouteID());
      
      change.addBusStation(current);
    }
    
    changes.add(change);

  }*/
  
  public LinkedList<BusChange> getFullPath() {
    return changes;
  }
  
  private Route chooseRoute(BusStop current, BusStop nextStation) {
    ArrayList<Route> currentRoutes = current.getRoutes();
    ArrayList<Route> nextStationRoutes = nextStation.getRoutes();
    
    ArrayList<Route> routes = (ArrayList<Route>)ListHelper
                            .<Route>intersect(currentRoutes, nextStationRoutes);
    
    
    if(routes.size()>0)
      return routes.get(0);
    
    return null;
  }
}
