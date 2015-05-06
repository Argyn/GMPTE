package gmpte.db;


import gmpte.entities.Area;
import gmpte.entities.BusStop;
import gmpte.entities.Route;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Information about bus stops. Real bus stops within the GMPTE area are
 * identified in a systematic way, but this is not the case for all the bus
 * stops within the scope of the the pilot IBMS as some of them are outside
 * the GMPTE area.<br><br>
 * 
 * Bus stops in the database are identified by an area and a name, 
 * e.g. "Hayfield, Bus Station" which is enough to uniqiely identify them.
 * Areas also have shorter codes, we.g. "HAY"<br><br>
 * 
 * Some bus stops are timing points, meaning that they appear on the timetable
 * associated with service times.<br><br>
 * 
 * None of the UCs in the pilot IBMS change bus stop information, so this
 * interface provides read-only access
 */
public class BusStopInfo 
{
  
  // This class is not intended to be instantiated
  private BusStopInfo()
  { 
  }

  /**
   * Get the IDs of all the routes within the scope of the pilot IBMS
   */
  public static int[] getRoutes()
  {
    return database.busDatabase.select_ids("route_id", "route", "name");
  }

  /**
   * Find a route with a given name. 
   * @param name, the name of a route within the scope of the IBMS.
   * These can be found using getRouteName of each of the route IDs given
   * by getAllRoutes
   */
  public static int findRoute(String name)
  {
    return database.busDatabase.find_id("route", "name", name);
  }

  /**
   * Get the name of a route
   * @return the name of one of the routes within the scope of the IBMS
   */
  public static String getRouteName(int route)
  {
    if (route == 0) throw new InvalidQueryException("Nonexistent route");
    return database.busDatabase.get_string("route", route, "name");
  }

  /**
   * Get the routes which visit a particular bus stop
   */
  public static int[] getRoutes(int busStop)
  {
    if (busStop == 0) throw new InvalidQueryException("Nonexistent bus stop");
    return database.busDatabase.select_ids("route", "path", "bus_stop", busStop, "");
  }

  /**
   * Get all the bus stops on the route which are in the database
   * This currently does not include intermediate stops between timing points
   */
  public static int[] getBusStops(int route)
  {
    if (route == 0) throw new InvalidQueryException("Nonexistent route");
    return database.busDatabase.select_ids("bus_stop", "path", "route", route, "sequence");
  }

  /**
   * Find a bus stop with a particular area code and name
   */
  public static int findBusStop(String areaCode, String name)
  {
    String source = database.join("bus_stop", "area", "area");
    return database.busDatabase.find_id("bus_stop_id", source, "area.code", areaCode, "bus_stop.name", name);
  }

  /**
   * Get the full name of a bus stop in the format 'areaname,stopname', eg
   * "Marple,Navigation"
   */
  public static String getFullName(int busStop)
  {
    if (busStop == 0) throw new InvalidQueryException("Nonexistent bus stop");
    database db = database.busDatabase;
    if (db.select_record("area.name, name", database.join("bus_stop", "area", "area"), "bus_stop_id", busStop))
      return (String)db.get_field("area.name") + ", " + (String)db.get_field("name");
    else
      return "";
  }

  /**
   * Get al the bus stops in a given area
   */
  public static int[] getBusStopsInArea(int area)
  {
    if (area == 0) throw new InvalidQueryException("Nonexistent area");
    return database.busDatabase.select_ids("bus_stop_id", "bus_stop", "area", area, "name");
  }

  /**
   * Get the next stop for a stop on a particular toute. Returns 0 if there
   * is no such stop.
   */
  public static int getNextStop(int busStop, int route)
  {
    if (busStop == 0) throw new InvalidQueryException("Nonexistent bus stop");
    if (route   == 0) throw new InvalidQueryException("Nonexistent route");
    database db       = database.busDatabase;
    String   source   = "path As path1 Inner Join path As path2 On (path1.sequence + 1 = path2.sequence)";
    String   criteria = "path1.route = " + route + " And path2.route = " + route + " And path1.bus_stop = " + busStop;
    db.select("path2.bus_stop", source, criteria, "");
    if (db.move_first()) return (Integer)db.get_field("path2.bus_stop"); else return 0;
  }

