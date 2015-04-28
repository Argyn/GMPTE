/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

/**
 *
 * @author mbgm2rm2
 */
public class Route {
  private int id;
  
  private String name;
  
  public Route(int id) {
    this.id = id;
  }
  
  public Route(int id, String name) {
    this(id);
    this.name = name;
  }
  
  public int getRouteID() {
    return id;
  }
  
  public boolean equals(Object other) {
    Route otherRoute = (Route)other;
    return id == otherRoute.id;
  }
  
  @Override
  public String toString() {
    return name;
  }
}
