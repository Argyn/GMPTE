/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.helpers;

import gmpte.passenger.Edge;
import gmpte.passenger.Graph;
import gmpte.passenger.PathSolution;
import gmpte.passenger.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 *
 * @author mbgm2rm2
 */
public class PathFinder {
  public static <T extends Comparable<T>> HashMap<T, T> 
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
                                
  public static <T extends Comparable<T>> 
    HashMap<Vertex<T>, ArrayList<Vertex<T>>> bfsPathsFind(Graph<T> graph, 
                                                    T from, T to) {
    
    Vertex<T> startVertex = graph.getVertices()
                                          .get(graph.getVertices().indexOf(new Vertex(from)));
    Vertex<T> target = graph.getVertices()
                                          .get(graph.getVertices().indexOf(new Vertex(to)));
    
    HashMap<Vertex<T>, ArrayList<Vertex<T>>> path = new HashMap<>();

    LinkedList<Vertex> queue = new LinkedList<>();
    queue.add(startVertex);

    startVertex.setExplored(true);

    //ArrayList<Path> pathes = new ArrayList<Path>();

    while(queue.size()>0) {
      Vertex<T> fetchedVertex = queue.poll();

      //if(fetchedVertex.equals(target))
      //	return path;

      for(Vertex<T> v : fetchedVertex.getAdjVertices()) {

        if(path.get(v)!=null) {
          if(!path.get(v).contains(fetchedVertex))
              path.get(v).add(fetchedVertex);
        } else {
          ArrayList<Vertex<T>> list = new ArrayList<>();
          list.add(fetchedVertex);
          path.put(v, list);
        }

        if(!v.isExplored()) {
          if(!v.equals(target)) {
              v.setExplored(true);
          }

          queue.add(v);
        }
      }
    }

    return path;
  }
    
public static <T extends Comparable<T>> void buildPath(
                                HashMap<Vertex<T>,ArrayList<Vertex<T>>> pathMap, 
                                                  Vertex<T> source, 
                                                  Vertex<T> target, 
                                                  PathSolution<T> path, 	
                                                  ArrayList<PathSolution<T>> result) {

    path.addHeadVertex(target);

    if(pathMap.get(target)!=null && !pathMap.get(target).equals(source)) {
        ArrayList<Vertex<T>> nextPath = pathMap.get(target);

        if(nextPath.size()>0) {

            for(Vertex adjV : nextPath) {
                try {
                    if(adjV.equals(source)) {
                      path.addHeadVertex(adjV);
                      result.add(path);
                    } else if(!path.contains(adjV))
                      buildPath(pathMap, source, adjV, (PathSolution<T>)path.clone(), result);	
                    
                } catch(CloneNotSupportedException ex) {
                    System.err.println(ex);
                    System.exit(0);
                }	
            }
        }
    } else {
        System.out.println("Adding final path");
        result.add(path);
    }
  }
}
