/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.test;
import gmpte.Bus;

/**
 *
 * @author mbax3jw3
 */
public class testBus 
{
  public static void main(String args[])
  {
    Bus testBus = new Bus(562);
    System.out.println("bus Id = " + testBus.getBusId());
    System.out.println("bus number = " + testBus.getBusNumber());
  }
}
