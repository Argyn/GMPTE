/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

import java.util.ArrayList;

/**
 *
 * @author mbgm2rm2
 */
public class Shift {
    public ArrayList<Service> services;
    
    // the time of the current shift
    public int shiftTime;
    
    // how long the driver has actually worked in that shift
    public int workedTime;
    
    public int startTime;
    
    public int endTime;
    
    // as there can be multiple shifts in this class, this stores the total worked time
    public int totalWorkedTime;
    
    public Shift() {
        services = new ArrayList<Service>();
        shiftTime = 0;
        startTime = 0;
        totalWorkedTime = 0;
    }
    
    // will return true if the service has been added to a shift, false if the
    // service cannot be added.
    public boolean addService(Service service) {
        /*boolean added = false;
        // check if the driver has already worked 10 hours today
        if (totalWorkedTime < 600)
        {
          /* check if this will be the first service assigned to the driver or
             check if there is suffienct time between the new service and the
             current shift for the driver to take a break */
          /*if(services.size()==0 || (service.getStartTime() - endTime >=60)) {
              services.add(service);
              workedTime+=service.getServiceLengthMinutes();
              totalWorkedTime+=service.getServiceLengthMinutes();
              startTime = service.getStartTime();
              endTime = service.getEndTime();
              // if a bus passes midnight add its value to the number of mins in
              // day
              if(startTime > endTime)
                  endTime+=1440;
              added = true;
          }
          // checks the 5 hour before break and 10 hour max constraints
          else if(isAbleToTakeService(service)) {
              services.add(service);
              workedTime+=service.getServiceLengthMinutes();
              totalWorkedTime+=service.getServiceLengthMinutes();             
              
              int tempEndTime = service.getEndTime();
              if(startTime > tempEndTime)
                  tempEndTime+=1440;
              endTime = tempEndTime;
              
              added = true;
          }
        
          shiftTime = endTime-startTime;
        }
        
        return added;*/
      services.add(service);
      return true;
    }
    /* check if adding the service to the shift will make it exceed the max
       time for a driver to work before taking a break and if adding the
       service will make the time that driver has worked greater than his
       max working time*/    
    public boolean isAbleToTakeService(Service service) {
        if(services.size()==0)
            return true;
        
        int tempEndTime = service.getEndTime();
        
        // if a bus passes midnight add its value to the number of mins in a day
        if(startTime > tempEndTime)
            tempEndTime+=1440;
        
        return (tempEndTime-startTime <=300) && ((totalWorkedTime + (tempEndTime - endTime)) < 600);
    }
    
    public ArrayList<Service> getAssignedServices() {
        return services;
    }
    
    
}
