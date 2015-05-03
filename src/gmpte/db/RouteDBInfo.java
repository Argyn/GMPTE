package gmpte.db;


import gmpte.entities.BusStop;
import gmpte.entities.Route;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mbgm2rm2
 */
public class RouteDBInfo {
  
  private static Route formRoute(ResultSet result) throws SQLException {
    int id = result.getInt("id");
    String name = result.getString("name");
    
    return new Route(id, name);
    
  }
  
  public static ArrayList<Route> getAllRoutes() {
    
    String query = "SELECT route_id as id, name FROM route";
    
    ArrayList<Route> routes = new ArrayList<>();
    
    try {
      ResultSet result = DBHelper.prepareStatement(query).executeQuery();
      
      while(result.next()) {
        routes.add(formRoute(result));
      }
    } catch (SQLException ex) {
      Logger.getLogger(RouteDBInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return routes;
  }
  
  public static String buildBusStops(BusStop start, String id) {
    StringBuilder builder = new StringBuilder();
    
    if(start.getIds().size()>1) {
      builder.append("(");
      Iterator<Integer> it = start.getIds().iterator();
      
      while(it.hasNext()) {
        builder.append(" "+id+".bus_stop="+it.next());
        if(it.hasNext())
          builder.append(" OR ");
      }
      builder.append(")");
    } else {
      builder.append(id).append(".bus_stop=").append(start.getIds().get(0));
    }
    
    return builder.toString();
  }
  
  public static boolean doesRouteGoFromTo(BusStop from, BusStop to, Route route) {
    String whereTimingPoints1 = buildBusStops(from, "b1");
    String whereTimingPoints2 = buildBusStops(to, "b2");
    
    String query = "SELECT * FROM path b1, path b2 WHERE %s AND %s AND b2.bus_stop>b1.bus_stop AND b1.route=b2.route AND b1.route=?";
    
    query = String.format(query, whereTimingPoints1, whereTimingPoints2);
    PreparedStatement statement = null;
    
    try {
      statement = DBHelper.prepareStatement(query);
      
      // setting the route
      statement.setInt(1, route.getRouteID());
      
      ResultSet result = statement.executeQuery();
      
      System.out.println(statement.toString());
      
      return result.next();
    } catch (SQLException ex) {
      Logger.getLogger(RouteDBInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    
    return false;
    
  }
  
}
