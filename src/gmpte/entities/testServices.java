/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.entities;
import gmpte.db.TimetableInfo;
import gmpte.db.database;
import java.util.ArrayList;

/**
 *
 * @author mbax3jw3
 */
public class testServices 
{
  public static void main(String args[]) {
  database.openBusDatabase();
  // create new service 6460 has a lot of gaps in its timing points
  Service serviceOne = new Service(6460);

  System.out.println(serviceOne + " " + serviceOne.getStartTime() + " " + serviceOne.getEndTime());
  serviceOne.introduceDelay(3, 50, "a shortage of water for the windscreen wipers");
  if (serviceOne.isDelayed())
    System.out.println(serviceOne.getDelayMessage());
  else
    System.out.println("not delayed");
  System.out.println(serviceOne + " " + serviceOne.getStartTime() + " " + serviceOne.getEndTime());
  serviceOne.cancel("a shortage of water for the windscreen wipers");
  if (serviceOne.isCancelled())
    System.out.println(serviceOne.getCancelMessage());
  else
    System.out.println("not cancelled");
  
  //printing start and end times
  System.out.println("serviceOne " + serviceOne.getStartTime() + " " + serviceOne.getEndTime());
  }
}
