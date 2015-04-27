/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gmpte.db;

import gmpte.entities.Area;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author argyn
 */
public class AreaDBInfo {
  
  public static ArrayList<Area> getAllAreas() {
    ArrayList<Area> areas = new ArrayList<>();
    String query = "SELECT area_id, name, code FROM area";
    
    try {
      ResultSet result = DBHelper.prepareStatement(query).executeQuery();
      
      while(result.next()) {
        // parse and result
        areas.add(parseArea(result));
      }
     
    } catch (SQLException ex) {
      Logger.getLogger(AreaDBInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return areas;
  }
  
  public static Area getAreaByID(int areaID) {
    String query = "SELECT area_id, name, code FROM area WHERE area_id=?";
    
    try {
      // setting parameters
      PreparedStatement statement = DBHelper.prepareStatement(query);
      statement.setInt(1, areaID);
      
      ResultSet result = statement.executeQuery();
      
      if(result.next()) {
        // parse and result
        return parseArea(result);
      }
     
    } catch (SQLException ex) {
      Logger.getLogger(AreaDBInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return null;
  }
  
  public static Area parseArea(ResultSet result) throws SQLException {
    if(result!=null) {
      int id = result.getInt("area_id");
      String name = result.getString("name");
      String code = result.getString("code");
      
      return new Area(id, name, code); 
    }
    
    return null;
  }
  
}
