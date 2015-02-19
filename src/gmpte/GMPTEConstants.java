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
                  "Holiday Request has been sent for approval.";
  
  public static final String HOLIDAY_REQUEST_DECLINED = 
                  "{reason}.";
  
  public static final String ERROR_DURING_REQUEST = 
                  "Error occured, try again.";
  
  public static final String ERROR_START_DATE_MUST_PRECEDE_END_DATE =
                  "Start date must precede end date.";
  
  public static int NUMBER_OF_HOLIDAYS_PER_YEAR = 25;
  
  public static final String WELCOME_DRIVER_TEXT = "Welcome, {driver-name}";
  
  public static final String REQUEST_EXCEEDS_25_DAYS = "Request exceeds 25 days";
  
  public static final String REQUEST_MORE_THAN_REQ_PEOP = "There are already 10 people on holidays. In particular, these days:";
  
  public static final String IBMS_SYSTEM = "IBMS System";
  
  public static final String LOGIN_WINDOW_TITLE = "Authorization";
  
  public static final String HOLIDAY_REQUEST_WINDOW_TITLE = "Holidays Request";
  
  public static final String CANNOT_REQUEST_HOLIDAYS_FOR_LESS_THAN_DAY = "Start date and end date cannot be the same day";
  
  public static final String CANNOT_TAKE_HOLIDAYS_IN_THE_PAST = "You cannot request holidays in the past";
  
  public static final String START_DATE_SHOULD_START_AT_LEAST_FROM_TMRW = "Start date must at least start from tomorrow.";
}
