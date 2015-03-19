/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.rostering;

import gmpte.db.BusInfo;
import gmpte.db.DriverInfo;
import gmpte.db.TimetableInfo;
import gmpte.db.database;
import gmpte.entities.Bus;
import gmpte.entities.Driver;
import gmpte.entities.Roster;
import gmpte.entities.Route;
import gmpte.entities.Schedule;
import gmpte.entities.Service;
import gmpte.helpers.DBHelper;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
/**
 *
 * @author mbgm2rm2
 */
public class RosterController {
    
    private ArrayList<Service> weekdayServices;
    private ArrayList<Service> saturdayServices;
    private ArrayList<Service> sundayServices;
    private ArrayList<Driver> drivers;
    private ArrayList<Bus> buses;
    private Schedule schedule;
    private Task t;
    private Date fromDate;
    private Date toDate;
    
    public static final int HOURS_PER_WEEK_LIMIT = 50;
    
    public ArrayList<Integer> routes;
    
    public RosterController() {
        weekdayServices = new ArrayList<Service>();
        saturdayServices = new ArrayList<Service>();
        sundayServices = new ArrayList<Service>();
        drivers = new ArrayList<Driver>();
        buses = new ArrayList<Bus>();
        
        schedule = new Schedule();
        
        routes = new ArrayList<Integer>();
    }
    
    public void addRoute(int routeID) {
        routes.add(routeID);
    }
    
    private void fetchServices() {
        Integer[] routeIds = new Integer[routes.size()];
        routeIds = routes.toArray(routeIds);
        
        for(int route : routeIds) {
            int[] servicesIds;
            
            // fetching services for weekdays
            servicesIds = TimetableInfo.getServices(route, TimetableInfo.timetableKind.weekday);

            for(int service : servicesIds) {
                if(service!=0)
                  weekdayServices.add(new Service(service));
                else
                  System.out.println("0 encountered");
            }
            
            // fetching services for saturdays
            servicesIds = TimetableInfo.getServices(route, TimetableInfo.timetableKind.saturday);

            for(int service : servicesIds) {
                saturdayServices.add(new Service(service));
            }
            
            // fetching services for sundays
            servicesIds = TimetableInfo.getServices(route, TimetableInfo.timetableKind.sunday);

            for(int service : servicesIds) {
                sundayServices.add(new Service(service));
            }
        }
    }
    
    private void fetchDrivers() {
        int[] driverIds = DriverInfo.getDrivers();
        int driverNumber;
        for(int driverID : driverIds) {
            driverNumber = Integer.parseInt(DriverInfo.getNumber(driverID));
            drivers.add(new Driver(driverID, driverNumber));
        }
    }
    
    private void fetchBuses() {
        int[] busesIds = BusInfo.getBuses();
        int busNumber;
        for(int busID : busesIds) {
            busNumber = Integer.parseInt(BusInfo.busNumber(busID));
            buses.add(new Bus(busID, busNumber));
        }
    }
    
