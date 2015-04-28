/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.db;

import gmpte.entities.Route;
import gmpte.entities.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mbgm2rm2
 */
public class ServiceDB {
  
  public static ArrayList<Service> fetchAllServices() {
    ArrayList<Service> services = new ArrayList<Service>();
    String query = "SELECT * FROM service ORDER BY service_id ASC";
    PreparedStatement statement;
    try {
      Connection connection = database.busDatabase.getConnection();
      statement = connection.prepareStatement(query);
      ResultSet result = statement.executeQuery();
      while(result.next()) {
        services.add(buildService(result));
      }
    } catch (SQLException ex) {
      Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return services;
  }
  
  public static Service fetchService(int serviceID) throws SQLException {
    String query = "SELECT s.service_id, s.daily_timetable, d.route "
            + "FROM `service` as s LEFT JOIN daily_timetable as d "
            + "ON s.daily_timetable=d.daily_timetable_id WHERE service_id=?";
    
    PreparedStatement statement = database.busDatabase.connection.prepareStatement(query);
    statement.setInt(1, serviceID);
    
    ResultSet result = statement.executeQuery();
    
    if(result.next()) {
      Service service = new Service(serviceID, result.getInt("daily_timetable"), result.getInt("route"));
      
      return service;
    }
    
    return null;
  }
  
  private static Service buildService(ResultSet result) throws SQLException {
    return new Service(result.getInt("service_id"));
  }
  
  /*private static int getNearestServiceTime(Route route, Date time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(time);
    
    int minsAfterMidnight = calendar.get(Calendar.HOUR_OF_DAY)*60
                                                +calendar.get(Calendar.MINUTE);
    
    String query = "SELECT * FROM "
  } */
}
