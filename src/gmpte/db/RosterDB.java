/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.db;

import gmpte.GMPTEConstants;
import gmpte.entities.Bus;
import gmpte.entities.Driver;
import gmpte.entities.Roster;
import gmpte.entities.Route;
import gmpte.entities.Service;
import gmpte.helpers.DBHelper;
import gmpte.helpers.SQLQueryFilter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mbgm2rm2
 */
public class RosterDB {
  
  public static boolean insertRoster(String[] fields, String[] values) throws SQLException {
    StringBuilder builder = new StringBuilder();
    builder.append("INSERT INTO roster (");
    for(int i=0; i<fields.length; i++) {
      if(i>0)
        builder.append(",");
      builder.append(fields[i]);
    }
    
    builder.append(") VALUES(");
    for(int i=0; i<values.length; i++) {
      if(i>0)
        builder.append(",");
      builder.append("?");
    }
    builder.append(")");
    
    Connection connection = database.busDatabase.getConnection();
    PreparedStatement statement = connection.prepareStatement(builder.toString());
    
    for(int i=0; i<values.length; i++) {
      statement.setString(i+1, values[i]);
    }
    return statement.execute();
  }
  
  
  public static ArrayList<Roster> getRosterBy(Driver driver, Integer route, Bus bus, Service service, 
                                                  Integer duration, java.util.Date date) {
    SQLQueryFilter filter = DBHelper.formRosterQueryFilter(driver, route, bus, service, duration, date);
    filter.order("date", SQLQueryFilter.ORDER.ASC);
    filter.order("driver", SQLQueryFilter.ORDER.ASC);
    
    return getRosterBy(filter.getWhereFields(), filter.getWhereValues(), filter.getOrderBy(), filter.getOrder());
  }
  
  public static ArrayList<Roster> getGlobalRoster() {
    SQLQueryFilter filter = DBHelper.formRosterQueryFilter(null, null, null, null, null, null);
    filter.order("date", SQLQueryFilter.ORDER.ASC);
    filter.order("driver", SQLQueryFilter.ORDER.ASC);
    
    return getRosterBy(filter.getWhereFields(), filter.getWhereValues(), filter.getOrderBy(), filter.getOrder());
  }
  
  public static ArrayList<Roster> getRosterBy(String where[], String values[],
                                                  String[] orderBy, String[] order) {
    ArrayList<Roster> rosters = new ArrayList<Roster>();
    
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT * FROM roster");
    
    // handle where query
    builder.append(DBHelper.prepareWhere(where, values));
    
    // handle order by
    builder.append(DBHelper.prepareOrderBy(orderBy, order));
    
    builder.append(" LIMIT 500");
    
    try {
      Connection connection = database.busDatabase.getConnection();
      PreparedStatement statement = connection.prepareStatement(builder.toString());
      
      for(int i=0; i<values.length; i++) {
        statement.setString(i+1, values[i]);
      }
      ResultSet result = statement.executeQuery();
      
      while(result.next()) {
        gmpte.entities.Driver driver = DriverInfo.fetchDriver(result.getInt("driver"));
        gmpte.entities.Bus bus = BusInfo.fetchBus(result.getInt("bus"));
        gmpte.entities.Service service = new Service(result.getInt("service"));
        int weekDay = result.getInt("day");
        Date date = result.getDate("date");
        
        if(driver!=null && bus!=null) {
          // add the roster to the arrayList
          rosters.add(new Roster(driver, bus, new Route(service.getRoute()), service, weekDay, date));
        }
      }
    } catch (SQLException ex) {
      Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return rosters;
  }
  
  public static void clearAfterDate(java.util.Date date) throws SQLException {
    DateFormat dateFormat = new SimpleDateFormat(GMPTEConstants.DATETIME_FORMAT_SQL);
    // truncate roster table
    PreparedStatement statement = database.busDatabase.getConnection()
                          .prepareStatement("DELETE FROM roster WHERE date>=?");
    
    statement.setString(1, dateFormat.format(date));
    statement.execute();
  }
}
