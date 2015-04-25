/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testing;

import java.util.ArrayList;

/**
 *
 * @author mbgm2rm2
 * @param <T>
 */
public class Vertex<T extends Comparable<T>> implements Comparable<Vertex<T>> {
  
  public T data;
  public int key;
  public KeyType keyType;
  
  public enum KeyType {
    REAL, INFINITE
  };
  
  public ArrayList<Vertex<T>> adjList;
  
  public Vertex(T data) {
    this.data = data;
    adjList = new ArrayList<Vertex<T>>();
    key = Integer.MAX_VALUE;
    keyType = KeyType.INFINITE;
  }
  
  public T getData() {
    return data;
  }
  
  public void addAdjVertex(Vertex<T> v) {
    adjList.add(v);
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
  
  public void setKey(int key) {
    this.key = key;
    keyType = KeyType.REAL;
  }
  
  public int getKey() {
    return key;
  }
 
  public boolean isReachable() {
    return keyType == KeyType.REAL;
  }
}
