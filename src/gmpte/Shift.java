/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;
import java.util.*;

/**
 *
 * @author mbax3jw3, mbax2mf2
 */
/*public class Shift 
{
  private Service[] services;
  private int shiftStart;
  private int shiftEnd;
  // waiting time
  private int waitingTime;
  
  public Shift(Service[] services, boolean[] serviceTaken)
  {
    for (int index = 0; index < services.length; index++)
      if (!serviceTaken[index]);
        
    // assign first available service to shift
    // thats start becomes the shift start
    // if service has no clash with any previous service
    // allocate service and set its availability to 0;
 
  }
}
*/
public class Shift implements Comparable<Shift>
{
  private int driverID;
  private int busID;
  private int shiftDuration;
  private Strech firstStrech, secondStrech;
  private Date date;

  public Shift() {
  }

  public int getDriverID()
  {
    return driverID;
  } // getDriverID


  public int getBusID()
  {
    return busID;
  } // getBusID


  public Strech getFirstStrech()
  {
    return firstStrech;
  } // getFirstStrech


  public Strech getSecondStrech()
  {
    return secondStrech;
  } // getSecondStrech


  public void setDriverID(int driver)
  {
    driverID = driver;
  } // getDriverID


  public void setBusID(int bus)
  {
    busID = bus;
  } // getBusID


  public int getShiftDuration()
  {
    return shiftDuration;
  } // getShiftDuration


  public int getNumberOfServices()
  {
    return firstStrech.getNumberOfServices()
           + secondStrech.getNumberOfServices();
  } // getNumberOfServices

  public Date getDate() {
    return date;
  }

  public Shift(Strech first, Strech second, Date day)
  {
    firstStrech  = first;
    secondStrech = second;
    date = day;
    shiftDuration = second.getStrechDuration() + first.getStrechDuration();
  } // constructor


  @Override
  public int compareTo(Shift other)
  {
    return this.getShiftDuration() - other.getShiftDuration();
  } // compareTo
  

  @Override
  public String toString()
  {    
    return "\nDRIVER: " + driverID + " on BUS: " + busID
           + "\n---------- Strech 1 ----------\n" + firstStrech.toString()
           + "\n---------- Strech 1 ----------\n" + secondStrech.toString();
  } // toString

  //public void setDriverID(int driver) 
  //{
    //throw new UnsupportedOperationException("Not supported yet."); 
//To change body of generated methods, choose Tools | Templates.
  //}
  
} // Shift
