/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.test;
import gmpte.Service;
import gmpte.databaseinterface.database;
import gmpte.databaseinterface.TimetableInfo;
/**
 *
 * @author mbax3jw3
 */
public class testService 
{
  public static void main(String args[]) 
  {
    System.out.println("Normal service");
    Service testServiceOne = new Service(6529);
    System.out.println("service Id = " + testServiceOne.getServiceId());
    System.out.println("dailyTimetableId = " + testServiceOne.getDailyTimetableId());
    System.out.println("route = " + testServiceOne.getRoute());
    System.out.println("length = " + testServiceOne.getServiceLength());
    
    System.out.println("\nNormal service");
    Service testServiceTwo = new Service(6178);
    System.out.println("service Id = " + testServiceTwo.getServiceId());
    System.out.println("dailyTimetableId = " + testServiceTwo.getDailyTimetableId());
    System.out.println("route = " + testServiceTwo.getRoute());
    System.out.println("length = " + testServiceTwo.getServiceLength()); 
    
    database.openBusDatabase();
    boolean clashCheck = TimetableInfo.checkServiceClashes(testServiceOne, testServiceTwo);
    System.out.println("clashCheck = " + clashCheck);
      
    /*System.out.println("\nMidnight service");
    Service testServiceThree = new Service(6237);
    System.out.println("service Id = " + testServiceThree.getServiceId());
    System.out.println("dailyTimetableId = " + testServiceThree.getDailyTimetableId());
    System.out.println("route = " + testServiceThree.getRoute());
    System.out.println("length = " + testServiceThree.getServiceLength()); 
    
    System.out.println("\nMidnight service");
    Service testServiceFour = new Service(6237);
    System.out.println("service Id = " + testServiceFour.getServiceId());
    System.out.println("dailyTimetableId = " + testServiceFour.getDailyTimetableId());
    System.out.println("route = " + testServiceFour.getRoute());
    System.out.println("length = " + testServiceFour.getServiceLength()); 
    
    System.out.println("\nNormal service");
    Service testServiceFive = new Service(6381);
    System.out.println("service Id = " + testServiceFive.getServiceId());
    System.out.println("dailyTimetableId = " + testServiceFive.getDailyTimetableId());
    System.out.println("route = " + testServiceFive.getRoute());
    System.out.println("length = " + testServiceFive.getServiceLength()); */
  }
}