   /**
   * Get the previous stop for a stop on a particular toute. Returns 0 if there
   * is no such stop.
   */
  public static int getPreviousStop(int busStop, int route)
  {
    if (busStop == 0) throw new InvalidQueryException("Nonexistent bus stop");
    if (route   == 0) throw new InvalidQueryException("Nonexistent route");
    database db       = database.busDatabase;
    String   source   = "path As path1 Inner Join path As path2 On (path1.sequence - 1 = path2.sequence)";
    String   criteria = "path1.route = " + route + " And path2.route = " + route + " And path1.bus_stop = " + busStop;
    db.select("path2.bus_stop", source, criteria, "");
    if (db.move_first()) return (Integer)db.get_field("path2.bus_stop"); else return 0;
  }

  /**
   * Determine whethee a bus stop is a timing point somewhere on the timetable
   */
  public static boolean isTimingPoint(int busStop)
  {
    if (busStop == 0) throw new InvalidQueryException("Nonexistent bus stop");
    return database.busDatabase.record_count("*", "timetable_line", "timing_point", busStop) > 0;
  }

  /**
   * Determine whether a bus stop is a timeing point on the given route
   */
  public static boolean isTimingPointOnRoute(int busStop, int route)
  {
    if (busStop == 0) throw new InvalidQueryException("Nonexistent bus stop");
    if (route   == 0) throw new InvalidQueryException("Nonexistent route");
    String source = database.join("timetable_line", "daily_timetable", "daily_timetable");
    return database.busDatabase.record_count("*", source, "timetable_line.timing_point", busStop, "daily_timetable.route", route) > 0;
  }

  /**
   * Get the IDs of all the area codes
   */
  public static int[] getAreas()
  {
    return database.busDatabase.select_ids("area_id", "area", "name");
  }

  /**
   * Get the code for a given area, e.g. "MAR"
   */
  public static String getAreaCode(int area)
  {
    if (area == 0) throw new InvalidQueryException("Nonexistent area");
    return database.busDatabase.get_string("area", area, "code");
  }

   /**
   * Get the name for a given area, e.g. "Marple"
   */
  public static String getAreaName(int area)
  {
    if (area == 0) throw new InvalidQueryException("Nonexistent area");
    return database.busDatabase.get_string("area", area, "name");
  }

  /**
   * Get the ID of the area with the given code
   */
  public static int findArea(String code)
  {
    return database.busDatabase.find_id("area", "code", code);
  }

  /**
   * Get the ID of the area with the given name
   */
  public static int findAreaByName(String name)
  {
    return database.busDatabase.find_id("area", "name", name);
  }
  
