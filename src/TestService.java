
import gmpte.db.DBHelper;
import gmpte.db.database;
import gmpte.entities.Service;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
  public static void insertBusStationTimes(Service service) {
    String query = "INSERT INTO bus_station_times (service, timing_point, time, daily_timetable)"
            + " VALUES(?,?,?,?)";
    
    try {
      PreparedStatement statement = DBHelper.prepareStatement(query);
      statement.setInt(1, service.getServiceId());
      statement.setInt(4, service.getDailyTimetableId());
      
      Iterator<Integer> it1 = service.getServiceTimingPoints().iterator();
      Iterator<Integer> it2 = service.getServiceTimes().iterator();
      
      while(it1.hasNext() && it2.hasNext()) {
        statement.setInt(2, it1.next());
        statement.setInt(3, it2.next());
        statement.execute();
      }

    } catch (SQLException ex) {
      Logger.getLogger(TestService.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  public static void main(String[] args) {
    
    database.openBusDatabase();
    
    Scanner scanner = new Scanner(System.in);
    
    /*System.out.print("Service id = ");
    int serviceID = scanner.nextInt();
    
    Service service = new Service(serviceID);
    
    System.out.println();
    System.out.println(service.getServiceTimingPoints());
    System.out.println(service.getServiceTimes());
    6177 and 6562*/
    
    for(int i=6177; i<=6562; i++) {
      Service service = new Service(i);
      
      System.out.println("Service "+i);
      System.out.println(service.getServiceTimingPoints());
      System.out.println(service.getServiceTimes());
      
      insertBusStationTimes(service);
    }
  }
}
