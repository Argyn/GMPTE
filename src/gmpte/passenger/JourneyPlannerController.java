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
import gmpte.helpers.PathFinder;
import java.util.ArrayList;
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
          
          graph.addEdge(source, target);
        }
      }
    }
    
    return graph;
  }
  
  public String getShortestPathString(LinkedList<BusStop> finalPath) {
    
    //LinkedList<BusStop> finalPath = routeToLinkedList(path, target);
    
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
    this.routes = RouteDBInfo.getAllRoutes();
    this.allBStops = BusStopInfo.getAllBusStops();
    this.network = buildNetwork(routes);
    this.areas = AreaDBInfo.getAllAreas();
    
    formAreasStationsMap();
  }
  
  public Path getJourneyPlan(BusStop from, BusStop to) {
    HashMap<BusStop, BusStop> path = 
                        PathFinder.<BusStop>getShortestPath(network, from, to);
    
    LinkedList<BusStop> result = routeToLinkedList(path, to);
    
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
      System.out.println(bStop.getArea());
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
 
  /*private ArrayList<PathSolution<BusStop>> getAllPathSolutions(BusStop from, BusStop to) {
    
    HashMap<Vertex<BusStop>, ArrayList<Vertex<BusStop>>> pathMap =
            PathFinder.bfsPathsFind(network, from, to);
    
    ArrayList<PathSolution<BusStop>> allPathes = new ArrayList<>();

    PathSolution<BusStop> p = new PathSolution<>();
    
    Vertex<BusStop> target = network.getVertices()
                                          .get(network.getVertices().indexOf(new Vertex(to)));
    
    Vertex<BusStop> source = network.getVertices()
                                          .get(network.getVertices().indexOf(new Vertex(from)));
    
    PathFinder.<BusStop>buildPath(pathMap, source, target, p, allPathes);
    
    return allPathes;
  }
  
  private ArrayList<Path> getPathes(BusStop from, BusStop to) {
    ArrayList<PathSolution<BusStop>> pathSolutions = getAllPathSolutions(from, to);
    ArrayList<Path> results = new ArrayList<>();
    
    for(PathSolution<BusStop> sol : pathSolutions) {
      for(BusStop stop : sol.getData()) {
        System.out.println(stop+","+stop.getRoutesString());
      }
      results.add(new Path(sol.getData()));
    }
    
    return results;
  }
  
  public ArrayList<Path> getJourneyPlans(BusStop from, BusStop to) {
    return getPathes(from, to);
  }*/
}
