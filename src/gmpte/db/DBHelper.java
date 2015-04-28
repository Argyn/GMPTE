/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.db;

import gmpte.GMPTEConstants;
import gmpte.db.RosterDB;
import gmpte.db.database;
import gmpte.entities.Bus;
import gmpte.entities.Driver;
import gmpte.entities.Roster;
import gmpte.entities.Route;
import gmpte.entities.Schedule;
import gmpte.entities.Service;
import gmpte.helpers.SQLQueryFilter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author mbgm2rm2
 */
public class DBHelper {
  
  public static String prepareOrderBy(String orderBy[], String order[]) { 
    if(orderBy.length==order.length && orderBy.length>0) {
      StringBuilder builder = new StringBuilder();
      builder.append(" ORDER BY ");
      builder.append(orderBy[0]+" "+order[0]);
      for(int i=1; i<orderBy.length; i++) {
        builder.append(",");
        builder.append(" "+orderBy[i]+" "+order[i]);
      }
      
      return builder.toString();
    }
    
    return "";
    
  }
  
  public static String prepareWhere(String where[], String values[]) {
    // handling WHERE
    if(where.length == values.length && where.length>0) {
      StringBuilder builder = new StringBuilder();
      builder.append(" WHERE ");
      
      builder.append(where[0]+"=?");
      for(int i=1; i<where.length; i++) {
        builder.append(" AND ");
        builder.append(where[i]+"=?");
      }
      
      return builder.toString();
    }
    
    return "";
  }
 
  
  public static boolean insertRoster(Driver driver, Bus bus, Service service, 
                                    Route route, int day, int duration, 
                                    Date date) throws SQLException {
    ArrayList<String> fields = new ArrayList<String>();
    ArrayList<String> values = new ArrayList<String>();
    
    if(driver!=null) {
      fields.add("driver");
      values.add(Integer.toString(driver.getDriverID()));
    }
    if(bus!=null) {
      fields.add("bus");
      values.add(Integer.toString(bus.getBusId()));
    }
    if(service!=null) {
      fields.add("service");
      values.add(Integer.toString(service.getServiceId()));
    }
    if(route!=null) {
      fields.add("route");
      values.add(Integer.toString(route.getRouteID()));
     // values.add(Integer.toString(service.getRoute()));
    }
    
    fields.add("day");
    values.add(Integer.toString(day));
    
    fields.add("timeWorked");
    values.add(Integer.toString(duration));
    
    fields.add("date");
    DateFormat dateFormat = new SimpleDateFormat(GMPTEConstants.DATETIME_FORMAT_SQL);
    values.add(dateFormat.format(date));
    
    String[] fieldsArr = fields.toArray(new String[fields.size()]);
    String[] valuesArr = values.toArray(new String[values.size()]);
    
    return RosterDB.insertRoster(fieldsArr, valuesArr);
  }
  
  public static void insertRoster(Schedule schedule) throws SQLException {
    Iterator<Roster> scheduleIt = schedule.getRosters().iterator();
    while(scheduleIt.hasNext()) {
      insertRoster(scheduleIt.next());
    }
  }
  
  public static void insertRoster(Roster roster) throws SQLException {
    insertRoster(roster.getDriver(), roster.getBus(), roster.getService(), roster.getRoute(), 
                  roster.getDay(), roster.getServiceTime(), roster.getDate());
  }
  
  public static SQLQueryFilter formRosterQueryFilter(Driver driver, Integer route, 
                                                      Bus bus, Service service, 
                                                      Integer duration, 
                                                      java.util.Date dateFrom,
                                                      java.util.Date dateTo) {
    SQLQueryFilter filter = new SQLQueryFilter();
    
    if(driver!=null) {
      filter.where("driver", Integer.toString(driver.getDriverID()));
    }
    
    if(route!=null) {
      filter.where("route", Integer.toString(route));
    }
    
    if(bus!=null) {
      filter.where("bus", Integer.toString(bus.getBusId()));
    }
    
    if(service!=null) {
      filter.where("service", Integer.toString(service.getServiceId()));
    }
    
    if(duration!=null) {
      filter.where("timeWorked", Integer.toString(duration));
    }
    
    if(dateFrom!=null) {
      DateFormat dateFormat = new SimpleDateFormat(GMPTEConstants.DATETIME_FORMAT_SQL);
      filter.where("dateFrom", dateFormat.format(dateFrom));
    }
    
    if(dateTo!=null) {
      DateFormat dateFormat = new SimpleDateFormat(GMPTEConstants.DATETIME_FORMAT_SQL);
      filter.where("dateTo", dateFormat.format(dateTo));
    }
    
    return filter;
  }
  
  public static PreparedStatement prepareStatement(String query) throws SQLException {
    return database.busDatabase.getConnection().prepareStatement(query);
  }
}
