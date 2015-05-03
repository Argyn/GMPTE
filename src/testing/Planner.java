/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testing;

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
import java.util.PriorityQueue;

/**
 *
 * @author mbgm2rm2
 */
public class Planner {
  
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
          
          System.out.println("Adding edge between "+sourceBStop+" and "+targetBStop+" on route "+currentRoute.getRouteID());
          double weight = BusStopInfo.getTimeBetweenBusStops(sourceBStop, targetBStop,0);
          if(weight!=-1 ) {
            graph.addEdge(source, target, weight);
          } 
          if(weight>10){
            System.out.println("Weight is too big!!");
          }
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
  
  public Planner() {
    this.routes = RouteDBInfo.getAllRoutes();
    this.areas = AreaDBInfo.getAllAreas();
    this.allBStops = BusStopInfo.getAllBusStops(areas);
    this.network = buildNetwork(routes);
    
    
    //formAreasStationsMap();
  }
  
  /*public Path getJourneyPlan(BusStop from, BusStop to) {
    HashMap<BusStop, BusStop> path = 
                        PathFinder.<BusStop>getShortestPath(network, from, to);
    
    LinkedList<BusStop> result = routeToLinkedList(path, to);
    
    return new Path(result);
  }*/
  
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
      //System.out.println(bStop.getArea());
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
  
  public void printNetwork() {
    for(Vertex<BusStop> v : network.getVertices()) {
      System.out.print(v+"(routes="+v.getData().getRoutesString()+"), ");
      
      if(v.getEdges().size()>0) {
        System.out.print(" ---> ");
      
        for(Edge<BusStop> e : v.getEdges()) {
          System.out.print(e.getVertex2()+"(time = "+e.getWeight()+")"+"(routes="+e.getVertex2().getData().getRoutesString()+"), ");

        }
      }
      
      System.out.println();
      
    }
  }
  public <T extends Comparable<T>> HashMap<T, T> 
                                getShortestPath(Graph<T> graph, T from, T to) {
    
    // flashing the keys
    graph.flashKeys();
    
    HashMap<T, T> shortestPath = new HashMap<>();
    Vertex<T> v = new Vertex<>(from);
    
    
    Vertex<T> startVertex = graph.getVertices()
                                          .get(graph.getVertices().indexOf(v));
    
    shortestPath.put(startVertex.getData(), null);
    
    startVertex.setKey(0);
    
    PriorityQueue<Vertex<T>> heap = 
                            new PriorityQueue<>(graph.getVertices());
    
    // until heap is not empty
    while(heap.size()>0) {
      
      // fetch the next vertex
      Vertex<T> fetchedVertex = heap.poll();

      if(fetchedVertex.isReachable()) {
        if(fetchedVertex.getData().equals(to)) {
          break;
        }

        // relax adj vertices
        if(fetchedVertex.getEdges().size()>0) {
          
          Iterator<Edge<T>> edgesIt = fetchedVertex.getEdges().iterator();
          while(edgesIt.hasNext()) {
            Edge<T> adjEdge = edgesIt.next();
            Vertex<T> adjV = adjEdge.getVertex2();
            // only update key if a new one is smaller
            if(fetchedVertex.getKey()+adjEdge.getWeight() < adjV.getKey() && heap.remove(adjV)) {
              adjV.setKey(fetchedVertex.getKey()+adjEdge.getWeight());
              heap.add(adjV);

              shortestPath.put(adjV.getData(), fetchedVertex.getData());
            }
          }
        }
      }
    }
    
    return shortestPath;
    
  }
                                
  public String getJourneyPlan(BusStop from, BusStop to) {
    HashMap<BusStop, BusStop> path = this.<BusStop>getShortestPath(network, from, to);
    
    LinkedList<BusStop> result = routeToLinkedList(path, to);
    
    return getShortestPathString(result);
  }                              
}
