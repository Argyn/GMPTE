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
  
  public static final String DAY_TIME_FORMAT = "HH:mm";
  
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
  
  public static final String CONTROLLER_INTERFACE_WINDOW = "Controller Interface";
  
  public static final String DRIVER_INTERFACE_WINDOW = "Driver User Panel";
  
  public static final String ROSTER_VIEW_WINDOW = "ROSTER FOR THE NEXT WEEK";
  
  public static final String CANNOT_REQUEST_HOLIDAYS_FOR_LESS_THAN_DAY = "Start date and end date cannot be the same day";
  
  public static final String CANNOT_TAKE_HOLIDAYS_IN_THE_PAST = "You cannot request holidays in the past";
  
  public static final String START_DATE_SHOULD_START_AT_LEAST_FROM_TMRW = "Start date must at least start from tomorrow.";
  
  public static final double STAGE_MIN_WIDTH = 500.0;
  public static final double STAGE_MIN_HEIGHT = 600.0;
  
  public static final String MAIN_MENU_ITEM_STYLE = "main-menu-item";
  public static final String MAIN_MENU_ITEM_ON_HOVER_STYLE = "main-menu-item-hover";
  
  public static final String ROSTER_GENERATED_LABEL = "The roster for the period from {from} to {to} has been successfully generated.";
  
  public static final String CSS_MEDIUM_LABEL = "medium-label";
  
  public static final Integer[] ROUTES = new Integer[]{65,66,67,68};
  
  public static final String DATETIME_FORMAT_SQL = "yyyy-MM-dd 00:00:00";
  
  public static final int MAX_MINUTES_PER_DAY = 600;
  
  public static final int MAX_SHIFT_DURATION = 300;
  
  public static final int MINUTES_PER_WEEK_LIMIT = 3000;
}
