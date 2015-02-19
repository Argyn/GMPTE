/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.holidayrequest;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author argyn
 */
public class HolidayRequestResponse {
    
    private ResponseType response;
    
    private String reason;
    
    private ArrayList<Date> takenDates;
    
    public HolidayRequestResponse(ResponseType response) {
        this.response = response;
        takenDates = new ArrayList<Date>();
    }
    
    public HolidayRequestResponse(ResponseType response, String reason) {
        this.response = response;
        this.reason = reason;
    }
    
    public ResponseType getResponse() {
        return response;
    }
    
    public String getReason() {
      return reason;
    }
    
    public void setReason(String reason) {
      this.reason = reason;
    }
    
    public enum ResponseType {
        GRANTED, NOT_GRANTED
    }
    
    public void addDateWhenMoreThanTen(Date date) {
      takenDates.add(date);
    }
    
    public ArrayList<Date> getDatesWhenTenDriversAreOnHolidays() {
      return takenDates;
    }
    
    public void setDatesWhenTenMorePeopleOnHolidays(ArrayList<Date> takenDates) {
      this.takenDates = takenDates;
    }
    
}
