/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.rostering;

import gmpte.Bus;
import gmpte.Driver;
import gmpte.Roster;
import gmpte.Schedule;
import gmpte.Service;
import gmpte.databaseinterface.BusInfo;
import gmpte.databaseinterface.DriverInfo;
import gmpte.databaseinterface.TimetableInfo;
import gmpte.databaseinterface.database;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

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
                weekdayServices.add(new Service(service));
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
        database.busDatabase.execute("UPDATE `driver` SET hours_this_week=0");
        // fetch all services
        fetchServices();
        
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
        
        
        // store the roster in the database
        TimetableInfo.storeSchedule(schedule);
        
        return new RosterGenerationResponse();
    }
    
    
    public void generateDayRoster(ArrayList<Service> services, int weekDay, Date date) {
        flashDriverServices();
        Iterator<Service> servicesIterator = weekdayServices.iterator();
        Iterator<Bus> busesIterator = buses.iterator();
        
        // generate roster for MONDAY-FRIDAY
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

            if(bus!=null && driver!=null) {
                Roster roster = new Roster(driver, bus, service, weekDay, date);
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
        
        //Date date = now.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        weekday = now.get(Calendar.DAY_OF_WEEK);
        
        while(weekday != Calendar.SATURDAY) {
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
    }
    
    public boolean driverCanTakeTheService(Service service, Driver driver) {
        Iterator<Service> assignedServicesIterator = 
                                        driver.getAssignedServices().iterator();
        while(assignedServicesIterator.hasNext()) {
            Service assignedService = assignedServicesIterator.next();
            if(TimetableInfo.checkServiceClashes(assignedService, service))
                return false;
        }
        
        return true;
    }
    
    public void flashDriverServices() {
        Iterator<Driver> driverIt = drivers.iterator();
        for(; driverIt.hasNext();) {
            driverIt.next().flashServices();
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
            if(driverCanTakeTheService(service, driver)) {
                if(driver.getHoursThisWeek() + service.getServiceLengthHours() <= 
                                                             HOURS_PER_WEEK_LIMIT) {
                    return driver;
                }
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
            System.out.println(it.next());
        }
        
        System.out.println("========== Saturday services =========");
        for(Iterator<Service> it=saturdayServices.iterator(); it.hasNext();) {
            // print the service
            System.out.println(it.next());
        }
        
        System.out.println("========== Sunday services =========");
        for(Iterator<Service> it=sundayServices.iterator(); it.hasNext();) {
            // print the service
            System.out.println(it.next());
        }
    } 
}
