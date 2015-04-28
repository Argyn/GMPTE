package gmpte.db;


import gmpte.db.DBHelper;
import gmpte.entities.Route;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
  
  
  
}
