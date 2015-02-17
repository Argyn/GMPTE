/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;

import gmpte.databaseinterface.DriverInfo;
import gmpte.databaseinterface.database;
import java.util.Date;

/**
 *
 * @author argyn
 */
public class HolidayController {
    
    public HolidayRequestResponse holidayRequest(HolidayRequest request) {
       /*
        boolean isGranted = true;
        int holidayCount = 0;

        Driver theDriver = request.getDriver();
        Date startDate = request.getStartDate();
        Date endDate = request.getEndDate();
        
        int driverNumber = theDriver.getDriverID();
        
        database.openBusDatabase();
        
        
        //the driver number should be a string
        int driverID = DriverInfo.findDriver(driverNumber);
        String driverName = DriverInfo.getName(driverID);
        int holidaysTaken = DriverInfo.getHolidaysTaken(driverID);
        
        for(Date date = startDate; date.compareTo(endDate) > 0; startDate++){
            if(DriverInfo,isAvailable(driverID, date))
                holidayCount++;
        }
        
        if(holidaysTaken + holidayCount > 25)
            isGranted = false;
        
        int driversAvailable[] = new int[holidayCount];
        
        int[] driverIDs = DriverInfo.getDrivers();
        

        for(Date date = startDate; date.compareTo(endDate) > 0; startDate++){
            int count = 0; 
            if(DriverInfo.isAvailable(driverID, date)){
                for(int j = 0; j < driverIDs.length; j++){
                    if(driverIDs[j] != driverID){
                        if(!DriverInfo.isAvailable(driverIDs[j], date))
                            count++;
                    }
                }
                if(count >= 10)
                    isGranted = false;
                
            }
        }
 
        
       
        
        if(isGranted){
            for(Date date = startDate; date.compareTo(endDate) > 0; startDate++){
                DriverInfo.setAvailable(driverID, false, date);
            }
            
            holidaysTaken += holidayCount;
            DriverInfo.setHolidaysTaken(driverID, holidaysTaken); 
            return new HolidayRequestResponse(HolidayRequestResponse.ResponseType.GRANTED);
        } else{
            return new HolidayRequestResponse(HolidayRequestResponse.ResponseType.NOT_GRANTED);
       } */
               
    return new HolidayRequestResponse(HolidayRequestResponse.ResponseType.GRANTED);
        
    }
}
