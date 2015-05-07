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
  private Date date;
  
  public DelayCancelController (ArrayList<Service2> services, Date date)
  {
    this.services = services;  
    this.date = date;
  }
  
  public void run()
  {
    try
    {
      while (true)
      {
        Causes causes = new Causes();
     
        Random random = new Random();
        int randomWait = random.nextInt(60);
        int currentService = random.nextInt(services.size());
        System.out.println(randomWait);
        Thread.sleep(randomWait * 1000);
        if (random.nextInt(100) < 33)
        {    
          StringBuilder builder = new StringBuilder();
          builder.append("The " + services.get(currentService).getId() + " service has been cancelled ");
          builder.append("due to a " + causes.getRandomCause());
          builder.append(". We apologize for any inconvienience this causes.");
          String reason = builder.toString();
          System.out.println("canel " + services.get(currentService).getId());
          TimetableInfo.addNewCancel(6460, reason, date);
        }
        else
        {
          int delayTime = random.nextInt(55) + 5;
          StringBuilder builder = new StringBuilder();
          int delayPoint = random.nextInt(services.get(currentService).getBusStops().size());
          BusStop delayStop = services.get(currentService).getBusStops().get(delayPoint);
          builder.append("The " + services.get(currentService).getId() + " service is delayed by approximately ");
          builder.append(delayTime + " minutes due to a " + causes.getRandomCause());
          builder.append(", and will arrive  at " + services.get(currentService).getBusStops().get(delayPoint + 1).getName());
          builder.append(" at approximately " + services.get(currentService).getDelayTime());
          builder.append(". We apologize for the delay to your journey.");
          String reason = builder.toString();
          System.out.println("delay " + services.get(currentService).getId() + " of " + delayTime);
          TimetableInfo.addNewDelay(6460, reason, date, delayTime, delayStop.getSequence());
        } // else
      }
    } catch (InterruptedException ex) { /* do nothing */}
  }
}
