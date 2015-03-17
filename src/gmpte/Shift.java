/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;

/**
 *
 * @author mbax3jw3
 */
public class Shift 
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
