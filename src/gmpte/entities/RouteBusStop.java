/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

import java.util.Objects;

/**
 *
 * @author mbgm2rm2
 */
public class RouteBusStop {
  private Route route;
  private BusStop stop;
  
  public RouteBusStop(Route route, BusStop stop) {
    this.route = route;
    this.stop = stop;
  }
  
  @Override
  public boolean equals(Object other) {
    RouteBusStop otherRBS = (RouteBusStop)other;
    return route.equals(otherRBS.route) && stop.equals(otherRBS.stop);
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 47 * hash + Objects.hashCode(this.route);
    hash = 47 * hash + Objects.hashCode(this.stop);
    return hash;
  }
}
