/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author mbgm2rm2
 */
public class BusStop implements Comparable<BusStop> {

  private int sequence;
  private final String name;
  private final ArrayList<Route> routes;
  private Area area;
  
  private ArrayList<Integer> ids;
  
  public BusStop(Area area, String name) {
    this.area = area;
    this.name = name;
    this.sequence = 0;
    routes = new ArrayList<>();
    ids = new ArrayList<>();
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
    
    BusStop otherBStop = (BusStop)other;
      if(otherBStop!=null) {
        return otherBStop.name.equals(name) && area.equals(otherBStop.area);
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
  
  public void addId(int id) {
    ids.add(id);
  }
  
  public void addId(ArrayList<Integer> ids) {
    for(Integer i : ids) {
      if(!this.ids.contains(i))
        this.ids.add(i);
    }
  }
  
  public ArrayList<Integer> getIds() {
    return ids;
  }
  
  
}