    public RosterGenerationResponse generateRoster() {
      
        toDate = null;
        fromDate = null;
        
        // all drivers now get 0 hours driven
        database.busDatabase.execute("UPDATE `driver` SET hours_this_week=0");
        
        // truncate roster table
        database.busDatabase.execute("TRUNCATE TABLE `roster`");
        
        // fetch all services
        fetchServices();
        //printServices();
        
        
        // fetch all driver
        fetchDrivers();
        
        // fetch all buses
        fetchBuses();
        
        
        // generate roster for week days
        generateWeekdaysRoster();
       
        // generate roster for saturdays
        generateSaturdayRoster();
        
        // generate roster for sundays
        generateSundayRoster();
                
        try {
          // store the roster in the database
          //TimetableInfo.storeSchedule(schedule);
          DBHelper.insertRoster(schedule);
        } catch (SQLException ex) {
          Logger.getLogger(RosterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        
        return new RosterGenerationResponse(fromDate, toDate);
    }
    
    
    public void generateDayRoster(ArrayList<Service> services, int weekDay, Date date) {
        flashDriverServices();
        Iterator<Service> servicesIterator = services.iterator();
        Iterator<Bus> busesIterator = buses.iterator();
        
        for(; servicesIterator.hasNext();) {
            
            Service service = servicesIterator.next();
            
            // for each service

            /* Choosing the driver */
            Driver driver = chooseDriver(service);
            if(driver!=null) {
                // increase the number of hours driver has drover
                driver.increaseHoursThisWeek(service.getServiceLengthHours());
                // update databsae information
                driver.dbUpdateHoursThisWeek();

                // update driver info in the database
            } else {
                System.out.println("NO DRIVER HAS BEEN CHOOSEN");
            }
            
            
            Bus bus = null;
            /* Choosing the bus */
            if(busesIterator.hasNext()) {
                // there is a bus for this service
                bus = busesIterator.next();
            }
            
            Route route = new Route(service.getRoute());
            if(bus!=null && driver!=null) {
                Roster roster = new Roster(driver, bus, route, service, weekDay, date);
                schedule.addRoster(roster);
            } 
        }
    }
    
    public void generateWeekdaysRoster() {        
        Calendar now = Calendar.getInstance();
        
        int weekday = now.get(Calendar.DAY_OF_WEEK);
        if (weekday != Calendar.MONDAY) {
            // calculate how much to add
            // the 2 is the difference between Saturday and Monday
            int days = (Calendar.SATURDAY - weekday + 2) % 7;
            now.add(Calendar.DAY_OF_YEAR, days);
        }

        // from date update
        fromDate = now.getTime();
        weekday = now.get(Calendar.DAY_OF_WEEK);
        
        while(weekday != Calendar.SATURDAY) {
            System.out.println(weekday);
            // generate for each day MONDAY-FRIDAY
            generateDayRoster(weekdayServices, weekday, now.getTime());
            
            now.add(Calendar.DAY_OF_YEAR, 1);
            weekday = now.get(Calendar.DAY_OF_WEEK);
        }
    }
    
    public void generateSaturdayRoster() {
        Calendar now = Calendar.getInstance();
        
        int weekday = now.get(Calendar.DAY_OF_WEEK);
        
        if (weekday != Calendar.MONDAY) {
            // calculate how much to add
            // the 2 is the difference between Saturday and Monday
            int days = (Calendar.SATURDAY - weekday + 2) % 7;
            now.add(Calendar.DAY_OF_YEAR, days);
        }
        
        now.add(Calendar.DAY_OF_YEAR, 5);
        weekday = now.get(Calendar.DAY_OF_WEEK);
        
        if(weekday==Calendar.SATURDAY) {
            // generating the roster for saturdays
            generateDayRoster(saturdayServices, weekday, now.getTime());
        }
    }
    
    public void generateSundayRoster() {
        Calendar now = Calendar.getInstance();
        int weekday = now.get(Calendar.DAY_OF_WEEK);
        if (weekday != Calendar.MONDAY) {
            // calculate how much to add
            // the 2 is the difference between Saturday and Monday
            int days = (Calendar.SATURDAY - weekday + 2) % 7;
            now.add(Calendar.DAY_OF_YEAR, days);
        }
        now.add(Calendar.DAY_OF_YEAR, 6);
        weekday = now.get(Calendar.DAY_OF_WEEK);
        
        if(weekday==Calendar.SUNDAY) {
            // generating the roster for saturdays
            generateDayRoster(sundayServices, weekday, now.getTime());
        }
        
        // toDate update
        toDate = now.getTime();
    }
    
    public void flashDriverServices() {
        Iterator<Driver> driverIt = drivers.iterator();
        for(; driverIt.hasNext();) {
            driverIt.next().flashShift();
        }
    }
    public Driver chooseDriver(Service service) {
        // sort drivers
        Collections.sort(drivers);
        Iterator<Driver> it = drivers.iterator();
        Driver driver;
        
        for(; it.hasNext();) {
            driver = it.next();
            
            // first check there are no clashes of this services with other
            // services this driver has been assigned to
            if(driver.getHoursThisWeek() + service.getServiceLengthHours() <= 
                                                         HOURS_PER_WEEK_LIMIT
                && driver.isAbleToTakeService(service)) {
                driver.assignToService(service);
                return driver;
            }
        }
        
        return null;
    }
    
    public void printBuses() {
        for(Iterator<Bus> it=buses.iterator(); it.hasNext();) {
            System.out.println(it.next());
        }
    }
    
    public void printDrivers() {
        Iterator<Driver> driverIt = drivers.iterator();
        while(driverIt.hasNext()) {
            System.out.println(driverIt.next());        
        }
    }
    
    public void printServices() {
        System.out.println("========== Weekday services =========");
        for(Iterator<Service> it=weekdayServices.iterator(); it.hasNext();) {
            // print the service
            System.out.println(it.next().getRoute());
        }
        
        System.out.println("========== Saturday services =========");
        for(Iterator<Service> it=saturdayServices.iterator(); it.hasNext();) {
            // print the service
            System.out.println(it.next().getRoute());
        }
        
        System.out.println("========== Sunday services =========");
        for(Iterator<Service> it=sundayServices.iterator(); it.hasNext();) {
            // print the service
            System.out.println(it.next().getRoute());
        }
    } 
}
