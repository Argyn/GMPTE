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
  private int routeID;
  
  public Route(int routeID) {
    this.routeID = routeID;
  }
  
  public int getRouteID() {
    return routeID;
  }
  
  public boolean equals(Object other) {
    Route otherRoute = (Route)other;
    return routeID == otherRoute.routeID;
  }
  
  @Override
  public String toString() {
    return Integer.toString(routeID);
  }
}
