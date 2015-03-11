package gmpte.databaseinterface;
import java.util.*;
import static java.util.Calendar.*;

/**
 * A class providing information about timetables. This is given in a low-level
 * manner, e.g. with arrays numbers representing service times. The IBMS
 * itself should implement sensible classes to represent this information
 * in a more high-level intuitive way.<br><br>
 * 
 * A Service identifies a row of the timetable. The times for each service,
 * along with the timing point information in the BusStopInfo class provide
 * all the data in the timetable. 
 * 
 * None of the UCs in the pilot IBMS change timetable information, so this
 * interface provides read-only access
 */
public class TimetableInfo
{
  private static Calendar calendar = new GregorianCalendar();

  /**
   * The kinds of timetale available. Sunday services run on bank holidays.
   */
  public enum timetableKind {weekday, saturday, sunday};

  // This class is not intended to be instantiated
  private TimetableInfo() 
  { 
  }

  /**
   * Find the route with a given name. Route names can be found via the
   * BusStopInfo.getRouteName() method
   */
  public static int findRoute(String name)
  {
    return database.busDatabase.find_id("route", "name", name);
  }

  /**
   * get a timetable id for a given service ID
   * @param serviceId
   * @return 
   */
  public static int getDailyTimetableId(int serviceId)
  {
    if (serviceId == 0) throw new InvalidQueryException("Nonexistent service");
    return database.busDatabase.find_id("service", serviceId, "daily_timetable");
  }
  
  public boolean checkServiceClashes(int serviceOneId, int ServiceTwoId)
  {
    // just removing the cases where some route times start before and end after
    // midnight by just adding the time past midnight to the hours in the day
    int[] serviceOneTimes = getRouteTimes(serviceOneId);
    if (serviceOneTimes[serviceOneTimes.length - 1] < serviceOneTimes[0])
      serviceOneTimes[serviceOneTimes.length - 1] = 1440 + serviceOneTimes[serviceOneTimes.length - 1];
    int[] serviceTwoTimes = getRouteTimes(ServiceTwoId);  
    if (serviceTwoTimes[serviceTwoTimes.length - 1] < serviceTwoTimes[0])
      serviceTwoTimes[serviceTwoTimes.length - 1] = 1440 + serviceTwoTimes[serviceTwoTimes.length - 1];

    if (serviceTwoTimes[serviceTwoTimes.length - 1] > serviceOneTimes[0] &&
        serviceTwoTimes[serviceTwoTimes.length - 1] < serviceOneTimes[serviceOneTimes.length - 1])
      return false;
    if (serviceTwoTimes[0] > serviceOneTimes[0] &&
        serviceTwoTimes[0] < serviceOneTimes[serviceOneTimes.length - 1])
      return false;
    if (serviceTwoTimes[0] < serviceOneTimes[0] &&
        serviceTwoTimes[serviceTwoTimes.length - 1] > serviceOneTimes[serviceOneTimes.length - 1])
      return false;
    return true;
  }
  
  public static int getRouteId(int dailyTimetableId)
  {
    if (dailyTimetableId == 0) throw new InvalidQueryException("Nonexistent timetable");
    return database.busDatabase.find_id("daily_timetable", dailyTimetableId, "route");
  }
  
  public static int getRouteLength(int serviceId)
  {
    if (serviceId == 0) throw new InvalidQueryException("Nonexistent service");
    int[] timingPoints = getRouteTimes(serviceId);
    for (int index = 0; index < timingPoints.length; index++)
      System.out.println(timingPoints[index]);
    if (timingPoints[timingPoints.length - 1] < timingPoints[0])
      return (1440 - timingPoints[0]) + timingPoints[timingPoints.length - 1];
    return timingPoints[timingPoints.length - 1] - timingPoints[0];
  }
  
  public static int[] getRouteTimes(int serviceId)
  {
    if (serviceId == 0) throw new InvalidQueryException("Nonexistent service");
    return database.busDatabase.select_ids("time", "timetable_line", "service", serviceId, "");
  }
  
  /**
   * Get the timing points on a given route
   */
  public static int[] getTimingPoints(int route)
  {
    if (route == 0) throw new InvalidQueryException("Nonexistent route");
    String source = database.join("timetable_line", "daily_timetable", "daily_timetable");
    return database.busDatabase.select_ids("Distinct timing_point", source, "daily_timetable.route", route, "time");
  }

  /**
   * Get the number of services on a route for the given timetable kind:
   * This will be more on weekdays than Sundays, for example.
   */
  public static int getNumberOfServices(int route, timetableKind kind)
  {
    if (route == 0) throw new InvalidQueryException("Nonexistent route");
    String source = database.join("service", "daily_timetable", "daily_timetable");
    return database.busDatabase.record_count("*", source, "daily_timetable.route", route, "daily_timetable.kind", kind.ordinal());
  }

  /**
   * Get all the services in a route for the given timetable kind.
   */
  public static int[] getServices(int route, timetableKind kind)
  {
    if (route == 0) throw new InvalidQueryException("Nonexistent route");
    String source = database.join("service", "daily_timetable", "daily_timetable");
    return database.busDatabase.select_ids("service_id", source, "daily_timetable.route", route, "daily_timetable.kind", kind.ordinal(), "");
  }
  
  /**
   * Get the number of services on a given route and data
   */
  public static int getNumberOfServices(int route, Date date)
  {
    return getNumberOfServices(route, timetableKind(date));
  }
  
   /**
   * Get the number of services on a given route today
   */
  public static int getNumberOfServices(int route)
  {
    return getNumberOfServices(route, database.today());
  }
  
  /**
   * Get the service times on a given route for a given timetable kind
   * for a particular service. This method along with the methods to
   * get service information can be used to build up a complete timetable
   */
  public static int[] getServiceTimes(int route, timetableKind kind, int serviceNumber)
  {
    if (route == 0) throw new InvalidQueryException("Nonexistent route");
    int[] service_ids      = getServices(route, kind);
    int   numberOfServices = service_ids.length;
    if (serviceNumber < 0 || serviceNumber >=  numberOfServices) throw new InvalidQueryException("Invalid service number " + serviceNumber);
    int service = service_ids[serviceNumber];
    String source = database.join("timetable_line", "service", "service");
    return database.busDatabase.select_ids("time", source, "service", service, "time");
  }
  
  /**
   * Get the service times on a given route for a givrn date
   * for a particular service. This method along with the methods to
   * get service information can be used to build up a complete timetable
   */
  public static int[] getServiceTimes(int route, Date date, int serviceNumber)
  {
    return getServiceTimes(route, timetableKind(date), serviceNumber);
  }
     
   /**
   * Get the service times on a given route today
   * for a particular service.
   */
  public static int[] getServiceTimes(int route, int serviceNumber)
  {
    return getServiceTimes(route, new Date(), serviceNumber);
  }

  /**
   * Get the timetable kind for a day
   */
  public static timetableKind timetableKind(Date day)
  {
    calendar.setTime(day);
    switch (calendar.get(Calendar.DAY_OF_WEEK))
    {
      case Calendar.SATURDAY: return timetableKind.saturday;
      case Calendar.SUNDAY:   return timetableKind.sunday;
      default:                return timetableKind.weekday;
    }
  }

}
