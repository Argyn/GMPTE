/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;

import java.util.ArrayList;
import gmpte.databaseinterface.DriverInfo;

/**
 *
 * @author argyn
 */
public class Driver implements Comparable<Driver> {
    
    private final int driverNumber;
    
    private final int driverID;
    
    private String name;
    
    private int holidaysTaken;
    
    private int hoursThisYear;
    
    private int hoursThisWeek;
    
    private boolean available;
    
    private ArrayList<Service> assignedServices;
    
    public Driver(int driverID, int driverNumber) {
        this.driverID = driverID;
        this.driverNumber = driverNumber;
        available = false;
        flashServices();
    }
    
    public int getDriverID() {
        return driverID;
    }
    
    public int getDriverNumber() {
        return driverNumber;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setHolidaysTaken(int holidaysTaken) {
        this.holidaysTaken = holidaysTaken;
    }
    
    
    public int getHolidaysTaken() {
        return holidaysTaken;
    }
    
    public void setHoursThisYear(int hoursThisYear) {
        this.hoursThisYear = hoursThisYear;
    }
    
    public int getHoursThisYear() {
        return hoursThisYear;
    }
    
    public void setHoursThisWeek(int hoursThisWeek) {
        this.hoursThisWeek = hoursThisWeek;
    }
    
    public double getHoursThisWeek() {
        return hoursThisWeek;
    }
    
    public void increaseHoursThisWeek(int hoursIncrease) {
      setHoursThisWeek(hoursThisWeek+hoursIncrease);
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public void flashServices() {
        assignedServices = new ArrayList<Service>();
    }
    
    public void assignToService(Service service) {
        assignedServices.add(service);
    }
    
    public ArrayList<Service> getAssignedServices() {
        return assignedServices;
    }
    
    public void dbUpdateHoursThisWeek() {
        DriverInfo.setHoursThisWeek(driverID, hoursThisWeek);
    }
    
    @Override
    public int compareTo(Driver other) {
      if(hoursThisWeek > other.hoursThisWeek)
        return -1;
      else if(hoursThisWeek == other.hoursThisWeek)
        return 0;
      return 1;
    }
    
    public String toString() {
        return "DriverID"+driverID+", DriverNumber: "+driverNumber+", HoursThisWeek:"+hoursThisWeek;
    }
}
