/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
  
}
