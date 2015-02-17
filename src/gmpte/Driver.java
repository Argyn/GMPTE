/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;

/**
 *
 * @author argyn
 */
public class Driver {
    private final int driverNumber;
    
    private final int driverID;
    
    private String name;
    
    private int holidaysTaken;
    
    private int hoursThisYear;
    
    private int hoursThisWeek;
    
    private boolean available;
    
    public Driver(int driverID, int driverNumber) {
        this.driverID = driverID;
        this.driverNumber = driverNumber;
        available = false;
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
    
    public int getHoursThisWeek() {
        return hoursThisWeek;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    
}
