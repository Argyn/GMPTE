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
public class GMPTEConstants {
  public static final String DATE_FORMAT = "dd/MM/yyyy";
    
  public static final String HOLIDAY_REQUEST_APPROVED = 
                  "Holiday Request Has Been Approved.";
  
  public static final String HOLIDAY_REQUEST_DECLINED = 
                  "Holiday Request Has Not Been Approved, because {reason}.";
  
  public static final String ERROR_DURING_REQUEST = 
                  "Error occured, try again.";
  
  public static final String ERROR_START_DATE_MUST_PRECEDE_END_DATE =
                  "Start date must precede end date.";
  
  public static int NUMBER_OF_HOLIDAYS_PER_YEAR = 25;
  
  public static final String WELCOME_DRIVER_TEXT = "Welcome, {driver-name}";
}
