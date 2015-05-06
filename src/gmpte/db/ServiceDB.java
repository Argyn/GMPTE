/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.db;

import gmpte.entities.BusStop;
import gmpte.entities.Route;
import gmpte.entities.Service;
import gmpte.entities.Service2;
import gmpte.helpers.DateHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
  
  public static String buildTimingPoints(BusStop start, String id) {
    StringBuilder builder = new StringBuilder();
    
    if(start.getIds().size()>1) {
      builder.append("(");
      Iterator<Integer> it = start.getIds().iterator();
      
      while(it.hasNext()) {
        builder.append(" "+id+".timing_point="+it.next());
        if(it.hasNext())
          builder.append(" OR ");
      }
      builder.append(")");
    } else {
      builder.append(id).append(".timing_point=").append(start.getIds().get(0));
    }
    
    return builder.toString();
  }
  
  public static int getNearestServiceID(Route route, BusStop start, Date time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(time);
    
    int minsAfterMidnight = calendar.get(Calendar.HOUR_OF_DAY)*60
                                                +calendar.get(Calendar.MINUTE);
    
    int kind = 0;
    calendar.setTime(time);
    
    if(calendar.get(Calendar.DAY_OF_WEEK)==7)
      kind = 1;
    else if(calendar.get(Calendar.DAY_OF_WEEK)==1)
      kind = 2;
      
    String query = "SELECT * FROM bus_station_times bs, daily_timetable p "
            + "WHERE p.daily_timetable_id=bs.daily_timetable "
            + "AND p.route=? AND %s AND bs.time>? AND p.kind=? "
            + "ORDER BY time ASC LIMIT 1";
    
    StringBuilder builder = new StringBuilder();
    
    if(start.getIds().size()>1) {
      builder.append("(");
      Iterator<Integer> it = start.getIds().iterator();
      
      while(it.hasNext()) {
        builder.append(" bs.timing_point="+it.next());
        if(it.hasNext())
          builder.append(" OR ");
      }
      builder.append(")");
    } else {
      builder.append("bs.timing_point="+start.getIds().get(0));
    }
    
    query = String.format(query, builder.toString());
    
    PreparedStatement statement = null;
    try {
      statement = DBHelper.prepareStatement(query);
      
      // settign the route
      statement.setInt(1, route.getRouteID());
      
      // seting the time
      statement.setInt(2, minsAfterMidnight);
      
      // setting the day
      statement.setInt(3, kind);
      
      ResultSet result = statement.executeQuery();
      
      if(result.next()) {
        return result.getInt("service");
      }
      
    } catch (SQLException ex) {
      System.out.println(statement.toString());
      Logger.getLogger(ServiceDB.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return 0; 
  }
  
  private static void checkDelaysCancellations(Service2 service, Date time) {
    String query = "SELECT service, status, reason, delay_time "
            + "FROM service_availability WHERE service=? AND date=? LIMIT 1";
    
    PreparedStatement statement = null;
    
    try {
      statement = DBHelper.prepareStatement(query);
      
      // setting service id
      statement.setInt(1, service.getId());
      
      java.sql.Date sqlDate = new java.sql.Date(time.getTime());
      // setting the date
      statement.setDate(2, sqlDate);
      
      ResultSet result = statement.executeQuery();
      
      if(result.next()) {
        String status = result.getString("status");
        String reason = result.getString("reason");
        if(status.equals("DELAYED")) {
          int delayTime = result.getInt("delay_time");
          service.setDelayedTime(delayTime, reason);
        } else if(status.equals("CANCELLED")) {
          service.setCancelled(reason);
        }
      }
      
    } catch (SQLException ex) {
      Logger.getLogger(ServiceDB.class.getName()).log(Level.SEVERE, null, ex);
    }
  } 
  
  public static Service2 getServiceById(Route route, int serviceID) {
    String query = "SELECT timing_point, time FROM bus_station_times WHERE service=?";
    
    Service2 service = null;
    
    PreparedStatement statement = null;
    
    try {
      statement = DBHelper.prepareStatement(query);
      
      // setting the service
      statement.setInt(1, serviceID);
      
      ResultSet result = statement.executeQuery();
      
      service = new Service2(route, serviceID);
      
      while(result.next()) {
        BusStop stop = BusStopInfo.getBusStopsById(result.getInt("timing_point"));
        int minutes = result.getInt("time");
        
        service.addTimingPoint(stop, minutes);
      }
      
      // check delays and cancellations
      checkDelaysCancellations(service, new Date());
      
      return service;
      
    } catch (SQLException ex) {
      Logger.getLogger(ServiceDB.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return service;
  }
  
  public static Service2 getNearestService(Route route, BusStop start, Date time) {
    int serviceID = getNearestServiceID(route, start, time);
    
    return getServiceById(route, serviceID);
  }
  
  public static ArrayList<Service2> getServicesByRoute(Route route, int kind) {
    
    ArrayList<Service2> services = new ArrayList<>();
    
    String query = "SELECT DISTINCT bs.service FROM bus_station_times bs, "
            + "daily_timetable d WHERE d.daily_timetable_id=bs.daily_timetable "
            + "AND d.kind=? AND d.route=?";
    
    PreparedStatement statement = null;
            
    try {
      statement = DBHelper.prepareStatement(query);
      
      // setting the kind
      statement.setInt(1, kind);
      
      // setting the route
      statement.setInt(2, route.getRouteID());
      
      ResultSet result = statement.executeQuery();
      
      while(result.next()) {
        services.add(getServiceById(route, result.getInt("service")));
      }
    } catch (SQLException ex) {
      Logger.getLogger(ServiceDB.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return services;
  }
  
  public static ArrayList<Date> getTimesByRouteAndBusStop(Route route, BusStop bStop, int kind) {
    ArrayList<Date> times = new ArrayList<>();
    
    String query = "SELECT bs.time FROM bus_station_times bs, daily_timetable d "
            + "WHERE d.daily_timetable_id=bs.daily_timetable AND d.route=? "
            + "AND %s AND d.kind=?";
    
    query = String.format(query, buildTimingPoints(bStop, "bs"));
    
    PreparedStatement statement = null;
    
    
    try {
      statement = DBHelper.prepareStatement(query);
      
      // setting the route
      statement.setInt(1, route.getRouteID());
      // setting the kind
      statement.setInt(2, kind);
      
      ResultSet result = statement.executeQuery();
      
      while(result.next()) {
        times.add(DateHelper.afterMidnighMinsToDate(result.getInt("time")));
      }
      
      System.out.println(statement.toString());
      
    } catch (SQLException ex) {
      Logger.getLogger(ServiceDB.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return times;
  }
  
}
