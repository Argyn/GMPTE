/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

import java.util.LinkedList;

/**
 *
 * @author mbgm2rm2
 */
public class BusChange {
  
  private Route route;
  private int service;
  
  private LinkedList<BusStop> path;
  
  public BusChange(Route route) {
    this.route = route;
    path = new LinkedList<>();
  }
  
  public void addBusStation(BusStop bStop) {
    path.add(bStop);
  }
  
  public LinkedList<BusStop> getPath() {
    return path;
  }

  public Route getRoute() {
    return route;
  }
 
  
  public int getService() {
    return service;
  }
}
