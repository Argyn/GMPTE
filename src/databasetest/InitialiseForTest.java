/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package databasetest;


import gmpte.databaseinterface.*;
import java.util.Date;

/**
 *
 * @author mbax2eu2
 */
public class InitialiseForTest {
    
    
    public static void makeDriversAvailable(int[] driverIDs
                                           , Date startDate, Date endDate){
        
    //for each driver set them available between start date
    //and end date
        
    }
    
    public static void setRestDays(int[] driverIDs, Date startDate
                                   , Date endDate){
    //for each driver set two days each week as rest week
        
    }
    
    public static void main(String args[]){
        database.openBusDatabase();
        int[] driverIDs = DriverInfo.getDrivers();
        
        //find a range where you want to test the results
        Date startDate;
        Date endDate;
        
        
        //makeDriversAvailable(driverIDs, startDate, endDate);
        //setRestDays(driverIDs, startDate, endDate);
    }
    
    
}
