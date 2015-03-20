/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

import java.util.Iterator;
import gmpte.db.DriverInfo;
import gmpte.db.TimetableInfo;

/**
 *
 * @author argyn
 */
public class Driver implements Comparable<Driver> {
    
    private final int driverNumber;
    
    private final int driverID;
    
    private String name;
    
    private int holidaysTaken;
    
    private int minutesThisYear;
    
    private int minutesThisWeek;
    
    private boolean available;
    
    private Shift shift;
    
    private int daysWorked;
    
    public Driver(int driverID, int driverNumber) {
        this.driverID = driverID;
        this.driverNumber = driverNumber;
        available = false;
        daysWorked = 0;
        
        flashShift();
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
    
    public void setMinutesThisYear(int minutesThisYear) {
        this.minutesThisYear = minutesThisYear;
    }
    
    public int getMinutesThisYear() {
        return minutesThisYear;
    }
    
    public void setMinutesThisWeek(int minutesThisWeek) {
        this.minutesThisWeek = minutesThisWeek;
    }
    
    public double getMinutesThisWeek() {
        return minutesThisWeek;
    }
    
    public void increaseMitnuesThisWeek(int minutesIncrease) {
      this.setMinutesThisWeek(minutesThisWeek+minutesIncrease);
      this.setMinutesThisYear(minutesThisYear+minutesIncrease);
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public void flashShift() {
        if(shift!=null && shift.getAssignedServices().size()>0) {
          daysWorked++;
        }
        
        shift = new Shift();
    }
    
    public boolean assignToService(Service service) {
      
      boolean output = false;
      //if(driverNumber==8213)
      //  output = true;
      
      return shift.addService(service, output);
    }
    
    public Shift getShift() {
        return shift;
    }
    
    public void dbUpdateHours() {
        DriverInfo.setHoursThisWeek(driverID, minutesThisWeek);
        DriverInfo.setHoursThisYear(driverID, minutesThisYear);
    }
    
    public boolean isAbleToTakeService(Service service) {
      return !doesServiceClash(service) && daysWorked<5; 
    }
    
    private boolean doesServiceClash(Service service) {
        Iterator<Service> assignedServicesIterator = shift.getAssignedServices().iterator();
        while(assignedServicesIterator.hasNext()) {
            Service assignedService = assignedServicesIterator.next();
            if(TimetableInfo.checkServiceClashes(assignedService, service)) {
                return true;
            }
        }
        return false;
    }
    
    
    
    @Override
    public int compareTo(Driver other) {
      if(minutesThisWeek > other.minutesThisWeek)
        return -1;
      else if(minutesThisWeek == other.minutesThisWeek)
        return 0;
      return 1;
    }
    
    @Override
    public String toString() {
        return name+"(ID:"+driverNumber+")";
    }
}
