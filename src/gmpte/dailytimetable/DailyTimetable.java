package gmpte.dailytimetable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import gmpte.entities.Service;
import java.util.ArrayList;
import java.util.Date;
import gmpte.db.TimetableInfo;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.text.ParseException;

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
      dailyTimeTableIds = TimetableInfo.getDailyTimetableIds(dayId);
      serviceIds = TimetableInfo.getLineServices(dailyTimeTableIds);
      Iterator<Integer> iterator = serviceIds.iterator();
      while (iterator.hasNext())
      {
        int serviceId = iterator.next();
        Service newService = new Service(serviceId);
        services.add(newService);
      } // while
      imposeCancelations();
      imposeDelays(); 
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
      
    }

    private void imposeDelays()
    {
      
    }
    
   /* public void addtimetableNode(DailyTimetableNode node) {
        timetable.add(node);
    }
    
    public ArrayList<DailyTimetableNode> getTimetable() {
        return timetable;
    } */
    
} // DailyTimetable