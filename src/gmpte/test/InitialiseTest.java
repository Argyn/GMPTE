/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.test;

import gmpte.databaseinterface.DriverInfo;
import gmpte.databaseinterface.database;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author mbax2eu2
 */
public class InitialiseTest {
  
  
  private final static int testDriver1 = 2012;
  private final static int testDriver2 = 2013;
  private static Date exceedDate;
  
  
  private static void setDateLessThanReq(int[] driverIDs, Date date){
  
    for(int i = 0; i < 10; i++){
    
      int index = (int)(Math.random() * 50);
      DriverInfo.setAvailable(driverIDs[index], date, false);
      
      int holidaysTaken = DriverInfo.getHolidaysTaken(driverIDs[index]);
      holidaysTaken++;
      
      DriverInfo.setHolidaysTaken(driverIDs[index], holidaysTaken);
      
    }
  }
  
    private static void setDateLessThanReqSunday(int[] driverIDs, Date date){
  
    for(int i = 0; i < (int)(driverIDs.length * 0.25); i++){
    
      int index = (int)(Math.random() * driverIDs.length);
      DriverInfo.setAvailable(driverIDs[index], date, false);
      
      int holidaysTaken = DriverInfo.getHolidaysTaken(driverIDs[index]);
      holidaysTaken++;
      
      DriverInfo.setHolidaysTaken(driverIDs[index], holidaysTaken);
      
    }
  }
  
  public static void setHolidayLimitExceeded(int driverID, Date startDate){

      int holidayCount = 0;

      Date currentDate = startDate;
          
      Calendar currentCal = Calendar.getInstance();
      
      int holidaysTaken;

      do{
        DriverInfo.setAvailable(driverID, currentDate, false);
        
        holidaysTaken = DriverInfo.getHolidaysTaken(driverID);
        holidaysTaken++;
        DriverInfo.setHolidaysTaken(driverID, holidaysTaken);
          
        currentCal.setTime(currentDate);
        currentCal.add(Calendar.DATE, 1);
        currentDate = currentCal.getTime();   
            
      } while (holidaysTaken < 25);
      
  }
  
  public static void main(String args[]){
    
    Date exceededDate1 = null;
    Date exceededDate2 = null;
    Date exceedDate = null;
    Date sundayDate = null;
    Date sundayDate2 = null;
    
    database.openBusDatabase();
    int[] driverIDs = DriverInfo.getDrivers();
    
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    String exceededString1 = "2-Apr-2015";
    String exceededString2 = "10-Mar-2015";
    String exceedString = "12-Apr-2015";
    String sundayString = "1-Mar-2015";
    String sundayString2 = "7-Jun-2015";
            
	try {
        exceededDate1 = formatter.parse(exceededString1);
        exceededDate2 = formatter.parse(exceededString2);
        exceedDate = formatter.parse(exceedString);
        sundayDate = formatter.parse(sundayString);
        sundayDate2 = formatter.parse(sundayString2);
        
	} catch (ParseException e) {
		e.printStackTrace();
	}
    
    //setDateLessThanReq(driverIDs, exceededDate1);
    //setDateLessThanReq(driverIDs, exceededDate2);
    //setDateLessThanReq(driverIDs, sundayDate);
    //setDateLessThanReqSunday(driverIDs, sundayDate2);

    //setHolidayLimitExceeded(testDriver1, exceedDate);
    //setHolidayLimitExceeded(testDriver2, exceedDate);
    
    int theThing =(int) (driverIDs.length * 0.25);
    System.out.println(theThing);
    
  }
}
