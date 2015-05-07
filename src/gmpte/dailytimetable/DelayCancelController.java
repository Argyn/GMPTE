/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.dailytimetable;

import gmpte.db.TimetableInfo;
import gmpte.entities.BusStop;
import gmpte.entities.Service2;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mbax3jw3
 */
public class DelayCancelController implements Runnable 
{
  private ArrayList<Service2> services; 
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
      
      while (true)
      { 
        Date date = new Date();
        int randomWait = random.nextInt(60);
        for (minServiceIndex = 0; minServiceIndex < services.size(); minServiceIndex++)
        {
          Service2 currentService = services.get(minServiceIndex);
          if (currentService.getTimes().get(currentService.getTimes().size()).compareTo(date) == -1)
          {
            break;
          }
        }
        int currentService = random.nextInt(services.size() - minServiceIndex) + minServiceIndex;
        Service2 service = services.get(currentService);
        System.out.println(randomWait);
        if (random.nextInt(100) < 33)
        {    
          StringBuilder builder = new StringBuilder();
          builder.append("The " + service.getId() + " service has been cancelled ");
          builder.append("due to a " + causes.getRandomCause());
          builder.append(". We apologize for any inconvienience this causes.");
          String reason = builder.toString();
          System.out.println("canel " + service.getId());
          TimetableInfo.addNewCancel(service.getId(), reason, date);
        }
        else
        {
          int minDelayPoint = 0;
          int delayTime = random.nextInt(55) + 5;
          
          for (minDelayPoint = 0; minDelayPoint < service.getBusStops().size(); minDelayPoint++)
          {
            Date currentStop = service.getTimes().get(minDelayPoint);
            if (currentStop.compareTo(date) == -1)
            {
              break;
            }
          }
          StringBuilder builder = new StringBuilder();
          int delayPoint = random.nextInt(service.getBusStops().size() - minDelayPoint) + minDelayPoint;
          BusStop delayStop = service.getBusStops().get(delayPoint);
          builder.append("The " + service.getId() + " service is delayed by approximately ");
          builder.append(delayTime + " minutes due to a " + causes.getRandomCause());
          builder.append(", and will arrive  at " + service.getBusStops().get(delayPoint + 1).getName());
          builder.append(" at approximately " + service.getDelayTime());
          builder.append(". We apologize for the delay to your journey.");
          String reason = builder.toString();
          System.out.println("delay " + service.getId() + " of " + delayTime);
          TimetableInfo.addNewDelay(service.getId(), reason, date, delayTime, delayStop.getSequence());
        } // else
        Thread.sleep(randomWait * 1000);
      }
    } catch (InterruptedException ex) { /* do nothing */}
  }
}
