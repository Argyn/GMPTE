package gmpte.db;
import java.util.*;
import static java.util.Calendar.*;
import gmpte.entities.*;
import java.sql.SQLException;
import java.util.ArrayList;

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

    // returns TRUE if there is a clash and false if there isn't
  public static boolean checkServiceClashes(Service serviceOne, Service serviceTwo)
  {   

    if (serviceTwo.getEndTime() > serviceOne.getStartTime() && serviceTwo.getEndTime() <= serviceOne.getEndTime())
        return true;
    if (serviceTwo.getStartTime() >= serviceOne.getStartTime() && serviceTwo.getStartTime() < serviceOne.getEndTime())
        return true;
    if (serviceTwo.getStartTime() <= serviceOne.getStartTime() && serviceTwo.getEndTime() >= serviceOne.getEndTime())
        return true;
    return false;
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
    if (timingPoints[timingPoints.length - 1] < timingPoints[0])
      return (1440 - timingPoints[0]) + timingPoints[timingPoints.length - 1];
    return timingPoints[timingPoints.length - 1] - timingPoints[0];
  }
  
  public static void storeNewRoster(Roster roster)
  {
    if (roster == null) throw new InvalidQueryException("Roster is null");
      database.busDatabase.new_record("roster", new Object[][]{{"driver", roster.getDriver().getDriverID()}, {"bus", roster.getBus().getBusId()}, {"service", roster.getService().getServiceId()}, {"day", roster.getDay()}, {"timeWorked", roster.getServiceTime()}, {"date", roster.getDate()}});
  }
  
  public static void storeSchedule(Schedule schedule)
  {
    ArrayList<Roster> rosters;
    rosters = schedule.getRosters();
    Iterator<Roster> iterator = rosters.iterator();
    for(; iterator.hasNext();)
        storeNewRoster(iterator.next());
  }
  
  public static void getStartEndTimes(Service service)   throws SQLException
  {
    int[] timingPoints = getServiceTimingPoints(service.getServiceId());
    int[] times = getRouteTimes(service.getServiceId());
    int[] pathPoints = getRoutePath(service.getRoute());
    
    // if the first timing poing is equals to start point of the path(route)
    // and end timing point is seqials to end point of the root
    // then only set start time to corresponding first and last points
    if (timingPoints[0] == pathPoints[0] && timingPoints[timingPoints.length - 1] == pathPoints[pathPoints.length - 1])
    {
      service.setStartTime(times[0]);
      service.setEndTime(times[times.length - 1]);
    }
    else
    {
      int[] completeServices = findCompleteServices(service.getRoute());  
      int serviceStart;
      int serviceEnd;
      boolean foundMatchingTime = false;
      int serviceIndex;
      int serviceTimePoint = 0;
      int completeServiceTimePoint = 0;
      int[] completeTimingPoints = null;
      for (serviceIndex = 0; serviceIndex < completeServices.length; serviceIndex++)
      {
        completeTimingPoints = getServiceTimingPoints(completeServices[serviceIndex]);
        for (serviceTimePoint = 0; serviceTimePoint < timingPoints.length; serviceTimePoint++)    
        {
          for (completeServiceTimePoint = 0; completeServiceTimePoint < completeTimingPoints.length; completeServiceTimePoint++)    
          {
            if (timingPoints[serviceTimePoint] == completeTimingPoints[completeServiceTimePoint])
            {
              foundMatchingTime = true;
              break;
            }
          }
          if (foundMatchingTime)
            break;        
        }
        if (foundMatchingTime)
          break;
      }
      if (foundMatchingTime)
      {
        int[] completeTimes = getRouteTimes(completeServices[serviceIndex]);
        int timeDifference = completeTimes[completeServiceTimePoint] - completeTimes[0];
        serviceStart = times[serviceTimePoint] - timeDifference;
        if (completeTimes[completeTimingPoints.length - 1] < completeTimes[0])
            completeTimes[completeTimingPoints.length - 1] += 1440;
        serviceEnd = serviceStart + (completeTimes[completeTimingPoints.length - 1] - completeTimes[0]);
        service.setStartTime(serviceStart);
        service.setEndTime(serviceEnd);
      }
      if (service.getRoute() == 67)
        addDriverToServiceTime(service);
      else if (service.getRoute() == 68)
        addDriverToStation(service);
    }
  }
  
  public static void addDriverToServiceTime(Service service)  throws SQLException
  {
    int startTime = service.getStartTime();
    int timeDifference = 0;
    BusReserve busTimes = getServiceToStation(790, 793);
    if (busTimes.getEndTime() < busTimes.getStartTime())
      busTimes.setEndTime(busTimes.getEndTime() + 1440);
    timeDifference = busTimes.getEndTime() - busTimes.getStartTime();
    service.setStartTime(startTime - timeDifference);
  }
  
  public static void addDriverToStation(Service service)  throws SQLException
  {
    int endTime = service.getEndTime();
    int timeDifference = 0;
    BusReserve busTimes = getServiceToStation(814, 817);
    System.out.println(busTimes.getStartTime());
    System.out.println(busTimes.getEndTime());
    if (busTimes.getEndTime() < busTimes.getStartTime())
      busTimes.setEndTime(busTimes.getEndTime() + 1440);
    timeDifference = busTimes.getEndTime() - busTimes.getStartTime();
    service.setEndTime(endTime + timeDifference);
  }
  
  public static BusReserve getServiceToStation(int stationPoint, int timingPoint) throws SQLException
  {
    return database.busDatabase.get_bus_times(stationPoint, timingPoint);
  }
  
  public static int[] getServiceFromTimingPoint(int timingPoint)
  {
    return database.busDatabase.select_ids("service", "timetable_line", "timing_point", timingPoint, "");
  }
   
  /*public static Schedule getDriverRoster(Driver driver)
  {
    int driverId = driver.getDriverID();
    System.out.println(driverId);
    int[] busIds = getDriverBuses(driverId);
    int[] serviceIds = getDriverServices(driverId);
    int[] days = getDriverDays(driverId);
    int[] times = getDriverTimes(driverId);
    Date[] dates = getDriverDates(driverId);
    Schedule schedule = new Schedule();
    
    Bus[] busAray = new Bus[busIds.length];
    Service[] serviceArray = new Service[busIds.length];
    Roster driverRoster;
    for (int index = 0; index < busIds.length; index++)
    {
      int busNumber = Integer.parseInt(BusInfo.busNumber(busIds[index]));
      busAray[index] = new Bus(busIds[index], busNumber);
      serviceArray[index] = new Service(serviceIds[index]);
   /*   driverRoster.setDriver(driver);
      driverRoster.setBus(busAray[index]);
      driverRoster.setService(serviceArray[index]);
      driverRoster.setDay(days[index]);
      driverRoster.setTime(times[index]); 
      driverRoster = new Roster(driver, busAray[index], serviceArray[index], days[index], times[index], dates[index]);
      schedule.addRoster(driverRoster);
    }
    return schedule;
  }*/
      
  public static int[] getDriverBuses(int driverId)
  {
    if (driverId == 0) throw new InvalidQueryException("Nonexistent driver"); 
    return database.busDatabase.select_ids("bus", "roster", "driver", driverId, "");
  }
  
  public static int[] getDriverServices(int driverId)
  {
    if (driverId == 0) throw new InvalidQueryException("Nonexistent driver"); 
    return database.busDatabase.select_ids("service", "roster", "driver", driverId, "");
  }
  
  public static int[] getDriverDays(int driverId)
  {
    if (driverId == 0) throw new InvalidQueryException("Nonexistent driver"); 
    return database.busDatabase.select_ids("date", "roster", "driver", driverId, "");
  }
  
  public static int[] getDriverTimes(int driverId)
  {
    if (driverId == 0) throw new InvalidQueryException("Nonexistent driver"); 
    return database.busDatabase.select_ids("timeWorked", "roster", "driver", driverId, "");
  }
  
  public static Date[] getDriverDates(int driverId)
  {
    if (driverId == 0) throw new InvalidQueryException("Nonexistent driver"); 
    return database.busDatabase.select_dates("date", "roster", "driver", driverId, "");
  }
  
  public static int getNewLength(int routeId)
  {
    if (routeId == 0) throw new InvalidQueryException("Nonexistent route");
    int serviceId = findCompleteService(routeId);
    return getRouteLength(serviceId);
  }
  
  // finds a service that actually has timing points that match the start and end
  // points of a route
  public static int findCompleteService(int routeId)
  {
    if (routeId == 0) throw new InvalidQueryException("Nonexistent route"); 
    int[] services = getServices(routeId);
    int[] routeTimes;
    int[] routePath = getRoutePath(routeId);    
    for (int index = 0; index < services.length; index++)
    {
      routeTimes = getServiceTimingPoints(services[index]);
      if ((routePath[0] == routeTimes[0]) && (routePath[routePath.length - 1] == routeTimes[routeTimes.length - 1]))
        return services[index];
    } // for
    return 0;
  }
  
    // finds a service that actually has timing points that match the start and end
  // points of a route
  public static int[] findCompleteServices(int routeId)
  {
    if (routeId == 0) throw new InvalidQueryException("Nonexistent route"); 
    int[] services = getServices(routeId);
    int[] completeServices = new int[services.length];
    int serviceCount = 0;
    int[] routeTimes;
    int[] routePath = getRoutePath(routeId);    
    for (int index = 0; index < services.length; index++)
    {
      routeTimes = getServiceTimingPoints(services[index]);
      if ((routePath[0] == routeTimes[0]) && (routePath[routePath.length - 1] == routeTimes[routeTimes.length - 1]))
      {
        completeServices[serviceCount] = services[index];
        serviceCount++;
      } // if
    } // for
    int[] shortenedCompleteServices = new int[serviceCount];
    for (int index = 0; index < serviceCount; index++)
    {
      shortenedCompleteServices[index] = completeServices[index];
    }
    return shortenedCompleteServices;
  }
  
  public static int[] getServiceTimingPoints(int serviceId)
  {
    if (serviceId == 0) throw new InvalidQueryException("Nonexistent service");
    return database.busDatabase.select_ids("timing_point", "timetable_line", "service", serviceId, "");
  }
  
  public static int[] getRoutePath(int routeId)
  {
    if (routeId == 0) throw new InvalidQueryException("Nonexistent route"); 
    return database.busDatabase.select_ids("Distinct bus_stop", "path", "route", routeId, "sequence");
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
   * Get all the services in a route.
   */
  public static int[] getServices(int routeId)
  {
    if (routeId == 0) throw new InvalidQueryException("Nonexistent route");
    String source = database.join("service", "daily_timetable", "daily_timetable");
    return database.busDatabase.select_ids("service_id", source, "daily_timetable.route", routeId, "");
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
  
  public static int[] getRosterBus(int driverId)
  {
    if (driverId == 0) throw new InvalidQueryException("Nonexistent driver");
    return database.busDatabase.select_ids("bus", "roster", "driver", driverId, "");
  } // getRosterBus
  
  public static int[] getRosterService(int driverId)
  {
    if (driverId == 0) throw new InvalidQueryException("Nonexistent driver");
    return database.busDatabase.select_ids("service", "roster", "driver", driverId, "");
  } // getRosterService
  
  public static int[] getRosterDay(int driverId)
  {
    if (driverId == 0) throw new InvalidQueryException("Nonexistent driver");
    return database.busDatabase.select_ids("day", "roster", "driver", driverId, "");
  } // getRosterDay
  
  public static int[] getTimeWorked(int driverId)
  {
    if (driverId == 0) throw new InvalidQueryException("Nonexistent driver");
    return database.busDatabase.select_ids("timeWorked", "roster", "driver", driverId, "");
  } // getRosterDay
}
