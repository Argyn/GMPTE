/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;
/**
 *
 * @author argyn
 */
public class HolidayController {
    
    public HolidayRequestResponse holidayRequest(HolidayRequest request) {
        // logic goes here
        return new HolidayRequestResponse(HolidayRequestResponse.ResponseType.GRANTED);
        // your logic starts here
    }
}
