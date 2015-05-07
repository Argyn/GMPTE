/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.dailytimetable;

import gmpte.db.TimetableInfo;
import gmpte.entities.BusStop;
import gmpte.entities.Service2;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author mbax3jw3
 */
public class DelayCancelController implements Runnable 
{
  private ArrayList<Service2> services; 
  private ArrayList<Service2> currentServices;
  private boolean running = true;
  
  public DelayCancelController (ArrayList<Service2> services)
  {
    this.services = services;  
  }
  
  public void run()
  {
    try
    {
      Causes causes = new Causes();    
      Random random = new Random();
      int minServiceIndex = 0;
      
      Date nowDate = null;
      int randomWait = 0;
      int size = 0;
      int randomServiceIndex = 0;
      
      Date firstServiceTime = null;
      Date lastServiceTime = null;
      
      while(running) {
        randomWait = random.nextInt(60);
        
        //sleep
        Thread.sleep(randomWait * 1000);
        
        nowDate = new Date();
        
        currentServices = new ArrayList<>();
        for(Service2 service : services) {
          size = service.getTimes().size();
          firstServiceTime = service.getTimes().get(0);
          lastServiceTime = service.getTimes().get(size-1);
          
          if(firstServiceTime.before(nowDate) && lastServiceTime.after(nowDate) 
                            && !service.isDelayed() && !service.isCancelled()) {
            currentServices.add(service);
          }  
        }
        
        // choosing random service to cancel
        if(currentServices.size()==0)
          continue;
        
        randomServiceIndex = random.nextInt(currentServices.size());
        Service2 delayCancelService = currentServices.get(randomServiceIndex);
        
        if(random.nextInt(4)==0) {
          StringBuilder builder = new StringBuilder();
          builder.append("due to a " + causes.getRandomCancel());
          builder.append(". We apologize for any inconvienience this causes.");
          String reason = builder.toString();
          System.out.println("cancel " + delayCancelService.getId());
          TimetableInfo.addNewCancel(delayCancelService.getId(), reason, nowDate);
          
          delayCancelService.setCancelled(reason);
        } else {
          BusStop delayStop = null;
          int delayTime = random.nextInt(20) + 5;
          
          int index = 0;
          for(Date delayPoint : delayCancelService.getTimes()) {
            if(delayPoint.after(nowDate) && delayStop==null) {
              delayStop = delayCancelService.getBusStops().get(index);
              break;
            }
            index++;
          }
          
          StringBuilder builder = new StringBuilder();
          builder.append(delayTime + " minutes due to a " + causes.getRandomDelay());
          builder.append(", and will arrive  at " + delayStop);
          builder.append(" at approximately " + delayCancelService.getDelayTime());
          builder.append(". We apologize for the delay to your journey.");
          String reason = builder.toString();
          System.out.println("delay " + delayCancelService.getId() + " of " + delayTime);
          TimetableInfo.addNewDelay(delayCancelService.getId(), reason, nowDate, delayTime, delayStop.getIds().get(0));
          
          delayCancelService.setDelayedTime(delayTime, delayStop.getIds().get(0), reason);
          System.out.println(running);
        }
        
        // sleep
        
      }
    }  catch (InterruptedException ex) { 
      Thread.currentThread().interrupt();//preserve the message
      running = false;
      return;
    }  
  }
  
  public void interrupt()
  {
    running = false;
    Thread.currentThread().interrupt();//preserve the message
    return;
  }
}
