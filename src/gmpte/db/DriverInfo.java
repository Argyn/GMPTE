package gmpte.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
 
/**
 * Class which represents information about drivers and their availability.
 * It also allows the application to get and set the number of hours a
 * driver has worked over time periods of a week or a year, and the
 * holidays taken by a driver in the current calendar year.<br><br>
 * 
 * As well as an ID, drivers (like buses) have numbers which are traditionally
 * used to identify them. You can also get the name of a specified driver<br><br>
 * 
 * The methods contain checks for invalid queries, which will result in
 * InvalidQueryExceptions being thrown, but it does not enforce "business
 * rules" such as checking for dates in the past.
 */
public class DriverInfo 
{
  // This class is not intended to be instantiated
  private DriverInfo() 
  { 
  }

  /**
   * Get the IDs of all the drivers in the database
   */
  public static int[] getDrivers()
  {
    return database.busDatabase.select_ids("driver_id", "driver", "name");
  }

  /**
   * Find the driver with the specified driver number
   */
  public static int findDriver(String number)
  {
    return database.busDatabase.find_id("driver", "number", number);
  }

  /**
   * Get the real name of a driver
   */
  public static String getName(int driver)
  {
    if (driver == 0) throw new InvalidQueryException("Nonexistent driver");
    return database.busDatabase.get_string("driver", driver, "name");
  }

  /**
   * Get the number of days holiday taken, or planned to be taken, in the
   * current calendar year
   */
  public static int getHolidaysTaken(int driver)
  {
    if (driver == 0) throw new InvalidQueryException("Nonexistent driver");
    return database.busDatabase.get_int("driver", driver, "holidays_taken");
  }

  /**
   * Set the number of days holiday taken, or planned to be taken, in
   * the current calendar year.
   */
  public static void setHolidaysTaken(int driver, int value)
  {
    if (driver == 0) throw new InvalidQueryException("Nonexistent driver");
    database.busDatabase.set_value("driver", driver, "holidays_taken", value);
  }

  /**
   * Get the number of hours worked by a driver so far this calandar year
   */
  public static int getHoursThisYear(int driver)
  {
    if (driver == 0) throw new InvalidQueryException("Nonexistent driver");
    return database.busDatabase.get_int("driver", driver, "hours_this_year");
  }

  /**
   * Set the number of hours worked by a driver so far this calandar year
   */
  public static void setHoursThisYear(int driver, int value)
  {
    if (driver == 0) throw new InvalidQueryException("Nonexistent driver");
    database.busDatabase.set_value("driver", driver, "hours_this_year", value);
  }

  /**
   * Get the number of hours worked by a driver during the period of a
   * weekly roster
   */
  public static int getHoursThisWeek(int driver)
  {
    if (driver == 0) throw new InvalidQueryException("Nonexistent driver");
    return database.busDatabase.get_int("driver", driver, "hours_this_week");
  }

   /**
   * Set the number of hours worked by a driver during the period of a
   * weekly roster
   */
  public static void setHoursThisWeek(int driver, int value)
  {
    if (driver == 0) throw new InvalidQueryException("Nonexistent driver");
    database.busDatabase.set_value("driver", driver, "hours_this_week", value);
  }

  /**
   * Get the identification number of a driver
   */
  public static String getNumber(int driver)
  {
    if (driver == 0) throw new InvalidQueryException("Nonexistent driver");
    return database.busDatabase.get_string("driver", driver, "number");
  }

  /**
   * Determine whether a driver is available on a given date
   */
  public static boolean isAvailable(int driver, Date date)
  {
    if (date == null) throw new InvalidQueryException("Date is null");
    if (driver == 0) throw new InvalidQueryException("Nonexistent driver");
    database db = database.busDatabase;
    if (db.select_record("driver_availability", "driver", driver, "day", date))
      return (Integer)db.get_field("available") != 0;
    else
      return true;
  }
  
  /**
   * Determine whether a driver is available today
   */
  public static boolean isAvailable(int driver)
  {
    return isAvailable(driver, database.today());
  }

  /**
   * Set whether a driver is available on a given date
   */
  public static void setAvailable(int driver, Date date, boolean available)
  {
    if (date == null) throw new InvalidQueryException("Date is null");
    if (driver == 0) throw new InvalidQueryException("Nonexistent driver");
    if (available && !isAvailable(driver, date))
      database.busDatabase.delete_record("driver_availability", "driver", driver, "day", date);
    else if (!available && isAvailable(driver, date))
      database.busDatabase.new_record("driver_availability", new Object[][]{{"available", false}, {"day", date}, {"driver", driver}});
  }

  /**
   * Set whether a driver is available today
   */
  public static void setAvailable(int driver, boolean available)
  {
    setAvailable(driver, database.today(), available);
  }
  
  public static gmpte.entities.Driver fetchDriver(int driverID) {
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT * FROM driver WHERE driver_id=?");
    PreparedStatement statement;
    try {
      Connection connection = database.busDatabase.getConnection();
      statement = connection.prepareStatement(builder.toString());
      // setting the paratmeter - driverID
      statement.setInt(1, driverID);
      
      // exectuing the query
      ResultSet result = statement.executeQuery();
      
      if(result.next()) {
        return buildDriver(result);
      }
    } catch (SQLException ex) {
      Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
  
  public static gmpte.entities.Driver buildDriver(ResultSet result) throws SQLException {
    gmpte.entities.Driver driver = new gmpte.entities.Driver(result.getInt("driver_id"), result.getInt("number"));
    driver.setMinutesThisWeek(result.getInt("hours_this_week"));
    driver.setMinutesThisYear(result.getInt("hours_this_year"));
    driver.setHolidaysTaken(result.getInt("holidays_taken"));
    driver.setName(result.getString("name"));
    return driver;
  }
  
  public static ArrayList<gmpte.entities.Driver> fetchDrivers(String[] where, String[] values, String[] orderBy, String[] order) {
    ArrayList<gmpte.entities.Driver> drivers = new ArrayList<gmpte.entities.Driver>();
    
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT * FROM driver");
    
    // handling WHERE
    if(where.length == values.length && where.length>0) {
      builder.append(" WHERE ");
      builder.append(where[0]+"="+values[0]);
      for(int i=1; i<where.length; i++) {
        builder.append(" AND ");
        builder.append(where[i]+"="+values[i]);
      }
    }
    
    if(orderBy.length==order.length && orderBy.length>0) {
      builder.append(" ORDER BY ");
      builder.append(orderBy[0]+" "+order[0]);
      for(int i=1; i<orderBy.length; i++) {
        builder.append(",");
        builder.append(" "+orderBy[i]+" "+order[i]);
      }
    }
    
    try {
      Connection connection = database.busDatabase.getConnection();
      PreparedStatement statement = connection.prepareStatement(builder.toString());
      ResultSet result = statement.executeQuery();
      while(result.next()) {
        drivers.add(buildDriver(result));
      }
    } catch (SQLException ex) {
      Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, builder.toString());
      Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return drivers;
  }

}
