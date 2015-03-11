/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.test;
import gmpte.Service;
/**
 *
 * @author mbax3jw3
 */
public class testService 
{
  public static void main(String args[])
  {
    Service testService = new Service(6527);
    System.out.println("service Id = " + testService.getServiceId());
    System.out.println("dailyTimetableId = " + testService.getDailyTimetableId());
  }
}
