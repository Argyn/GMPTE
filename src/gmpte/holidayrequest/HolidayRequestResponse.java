/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gmpte.holidayrequest;

import gmpte.holidayrequest.*;
import gmpte.holidayrequest.RejectionReasons.RejectionReason;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author argyn
 */
public class HolidayRequestResponse {
    
    public enum ResponseType {
        GRANTED, NOT_GRANTED
    }
    
    private ResponseType response;
    
    private String reason;
    
    private ArrayList<Date> takenDates;
    
    private RejectionReason reasonCode;
    
    // construct with null response
    public HolidayRequestResponse() {
      this.response = null;
      takenDates = new ArrayList<Date>();
    }
    public HolidayRequestResponse(ResponseType response) {
        super();
        this.response = response;
    }
    
    /*
      Sets response (GRANTED or NOT_GRANTED)
    */
    public void setResponse(ResponseType response) {
      this.response = response;
    }
    /*
      Returns response
    */
    public ResponseType getResponse() {
        return response;
    }
   
    /*
      Adds date to the array list of dates when 10 or more people have holidays
    */
    public void addDateWhenMoreThanTen(Date date) {
      takenDates.add(date);
    }
    
    public ArrayList<Date> getDatesWhenTenDriversAreOnHolidays() {
      return takenDates;
    }
    
    public void setDatesWhenTenMorePeopleOnHolidays(ArrayList<Date> dates) {
      takenDates = dates;
    }
    
    /*
      Set reason code
    */
    public void setReasonCode(RejectionReason reasonCode) {
      this.reasonCode = reasonCode; 
    }
    
    /*
      Return the reason code
    */
    public RejectionReason getReasonCode() {
      return this.reasonCode;
    }
    
}
