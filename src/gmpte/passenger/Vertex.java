/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.passenger;

import java.util.ArrayList;

/**
 *
 * @author mbgm2rm2
 * @param <T>
 */
public class Vertex<T extends Comparable<T>> implements Comparable<Vertex<T>> {
  
  public T data;
  public double key;
  public KeyType keyType;
  public boolean explored;
  
  public enum KeyType {
    REAL, INFINITE
  };
  
  public ArrayList<Vertex<T>> adjList;
  public ArrayList<Edge<T>> edges;
  
  public Vertex(T data) {
    this.data = data;
    adjList = new ArrayList<>();
    key = Double.MAX_VALUE;
    keyType = KeyType.INFINITE;
    explored = false;
    edges = new ArrayList<>();
  }
  
  public T getData() {
    return data;
  }
  
  public void addAdjVertex(Vertex<T> v) {
    adjList.add(v);
  }
  
  public void addEdge(Edge<T> e) {
    edges.add(e);
  }
  
  public ArrayList<Edge<T>> getEdges() {
    return edges;
  }
  
  public ArrayList<Vertex<T>> getAdjVertices() {
    return adjList;
  }
  
  @Override
  public boolean equals(Object other) {
    Vertex v = (Vertex)other;
    return v.getData().equals(data);
  }

  @Override
  public int compareTo(Vertex<T> o) {
    if(key < o.key)
      return -1;
    return 1;
  }
  
  public void setKey(double key) {
    this.key = key;
    keyType = KeyType.REAL;
  }
  
  public double getKey() {
    return key;
  }
 
  public boolean isReachable() {
    return keyType == KeyType.REAL;
  }
  
  public void flashKey() {
    key = Integer.MAX_VALUE;
    keyType = KeyType.INFINITE;
  }
  
  public boolean isExplored() {
    return explored;
  }
  
  public void setExplored(boolean explored) {
    this.explored = explored;
  }
  
  @Override
  public String toString() {
    return data.toString();
  }
}