  public static ArrayList<BusStop> getBusStopsByRoute(Route route) {
    
    ArrayList<BusStop> busStops = new ArrayList<>();
    
    String query = "SELECT p.bus_stop, p.sequence, b.area, b.name FROM path p "
                   + "LEFT JOIN bus_stop b ON b.bus_stop_id=p.bus_stop "
                   + "WHERE p.route=?";
    
    PreparedStatement statement;
    
    try {
      statement = prepareStatement(query);
      
      statement.setInt(1, route.getRouteID());
      
      ResultSet result = statement.executeQuery();
      
      while(result.next()) {
        busStops.add(resultToBusStop(result));
      }
      
      return busStops;
    } catch (SQLException ex) {
      Logger.getLogger(BusStopInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return null;
  }
  
  public static ArrayList<BusStop> getDistinctBusStopsByRoute(Route route) {
    
    ArrayList<BusStop> busStops = new ArrayList<>();
    
    String query = "SELECT DISTINCT b.area, b.name, 0 as sequence FROM path p "
                   + "LEFT JOIN bus_stop b ON b.bus_stop_id=p.bus_stop "
                   + "WHERE p.route=?";
    
    PreparedStatement statement;
    
    try {
      statement = prepareStatement(query);
      
      statement.setInt(1, route.getRouteID());
      
      ResultSet result = statement.executeQuery();
      
      while(result.next()) {
        busStops.add(resultToBusStop(result));
      }
      
      return busStops;
    } catch (SQLException ex) {
      Logger.getLogger(BusStopInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return null;
  }
  
  public static BusStop getBusStopsById(int busStopId) {
  
 
    String query = "SELECT area, name, 0 as sequence FROM bus_stop WHERE bus_stop_id=?";
    
    PreparedStatement statement;
    
    try {
      statement = prepareStatement(query);
      
      statement.setInt(1, busStopId);
      
      ResultSet result = statement.executeQuery();
      
      if(result.next()) {
        BusStop stop = resultToBusStop(result);
        stop.addId(busStopId);
        
        return stop;
      }

    } catch (SQLException ex) {
      Logger.getLogger(BusStopInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return null;
  }
  
  
  private static BusStop resultToBusStop(ResultSet result) throws SQLException {
    BusStop busStop = null;
    if(result!=null) {
      int areaID = result.getInt("area");
      int seq = result.getInt("sequence");
      String name = result.getString("name");
      busStop = new BusStop(AreaDBInfo.getAreaByID(areaID), name, seq);
    }
    
    return busStop;
  }
  
  private static BusStop resultToBusStop(ResultSet result, ArrayList<Area> areas) throws SQLException {
    BusStop busStop = null;
    if(result!=null) {
      
      int id = result.getInt("id");
      int areaID = result.getInt("area");
      int seq = result.getInt("sequence");
      String name = result.getString("name");
      
      int index = areas.indexOf(new Area(areaID, null));
      Area area = null;
      
      if(areas.get(index)!=null)
        area = areas.get(index);
      else {
        area = AreaDBInfo.getAreaByID(areaID);
      }
      busStop = new BusStop(area, name, seq);
      busStop.addId(id);
    }
    
    return busStop;
  }
  
  public static PreparedStatement prepareStatement(String query) throws SQLException {
    return database.busDatabase
              .getConnection().prepareStatement(query);
  }
  
  public static ArrayList<BusStop> getAllBusStops() {
    ArrayList<BusStop> busStops = new ArrayList<>();
    
    try {
      String query = "SELECT bus_stop_id as id, name, area, 0 as sequence FROM `bus_stop`";
      PreparedStatement statement = prepareStatement(query);
      
      ResultSet result = statement.executeQuery();
      
      while(result.next()) {
        BusStop bStop = resultToBusStop(result);
        
        if(busStops.contains(bStop))
          busStops.get(busStops.indexOf(bStop)).addId(bStop.getIds());
        else
          busStops.add(resultToBusStop(result));
      }
    } catch (SQLException ex) {
      Logger.getLogger(BusStopInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return busStops;
  }
  
  public static ArrayList<BusStop> getAllBusStops(ArrayList<Area> areas) {
    ArrayList<BusStop> busStops = new ArrayList<>();
    
    try {
      String query = "SELECT bus_stop_id as id, name, area, 0 as sequence FROM `bus_stop`";
      PreparedStatement statement = prepareStatement(query);
      
      ResultSet result = statement.executeQuery();
      
      while(result.next()) {
        BusStop bStop = resultToBusStop(result, areas);
        
        if(busStops.contains(bStop)) {
          BusStop originalB = busStops.get(busStops.indexOf(bStop));
          originalB.addId(bStop.getIds());
        } else {
          busStops.add(bStop);
        }
      }
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
      Logger.getLogger(BusStopInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return busStops;
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
  
  public static double getTimeBetweenBusStops(BusStop start, BusStop target, int kind) {
    String query = "SELECT AVG(diff) as avg FROM "
            + "(SELECT DISTINCT(abs(b1.time-b2.time)) as diff "
            + "FROM `bus_station_times` b1, bus_station_times b2, "
            + "daily_timetable d WHERE %s AND "
            + "%s AND b1.service=b2.service AND b2.timing_point-b1.timing_point=1 "
            + "AND b2.time-b1.time>0 "
            + "AND b1.daily_timetable=d.daily_timetable_id "
            + "AND d.kind=?) d3";
    
    query = String.format(query, buildTimingPoints(start, "b1"), 
                                buildTimingPoints(target, "b2"));
   
    
    PreparedStatement statement = null;
    try {
      statement = DBHelper.prepareStatement(query);
      
      
      // setting the kind
      statement.setInt(1, kind);
      
      ResultSet result = statement.executeQuery();
      if(result.next()) {
        return result.getDouble("avg");
      }
    } catch (SQLException ex) {
      System.out.println(statement.toString());
      Logger.getLogger(BusStopInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return -1;
  }
  
  
  
  
     
}
