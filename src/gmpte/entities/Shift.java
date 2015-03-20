/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;

import gmpte.GMPTEConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author mbgm2rm2
 */
public class Shift {
    public ArrayList<Service> services;
    public ArrayList<TimeShift> shiftTimes;
    public int lastEndTime;
    public boolean breakTaken;
    
    public Shift() {
        services = new ArrayList<Service>();
        shiftTimes = new ArrayList<TimeShift>();
        breakTaken = false;
    }
    
    public void updateData() {
      Collections.sort(shiftTimes);
    }
    
    public boolean satisfy(boolean output) {
      updateData();
      Iterator<TimeShift> timeShiftsIt = shiftTimes.iterator();
      
      TimeShift firstShiftService = null;
      int totalTimeWorked = 0;
      int totalShiftTime;
      
      if(timeShiftsIt.hasNext()) {
        firstShiftService = timeShiftsIt.next();
        if(output) {
          System.out.println("First time:"+firstShiftService.getStartTime());
        }
      }
        
      TimeShift next;
      TimeShift prev = firstShiftService;
      
      while(timeShiftsIt.hasNext()) {
        next = timeShiftsIt.next();
        
        if(output)
          System.out.println("End time:"+next.getEndTime());
                  
        totalShiftTime = next.getEndTime()-firstShiftService.getStartTime();
        
        if(next.getStartTime()-prev.getEndTime()>=60) {
          firstShiftService = next;
          totalTimeWorked += totalShiftTime;
          totalShiftTime = 0;
          breakTaken = true;
          if(output)
            System.out.println("Taking the break");
        }
        
        if(output) {
          System.out.println("Total time worked:"+(totalTimeWorked+totalShiftTime));
        }
        // do not allow to work more than 10hours a day(600 minutes)
        if(totalTimeWorked+totalShiftTime>GMPTEConstants.MAX_MINUTES_PER_DAY 
                          || totalShiftTime>GMPTEConstants.MAX_SHIFT_DURATION) {
          if(output) {
            System.out.println("Exceeded maxiumin shift time or timework limit");
          }
          return false;
        }
        
        prev = next;
      }
      
      return true;
    }
    public boolean addService(Service service, boolean output) {
      if(output)
        System.out.println("Service:"+service.getServiceId());
      TimeShift tsh = new TimeShift(service.getStartTime(), service.getEndTime());
      shiftTimes.add(tsh);
      
      if(!satisfy(output)) {
        shiftTimes.remove(tsh);
        
        if(output)
          System.out.println("Service:"+service.getServiceId()+" Does not satisfy");
        return false;
      }
      
      services.add(service);
      if(output)
          System.out.println("Service:"+service.getServiceId()+" satisfies");
      return true;
    }
    
    public ArrayList<Service> getAssignedServices() {
        return services;
    }
    
    
}
