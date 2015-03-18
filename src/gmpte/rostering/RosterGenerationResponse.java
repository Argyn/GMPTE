/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.rostering;

import java.util.Date;

/**
 *
 * @author mbgm2rm2
 */

public class RosterGenerationResponse {
  
  private final Date fromDate;
  
  private final Date toDate;
  
  public RosterGenerationResponse(Date from, Date to) {
    // storing from date
    fromDate = from;
    
    // storing to date
    toDate = to;
  }
  
  public Date getFromDate() {
    return fromDate;
  }
  
  public Date getToDate() {
    return toDate;
  }
}
