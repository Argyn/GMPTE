/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.passenger;

import java.util.ArrayList;
import java.util.Iterator;
import testing.*;

/**
 *
 * @author mbgm2rm2
 */
public class Graph<T extends Comparable<T>> {
  private ArrayList<Vertex<T>> vertices;
  private ArrayList<Edge<T>> edges;
  
  
  public Graph() {
    vertices = new ArrayList<Vertex<T>>();
    edges = new ArrayList<Edge<T>>();
  }
  
  public void addVertex(Vertex<T> vertex) {
    if(!vertices.contains(vertex))
      vertices.add(vertex);
  }
  
  public Edge addEdge(Vertex<T> vertex1, Vertex<T> vertex2) {
    Vertex<T> source = vertex1;
    Vertex<T> target = vertex2;
    
    if(!vertices.contains(vertex1))
      addVertex(vertex1);
    else
      source = vertices.get(vertices.indexOf(vertex1));
    
    if(!vertices.contains(vertex2))
      addVertex(vertex2);
    else
      target = vertices.get(vertices.indexOf(vertex2));
    
    if(!source.equals(target)) {
      Edge e = new Edge(source, target);

      edges.add(e);
      source.addAdjVertex(target);
      source.addEdge(e);

      return e;
    }
    
    return null;
  }
  
  public void addEdge(Vertex<T> vertex1, Vertex<T> vertex2, double weight) {
    Edge e = addEdge(vertex1, vertex2);
    if(e!=null)
      e.setWeight(weight);
  }
  
  public ArrayList<Vertex<T>> getVertices() {
    return vertices;
  }
  
  public ArrayList<Edge<T>> getEdges() {
    return edges;
  }
  
  public void flashKeys() {
    Iterator<Vertex<T>> it = vertices.iterator();
    while(it.hasNext()) {
      it.next().flashKey();
    }
  }
}
