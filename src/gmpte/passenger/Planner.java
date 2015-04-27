/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.passenger;

import testing.*;
import gmpte.db.BusStopInfo;
import gmpte.db.database;
import gmpte.entities.BusStop;
import gmpte.entities.Route;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author mbgm2rm2
 */
public class Planner {
  public static void printStops(int routeID) {
    Route route = new Route(routeID);
    ArrayList<BusStop> busStopes = BusStopInfo.getBusStopsByRoute(route);
    
    System.out.println("Bus stopes of route "+routeID);
    Iterator<BusStop> it = busStopes.iterator();
    while(it.hasNext()) {
      //BusStop bStop = it.next();
      System.out.println(it.next());
    }
  }
  
  public static ArrayList<BusStop> getBStops(int routeID) {
    Route route = new Route(routeID);
    ArrayList<BusStop> busStopes = BusStopInfo.getBusStopsByRoute(route);
    return busStopes;
  }
  
  public static Graph buildNetwork(int[] routes) {
    Graph<BusStop> graph = new Graph<BusStop>();
    
    for(int routeID : routes) {
      ArrayList<BusStop> bStops = getBStops(routeID);
      
      Iterator<BusStop> it = bStops.iterator();
      Iterator<BusStop> newIt = bStops.iterator();
      if(newIt.hasNext())
        newIt.next();
      
      while(it.hasNext()) {
        BusStop sourceBStop = it.next();
        Vertex<BusStop> source = new Vertex<BusStop>(sourceBStop);
        
        graph.addVertex(source);
        
        if(newIt.hasNext()) {
          BusStop targetBStop = newIt.next();
          Vertex<BusStop> target = new Vertex<BusStop>(targetBStop);
          
          graph.addEdge(source, target);
        }
      }
    }
    
    return graph;
  }
  
  public static <T extends Comparable<T>> HashMap<T, T> getShortestPath(Graph<T> graph, T from, T to) {
    
    HashMap<T, T> shortestPath = new HashMap<T, T>();
    Vertex<T> v = new Vertex<T>(from);
    
    
    Vertex<T> startVertex = graph.getVertices().get(graph.getVertices().indexOf(v));
    
    shortestPath.put(startVertex.getData(), null);
    
    startVertex.setKey(0);
    
    PriorityQueue<Vertex<T>> heap = new PriorityQueue<Vertex<T>>(graph.getVertices());
    
    // until heap is not empty
    while(heap.size()>0) {
      
      // fetch the next vertex
      Vertex<T> fetchedVertex = heap.poll();
      
      if(fetchedVertex.isReachable()) {
        if(fetchedVertex.getData().equals(to)) {
          break;
        }
        
        // relax adj vertices
        if(fetchedVertex.getAdjVertices().size()>0) {
          Iterator<Vertex<T>> adjVsIt = fetchedVertex.getAdjVertices().iterator();
          while(adjVsIt.hasNext()) {
            Vertex<T> adjV = adjVsIt.next();
            // only update key if a new one is smaller
            if(fetchedVertex.getKey()+1 < adjV.getKey() && heap.remove(adjV)) {
              adjV.setKey(fetchedVertex.getKey()+1);
              heap.add(adjV);

              shortestPath.put(adjV.getData(), fetchedVertex.getData());
            }
          }
        }
      }
    }
    
    return shortestPath;
    
  }
  
  public static void findRoute(BusStop from, BusStop to) {
    ArrayList<BusStop> route65 = getBStops(65);
    ArrayList<BusStop> route66 = getBStops(66);
    ArrayList<BusStop> route67 = getBStops(67);
   
  }
 
  
  public static void printNetwork(Graph graph) {
    Iterator<Vertex<BusStop>> verticesIT = graph.getVertices().iterator();
    while(verticesIT.hasNext()) {
      Vertex<BusStop> next = verticesIT.next();
      System.out.print(next.getData());
      if(next.getAdjVertices().size()>0) {
        System.out.print(" --> ");
        Iterator<Vertex<BusStop>> adjIt = next.getAdjVertices().iterator();
        while(adjIt.hasNext()) {
          Vertex<BusStop> nextAdj = adjIt.next();
          System.out.print(nextAdj.getData()+",");
        }
      }
      System.out.println();
    }
  }
  
  public static void printShortestPath(Graph<BusStop> graph, HashMap<BusStop, BusStop> path, BusStop target) {
    Vertex<BusStop> v = new Vertex(target);
    BusStop realTarget = graph.getVertices().get(graph.getVertices().indexOf(v)).getData();
    
    BusStop stop = path.get(realTarget);
    System.out.print(realTarget+"<--");
    
    while(stop!=null) {
      System.out.print(stop+" <--");
      stop = path.get(stop);
    }
  }
  
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    database.openBusDatabase();
    
    int[] routes = new int[]{65,66,67, 68};
    
    printStops(65);
    System.out.println("==============");
    printStops(66);
    System.out.println("==============");
    printStops(67);
    System.out.println("==============");
    printStops(68);
    System.out.println("==============");
    
    Graph bStopsNetwork = buildNetwork(routes);
    
    printNetwork(bStopsNetwork);
    
    BusStop from = new BusStop(221, "Little Hayfield", 1);
    BusStop to = new BusStop(209, "Asda/Sainsbury's", 1);
    
    HashMap<BusStop, BusStop> shortestPath = getShortestPath(bStopsNetwork, from, to);
    
    printShortestPath(bStopsNetwork, shortestPath, to);
    /*ArrayList<BusStop> route65 = getBStops(65);
    ArrayList<BusStop> route66 = getBStops(66);
    ArrayList<BusStop> route67 = getBStops(67);
    */
    /*System.out.println("From bus stop:");
    String fromBusStop = scanner.nextLine();
    System.out.println("To bus stop:");
    String toBusStop = scanner.nextLine();*/
  }
} 
