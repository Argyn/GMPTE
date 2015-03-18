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
    
    public int shiftTime;
    
    public int workedTime;
    
    public int startTime;
    
    public int endTime;
    
    public Shift() {
        services = new ArrayList<Service>();
        shiftTime = 0;
        startTime = 0;
    }
    
    public boolean addService(Service service) {
        // first service
        boolean added = false;
        
        if(services.size()==0 || (service.getStartTime() - endTime >=60)) {
            services.add(service);
            workedTime+=service.getServiceLengthMinutes();
            // calculate shift time

            startTime = service.getStartTime();
            endTime = service.getEndTime();
            if(startTime > endTime)
                endTime+=1440;
            
            added = true;
        } else if(isAbleToTakeService(service)) {
            services.add(service);
            workedTime+=service.getServiceLengthMinutes();
            
            int tempEndTime = service.getEndTime();
            if(startTime > tempEndTime)
                tempEndTime+=1440;
            endTime = tempEndTime;
            
            added = true;
        }
        
        shiftTime = endTime-startTime;
        
        return added;
    }
    
    public boolean isAbleToTakeService(Service service) {
        if(services.size()==0)
            return true;
        
        int tempEndTime = service.getEndTime();
        
        if(startTime > tempEndTime)
            tempEndTime+=1440;
        
        return tempEndTime-startTime <=300;
    }
    
    public ArrayList<Service> getAssignedServices() {
        return services;
    }
    
    
}
