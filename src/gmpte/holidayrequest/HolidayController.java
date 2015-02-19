/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.holidayrequest;

import gmpte.holidayrequest.HolidayRequest;
import gmpte.databaseinterface.DriverInfo;
import gmpte.databaseinterface.database;
import gmpte.Driver;
import gmpte.GMPTEConstants;
import java.util.Date;
import java.util.Calendar;

/**
 *
 * @author argyn
 */
public class HolidayController {

	/*
     * This method counts the number of days the
     * driver requested for holiday except for his/her
	 * rest days
	 */
    public static int numberOfRequestedHolidays(Date startDate,
                                                Date endDate,int driverID){

      int holidayCount = 0;

      Date currentDate = startDate;
      Date afterEndDate = endDate;
          
      Calendar currentCal = Calendar.getInstance();
      currentCal.setTime(afterEndDate);
      currentCal.add(Calendar.DATE, 1);
      afterEndDate = currentCal.getTime(); 

      do{
        if(DriverInfo.isAvailable(driverID, currentDate)){
          holidayCount++;
        }

        currentCal.setTime(currentDate);
        currentCal.add(Calendar.DATE, 1);
        currentDate = currentCal.getTime();   
            
      } while (currentDate.compareTo(afterEndDate) != 0);
      holidayCount -= ((holidayCount / 7) * 2);
      return holidayCount;
    }

    /*
     * If the requested holidays are granted
     * this method sets those days unavailable and
     * increments the drivers holiday count
     */
    public static void setDatesUnavailable(Date startDate,
                                           Date endDate, int driverID,
                                           int holidaysTaken){

      Date currentDate = startDate;
      Date afterEndDate = endDate;

      Calendar currentCal = Calendar.getInstance();
      currentCal.setTime(afterEndDate);
      currentCal.add(Calendar.DATE, 1);
      afterEndDate = currentCal.getTime(); 

      do{

      DriverInfo.setAvailable(driverID, currentDate, false);

      holidaysTaken++;

      currentCal.setTime(currentDate);
      currentCal.add(Calendar.DATE, 1);
      currentDate = currentCal.getTime();   

      }while (currentDate.compareTo(afterEndDate) != 0);
      
      holidaysTaken -= ((holidaysTaken / 7) * 2);
      DriverInfo.setHolidaysTaken(driverID, holidaysTaken);

	}

	/*
     * This method checks if there are enough drivers
     * within the range that the driver requested holiday
     * returns false if there aren't enough drivers
	 */
	public static boolean ifMoreThanRequired(Date startDate, Date endDate,
                                             int driverID){

      int[] driverIDs = DriverInfo.getDrivers();
      int count;
      boolean ifSunday = false;

      Date currentDate = startDate;
      Date afterEndDate = endDate;

      Calendar currentCal = Calendar.getInstance();
      currentCal.setTime(afterEndDate);
      currentCal.add(Calendar.DATE, 1);
      afterEndDate = currentCal.getTime(); 

      do{
        count = 0;
        
        currentCal.setTime(currentDate);
        if(currentCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
          ifSunday = true;
        
        if(DriverInfo.isAvailable(driverID, currentDate)){
          for(int j = 0; j < driverIDs.length; j++){
              if(!DriverInfo.isAvailable(driverIDs[j], currentDate))
                count++;
          }
        }
        
        if(!ifSunday && count >= 10)
          return true;
        else if(ifSunday && count >= (int)(driverIDs.length * 0.25))
           return true;
          
        currentCal.add(Calendar.DATE, 1);
        currentDate = currentCal.getTime();   

      } while (currentDate.compareTo(afterEndDate) != 0);

      return false;
	}	

    /*
    * This method decides whether the driver can
	* request holidays within the given range
    */
	public static HolidayRequestResponse ifGranted(Date startDate,
                                                   Date endDate,                                                       int driverID,
                                                   boolean moreThanReqPeople,
                                                   boolean holidayLimitExceeded,
                                                   int holidaysTaken){
		
      if(!moreThanReqPeople && !holidayLimitExceeded){
        
        setDatesUnavailable(startDate, endDate, driverID, holidaysTaken);
        return new HolidayRequestResponse(HolidayRequestResponse.ResponseType.GRANTED);
      
      } else if(!moreThanReqPeople && holidayLimitExceeded)
        return new HolidayRequestResponse(HolidayRequestResponse.ResponseType.NOT_GRANTED, GMPTEConstants.REQUEST_EXCEEDS_25_DAYS);
      else
        return new HolidayRequestResponse(HolidayRequestResponse.ResponseType.NOT_GRANTED, GMPTEConstants.REQUEST_MORE_THAN_REQ_PEOP);
    
  
	}
    
    public HolidayRequestResponse holidayRequest(HolidayRequest request) {
       
        boolean holidayLimitExceeded = false;
        boolean moreThanReqPeople = false;
        int holidayCount;

        Driver theDriver = request.getDriver();
        int driverID = theDriver.getDriverID();
        Date startDate = request.getStartDate();
        Date endDate = request.getEndDate();
        
        int holidaysTaken = DriverInfo.getHolidaysTaken(driverID);
        

        holidayCount = numberOfRequestedHolidays(startDate, endDate, driverID);
        
        
        if((holidaysTaken + holidayCount) >= 25){
          holidayLimitExceeded = true;
        }

        moreThanReqPeople = ifMoreThanRequired(startDate, endDate, driverID);

        return ifGranted(startDate, endDate, driverID,
        								 moreThanReqPeople, holidayLimitExceeded,
        								 holidaysTaken);
    }
}
