/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gmpte;
import java.util.ArrayList;

/**
 *
 * @author mabx2mf2
 */
class Strech {
  private final int startTime;
  private final int endTime;
  private final ArrayList<Service> services;


  public int getStartTime()
  {
    return startTime;
  } // getStartTime


  public int getEndTime()
  {
    return endTime;
  } // getEndTime


  public ArrayList<Service> getServices()
  {
    return services;
  } // getServices


  public int getNumberOfServices()
  {
    return services.size();
  } // getNumberOfServices


  public int getStrechDuration()
  {
    return endTime - startTime;
  } // getStrechDuration


  Strech (int strechStart, int strechEnd, ArrayList<Service> strechServices)
  {
    startTime = strechStart;
    endTime = strechEnd;
    services = strechServices;
  } // constructor


  @Override
  public String toString()
  {
    String result = "";
    for (int index = 0; index < getNumberOfServices(); index++)
      result += services.get(index).toString() + "\n";
    result += "Strech duration " + getStrechDuration();
    return result;
  } // toString

} // Strech

  
