package gmpte.passenger;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mbgm2rm2
 */
public class Edge<T extends Comparable<T>> {
  
  private Vertex<T> v1;
  private Vertex<T> v2;
  private double weight;
  
  public Edge(Vertex<T> v1, Vertex<T> v2) {
    this.v1 = v1;
    this.v2 = v2;
  }
  
  public Edge(Vertex<T> v1, Vertex<T> v2, double weight) {
    this(v1, v2);
    this.weight = weight;
  }
  
  public Vertex<T> getVertex1() {
    return v1;
  }
  
  public Vertex<T> getVertex2() {
    return v2;
  }
  
  public double getWeight() {
    return weight;
  }
  
  public void setWeight(double weight) {
    this.weight = weight;
  }
}
