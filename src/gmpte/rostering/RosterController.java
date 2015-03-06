/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.rostering;

import gmpte.Driver;

/**
 *
 * @author mbgm2rm2
 */
public class RosterController {
  
  public RosterGenerationResponse generateRoster() {
    return new RosterGenerationResponse();
  }
  
  public Roster getRosterForDriver(Driver driver) {
    return new Roster();
  }
  
  public Roster getRostersForAllDrivers() {
    return new Roster();
  }
  
}
