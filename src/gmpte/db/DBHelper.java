/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gmpte.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author argyn
 */
public class DBHelper {
  public static PreparedStatement prepareStatement(String query) throws SQLException {
    return database.busDatabase.getConnection().prepareStatement(query);
  }
}
