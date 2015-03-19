package gmpte.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class containing information about buses. 
 * The methods contain checks for invalid queries, which will result in
 * InvalidQueryExceptions being thrown, but it does not enforce "business
 * rules" such as checking for dates in the past.
 */
public class BusInfo 
{

  // This class is not intended to be istantiated.
  private BusInfo() 
  {
  }

  /**
   * Get the IDs of all the buses in the database
   */
  public static int[] getBuses()
  {
    return database.busDatabase.select_ids("bus_id", "bus", "number");
  }

  /**
   * Find the ID of the bus with a given fleet number
   * @param number the five-digit fleet  number traditionally used to identify
   * a bus
   */
  public static int findBus(String number)
  {
    return database.busDatabase.find_id("bus", "number", number);
  }

  /**
   * Get the number of a bus
   * @return The five-digit fleet number traditionally used to identify a bus
   */
  public static String busNumber(int bus)
  {
    if (bus == 0) throw new InvalidQueryException("Nonexistent bus");
    return database.busDatabase.get_string("bus", bus, "number");
  }

  /**
   * Determine whethe a bus is available on a given date
   */
  public static boolean isAvailable(int bus, Date date)
  {
    if (date == null) throw new InvalidQueryException("Date is null");
    if (bus  == 0   ) throw new InvalidQueryException("Nonexistent bus");
    database db = database.busDatabase;
    if (db.select_record("bus_availability", "bus", bus, "day", date))
      return ((Integer) db.get_field("available")) != 0;
    else
      return true;
  }

  /**
   * Determine whether a bus is available today
   */
  public static boolean isAvailable(int bus)
  {
    return isAvailable(bus, database.today());
  }

  /**
   * Set the availability of a bus on a given date. Availability is modelled
   * in the database as a list of dates on which a bus is not available,
   * so you can use any date you like, including dates in the past.
   */
  public static void setAvailable(int bus, Date date, boolean available)
  {
    if (date == null) throw new InvalidQueryException("Date is null");
    if (bus == 0) throw new InvalidQueryException("Bus " + bus + " does not exist");
    if (available && !isAvailable(bus, date))
      database.busDatabase.delete_record("bus_availability", "bus", bus, "day", date);
    else if (!available && isAvailable(bus, date))
      database.busDatabase.new_record("bus_availability", new Object[][]{{"available", false}, {"day", date}, {"bus", bus}});
  }
 
  /**
   * Set whether a bus is available today.
   */
  public static void setAvailable(int ID, boolean available) 
  {
    setAvailable(ID, database.today(), available);
  }
  
  public static gmpte.entities.Bus fetchBus(int busID) {
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT * FROM bus");
    builder.append(" WHERE bus_id=?");
    PreparedStatement statement;
    try {
      Connection connection = database.busDatabase.getConnection();
      statement = connection.prepareStatement(builder.toString());
      // setting the paratmeter - busID
      statement.setInt(1, busID);
      
      // exectuing the query
      ResultSet result = statement.executeQuery();
      if(result.next()) {
        gmpte.entities.Bus bus = new gmpte.entities.Bus(busID, result.getInt("number"));
        return bus;
      }
    } catch (SQLException ex) {
      Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return null;
  }
 
}
