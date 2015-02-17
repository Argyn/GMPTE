/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.holidayrequest;

/**
 *
 * @author argyn
 */
public class HolidayRequestResponse {
    
    private ResponseType response;
    
    public HolidayRequestResponse(ResponseType response) {
        this.response = response;
    }
    
    public ResponseType getResponse() {
        return response;
    }
    
    public enum ResponseType {
        GRANTED, NOT_GRANTED
    }
    
    
    
}
