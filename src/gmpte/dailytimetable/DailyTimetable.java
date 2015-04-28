package gmpte.dailytimetable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import gmpte.db.TimetableInfo;
import gmpte.entities.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author mbax2eu2
 */
public class DailyTimetable 
{
    private ArrayList<Integer> dailyTimeTableIds;
    private ArrayList<Service> services;
    private Date date;
    private int dayId;
    
    public DailyTimetable(Date date)
    {
      this.date = date;
      dayId = 0;
      getDayId(date);
      ArrayList<Integer> serviceIds = new ArrayList<>();
      services = new ArrayList<>();
      dailyTimeTableIds = TimetableInfo.getDailyTimetableIds(dayId);
      serviceIds = TimetableInfo.getLineServices(dailyTimeTableIds);
      Iterator<Integer> iterator = serviceIds.iterator();
      while (iterator.hasNext())
      {
        int serviceId = iterator.next();
        Service newService = new Service(serviceId);
        services.add(newService);
      } // while
      imposeDelays(); 
      imposeCancelations();
    } // DailyTimetable
    
    public void getDayId(Date date)
    {      
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);   
      if (dayOfWeek == Calendar.SUNDAY)
        dayId = 2;
      else if (dayOfWeek == Calendar.SATURDAY)
        dayId = 1;
      else
        dayId = 0;
    } // getDayId
    
    public ArrayList<Integer> getDailyTimeTableIds()
    {
      return dailyTimeTableIds;
    }
    
    public ArrayList<Service> getServices()
    {
      return services;
    }
    
    public Date getDate()
    {
      return date;
    }
    
    public int getDayId()
    {
      return dayId;
    }
    
    private void imposeCancelations()
    {    
      Random random = new Random();
      int servicesSize = services.size();
      int numberOfCancels = random.nextInt(servicesSize / 10);
      int serviceIndex;
      Causes reasons = new Causes();
      for (int index = 0; index < numberOfCancels; index++)
      {
        serviceIndex = random.nextInt(servicesSize);
        services.get(serviceIndex).cancel(reasons.getRandomCause());
      } // for
    }

    private void imposeDelays()
    {
      Random random = new Random();
      int servicesSize = services.size();
      int numberOfDelays = random.nextInt(servicesSize / 10);
      int serviceIndex;
      int stopIndex;
      int delay;
      Causes reasons = new Causes();
      for (int index = 0; index < numberOfDelays; index++)
      {
        serviceIndex = random.nextInt(servicesSize);
        stopIndex = random.nextInt(services.get(serviceIndex).getServiceTimingPoints().size() - 1);
        delay = random.nextInt(60);
        services.get(serviceIndex).introduceDelay(stopIndex, delay, reasons.getRandomCause());
      } // for
    }
    
   /* public void addtimetableNode(DailyTimetableNode node) {
        timetable.add(node);
    }
    
    public ArrayList<DailyTimetableNode> getTimetable() {
        return timetable;
    } */
    
} // DailyTimetable