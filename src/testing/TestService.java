package testing;


import gmpte.db.database;
import gmpte.entities.Service;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mbgm2rm2
 */
public class TestService {
  public static void main(String[] args) {
    
    database.openBusDatabase();
    
    Scanner scanner = new Scanner(System.in);
    
    System.out.print("Service id = ");
    int serviceID = scanner.nextInt();
    
    Service service = new Service(serviceID);
    
    System.out.println();
    System.out.println(service.getServiceTimingPoints());
    System.out.println(service.getServiceTimes());

  }
}
