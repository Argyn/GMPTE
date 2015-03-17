/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.test;

import gmpte.databaseinterface.TimetableInfo;
import gmpte.databaseinterface.database;

/**
 *
 * @author mbgm2rm2
 */
public class test {
  public static void main(String[] args) {
    database.openBusDatabase();
    
    int route = 65;
    int[] services = TimetableInfo.getServices(route, 
                                    TimetableInfo.timetableKind.weekday);

    for(int service : services) {
        System.out.println(service);
    }
  }
}
