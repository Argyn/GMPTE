/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.helpers;

import gmpte.db.RosterDB;
import gmpte.entities.Bus;
import gmpte.entities.Driver;
import gmpte.entities.Roster;
import gmpte.entities.Route;
import gmpte.entities.Schedule;
import gmpte.entities.Service;
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
      builder.append(where[0]+"="+values[0]);
      for(int i=1; i<where.length; i++) {
        builder.append(" AND ");
        builder.append(where[i]+"="+values[i]);
      }
      
      return builder.toString();
    }
    
    return "";
  }
  
  public static ArrayList<Roster> searchRosterByOptions(Driver driver, Route route, Service service, 
                                                  Integer duration, Date date) {
    ArrayList<String> where = new ArrayList<String>();
    ArrayList<String> values = new ArrayList<String>();
    
    if(driver!=null) {
      where.add("driver");
      values.add(Integer.toString(driver.getDriverID()));
    }
    if(route!=null) {
      where.add("route");
      values.add(Integer.toString(route.getRouteID()));
    }
    
    return new ArrayList<Roster>();
  }
  
  public static boolean insertRoster(Driver driver, Bus bus, Service service, Route route, 
                                                          int day, int duration, Date date) throws SQLException {
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
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
  
}
