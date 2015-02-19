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
  
  
  private static Date startDate;
  private static Date endDate;
  private final static int testDriver1 = 2012;
  private final static int testDriver2 = 2013;
  private static Date exceedDate;
  
  /*
   * This method sets all the drivers available 
   * between dates startDate and endDate
  */
  private static void setDriversAvailable(int[] driverIDs){
          
    Date currentDate = startDate;
    Date afterEndDate = endDate;

    Calendar currentCal = Calendar.getInstance();
    currentCal.setTime(afterEndDate);
    currentCal.add(Calendar.DATE, 1);
    afterEndDate = currentCal.getTime(); 

    do{
      
      for(int i = 0; i < driverIDs.length; i++){
        DriverInfo.setAvailable(driverIDs[i], currentDate, true);
      }
     
      currentCal.setTime(currentDate);
      currentCal.add(Calendar.DATE, 1);
      currentDate = currentCal.getTime();   

      }while (currentDate.compareTo(afterEndDate) != 0);
  }
  
  /*
  private static void setRestingDays(int[] driverIDs){
    
    int count = 1;
        
    Date currentDate = startDate;
    Date afterEndDate = endDate;

    Calendar currentCal = Calendar.getInstance();
    currentCal.setTime(afterEndDate);
    currentCal.add(Calendar.DATE, 1);
    afterEndDate = currentCal.getTime(); 

      
    do{
        
        for(int i = 0; i < driverIDs.length; i++){
          switch(i % 7){
            case 0: DriverInfo.setAvailable(driverIDs[i], currentDate, false);
                    break;
            case 1: DriverInfo.setAvailable(driverIDs[i], currentDate, false);
                    break;
            case 2: DriverInfo.setAvailable(driverIDs[i], currentDate, false);
                    break;
            case 3: DriverInfo.setAvailable(driverIDs[i], currentDate, false);
                    break;
            case 4: DriverInfo.setAvailable(driverIDs[i], currentDate, false);
                    break;
            case 5: DriverInfo.setAvailable(driverIDs[i], currentDate, false);
                    break; 
            case 6: DriverInfo.setAvailable(driverIDs[i], currentDate, false);
                    break;
          }
        }
        
        currentCal.setTime(currentDate);
        currentCal.add(Calendar.DATE, 1);
        currentDate = currentCal.getTime(); 
        
        count++;

    }while (currentDate.compareTo(afterEndDate) != 0);
    
  }
  
  
  */
  
  private static void setDateLessThanReq(int[] driverIDs, Date date){
  
    for(int i = 0; i < 10; i++){
    
      int index = (int)(Math.random() * 50);
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

      do{
        if(DriverInfo.isAvailable(driverID, currentDate)){
          DriverInfo.setAvailable(driverID, currentDate, false);
        
          int holidaysTaken = DriverInfo.getHolidaysTaken(driverID);
          holidaysTaken++;
          DriverInfo.setHolidaysTaken(driverID, holidaysTaken);
          
          holidayCount++;
          
        }

        currentCal.setTime(currentDate);
        currentCal.add(Calendar.DATE, 1);
        currentDate = currentCal.getTime();   
            
      } while (holidayCount == 24);
      
    }
  
  public static void main(String args[]){
    
    Date exceededDate1 = null;
    Date exceededDate2 = null;
    Date exceedDate = null;
    
    database.openBusDatabase();
    int[] driverIDs = DriverInfo.getDrivers();
    
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	String startString = "01-Mar-2015";
    String endString = "30-Jun-2015";
    String exceededString1 = "2-Apr-2015";
    String exceededString2 = "10-Mar-2015";
    String exceedString = "12-Apr-2015";
            
	try {
 
		startDate = formatter.parse(startString);
        endDate = formatter.parse(endString);
        exceededDate1 = formatter.parse(exceededString1);
        exceededDate2 = formatter.parse(exceededString2);
        exceedDate = formatter.parse(exceedString);
        
	} catch (ParseException e) {
		e.printStackTrace();
	}
    
    setDriversAvailable(driverIDs);
    //setRestingDays(driverIDs);
    
    setDateLessThanReq(driverIDs, exceededDate1);
    setDateLessThanReq(driverIDs, exceededDate2);
    
    setHolidayLimitExceeded(testDriver1, exceedDate);
    setHolidayLimitExceeded(testDriver2, exceedDate);

    
  }
}
