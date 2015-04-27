/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 *
 * @author mbgm2rm2
 */
public class BusStop implements Comparable<BusStop> {

  private int sequence;
  private final String name;
  private final ArrayList<Route> routes;
  private Area area;
  
  
  public BusStop(Area area, String name) {
    this.area = area;
    this.name = name;
    this.sequence = 0;
    routes = new ArrayList<>();
  }
  
  public BusStop(Area area, String name, int sequence) {
    this(area, name);
    this.sequence = sequence;
  }
  
  public Area getArea() {
    return area;
  }
  
  public String getName() {
    return name;
  }
  
  public int getSequence() {
    return sequence;
  }
  
  public void addRoute(Route route) {
    if(!hasRoute(route)) {
      routes.add(route);
    }
  }
  
  public boolean hasRoute(Route route) {
    return routes.contains(route);
  }
  
  public ArrayList<Route> getRoutes() {
    return routes;
  }
  
  @Override
  public String toString() {
    return name+"("+area+")";
  }
  
  public String getRoutesString() {
    if(routes.size() > 0) {
      StringBuilder builder = new StringBuilder();
      Iterator<Route> it = routes.iterator();
      
      while(it.hasNext()) {
        builder.append(Integer.toString(it.next().getRouteID()));
        if(it.hasNext())
          builder.append(",");
      }
      
      return builder.toString();
    }
    
    return null;
  }
  
  @Override
  public boolean equals(Object other) {
    BusStop otheBStop = (BusStop)other;
      if(otheBStop!=null) {
        return otheBStop.name.equals(name) && area.equals(otheBStop.area);
      }
    return false;
  }

  @Override
  public int compareTo(BusStop o) {
    if(sequence < o.sequence)
      return -1;
    else
      return 1;
    
  }
  
  
}
