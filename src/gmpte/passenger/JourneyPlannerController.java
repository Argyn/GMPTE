/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.passenger;

import gmpte.db.AreaDBInfo;
import gmpte.db.BusStopInfo;
import gmpte.db.RouteDBInfo;
import gmpte.entities.Area;
import gmpte.entities.BusStop;
import gmpte.entities.Path;
import gmpte.entities.Route;
import gmpte.helpers.DateHelper;
import gmpte.helpers.PathFinder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author mbgm2rm2
 */
public class JourneyPlannerController {
  
  private Graph<BusStop> network;
  
  private ArrayList<BusStop> allBStops;
  
  private ArrayList<Route> routes;
  
  private ArrayList<Area> areas;
  
  private HashMap<Area, ArrayList<BusStop>> areasStationsMap;
  
  public BusStop getOriginalBusStop(BusStop bStop) {
    return allBStops.get(allBStops.indexOf(bStop));
  }
  
  
  public Graph buildNetwork(ArrayList<Route> routes) {
    Graph<BusStop> graph = new Graph<>();
    
    
    for(Route currentRoute : routes) {
      // fetch all bus stops on that route
      ArrayList<BusStop> bStops = BusStopInfo.getBusStopsByRoute(currentRoute);
      
      Iterator<BusStop> it = bStops.iterator();
      
      Iterator<BusStop> newIt = bStops.iterator();
      if(newIt.hasNext())
        newIt.next();
      
      while(it.hasNext()) {
        // source bus stop
        BusStop sourceBStop = getOriginalBusStop(it.next());
        sourceBStop.addRoute(currentRoute);
        
        Vertex<BusStop> source = new Vertex<>(sourceBStop);
        
        graph.addVertex(source);
        
        if(newIt.hasNext()) {
          // get original target bus stop
          BusStop targetBStop = getOriginalBusStop(newIt.next());
          
          Vertex<BusStop> target = new Vertex<>(targetBStop);
          
          double weight = BusStopInfo.getTimeBetweenBusStops(sourceBStop, targetBStop, DateHelper.getKind(new Date()));
          if(weight!=-1 ) {
            // add edge between bus stops with time = weight
            graph.addEdge(source, target, weight);
          } 
        }
      }
    }
    
    return graph;
  }
  
  public String getShortestPathString(LinkedList<BusStop> finalPath) {
    StringBuilder builder = new StringBuilder();
    while(finalPath.peek()!=null) {
      BusStop bStop = finalPath.poll();
      builder.append(bStop).append("(").append(bStop.getRoutesString()).append(")");
      if(finalPath.peek()!=null)
        builder.append(" --> ");
    }
    
    return builder.toString();
  }
  
  private LinkedList<BusStop> routeToLinkedList(HashMap<BusStop, BusStop> path, BusStop target) {
    Vertex<BusStop> v = new Vertex(target);
    BusStop realTarget = getOriginalBusStop(target);
            
    LinkedList<BusStop> finalPath = new LinkedList<>();
    
    BusStop stop = path.get(realTarget);
    
    if(stop!=null) {
      finalPath.addFirst(realTarget);
      
      while(stop!=null) {
        finalPath.addFirst(stop);
        stop = path.get(stop);
      }
    }
    
    return finalPath;
  }
  
  public JourneyPlannerController() {
    // fetch all routes from database
    this.routes = RouteDBInfo.getAllRoutes();
    
    // fetch all ares from database
    this.areas = AreaDBInfo.getAllAreas();
    
    // fetch all bus stops
    this.allBStops = BusStopInfo.getAllBusStops(areas);
    
    // build bus stops network
    this.network = buildNetwork(routes);
    
    // map areas and bus stations
    formAreasStationsMap();
  }

  
  public Path getJourneyPlan(BusStop from, BusStop to) {
    // get shortest path
    HashMap<BusStop, BusStop> path = 
                        PathFinder.<BusStop>getShortestPath(network, from, to);
    
    // convert shortest path to linked list
    LinkedList<BusStop> result = routeToLinkedList(path, to);
    
    // build path 
    return new Path(result);
  }
  
  public ArrayList<BusStop> getBusStops() {
    return allBStops;
  }
  
  public ArrayList<Route> getRoutes() {
    return routes;
  }
  
  public ArrayList<Area> getAreas() {
    return areas;
  }
  
  private void formAreasStationsMap() {
    areasStationsMap = new HashMap<>();
    
    for(BusStop bStop : allBStops) {
      if(areasStationsMap.get(bStop.getArea())!=null) {
        areasStationsMap.get(bStop.getArea()).add(bStop);
      } else {
        ArrayList<BusStop> list = new ArrayList<>();
        list.add(bStop);
        areasStationsMap.put(bStop.getArea(), list);
      }
    }
  }
  
  public ArrayList<BusStop> getAreasBusStop(Area area) {
    return areasStationsMap.get(area);
  }
  
  public Graph<BusStop> getNetwork() {
    return network;
  }
}
