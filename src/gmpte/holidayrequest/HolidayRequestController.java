/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.holidayrequest;

import eu.schudt.javafx.controls.calendar.DatePicker;
import gmpte.ControllerInterface;
import gmpte.entities.Driver;
import gmpte.GMPTEConstants;
import gmpte.MainControllerInterface;
import gmpte.db.DriverInfo;
import gmpte.holidayrequest.RejectionReasons.RejectionReason;
import static gmpte.holidayrequest.RejectionReasons.RejectionReason.MORE_THAN_TEN_PEOPLE_HAVE_HOLIDAYS;
import gmpte.login.LoginCredentials;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author mbgm2rm2
 */
public class HolidayRequestController implements Initializable, ControllerInterface {
  @FXML
  public Button submitButton;

  @FXML
  public TextField startDateTextField;

  @FXML
  public TextField endDateTextField;

  @FXML
  public HBox resultMessageHBox;
  
  @FXML
  public Label resultMessageText;
  
  @FXML
  public GridPane holidayForm;
  
  @FXML
  public Label holidaysTakenThisYearLabel;
  
  @FXML
  public Label numberOfHolidaysLeftLabel;
  
  @FXML
  public Label driverName;
  
  @FXML
  public Button backButton;
  
  @FXML
  public Button logOutButton;
  
  public DatePicker startDatePicker;
  
  public DatePicker endDatePicker;
  
  public HolidayController holidayController;
  
  public MainControllerInterface mainController;
  
  /**
   * Initialises the controller class.
   */

  @Override
  public void initialize(URL url, ResourceBundle rb) {
      // initializing date pickers
      startDatePicker = getStartDatePicker();
      
      endDatePicker = getEndDatePicker();
     
      
      holidayForm.add(startDatePicker, 1, 0);
      holidayForm.add(endDatePicker, 1, 1);
      
      startDateTextField.setVisible(false);
      endDateTextField.setVisible(false);

      // add log out button
      submitButton.setOnAction(getSubmitButtonHandler());
      
      // populate info table
      populateInfoTable();
      
      // display driver's name
      putWelcomeDriverText();
      
      // hide message box when date picker get changed
      startDatePicker.setOnMouseClicked(new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
          hideMessageBox();
        }
      });
      
      // handle log out button
      onLogOutButtonClick();
      
      // handle back button
      onBackButtonClick();
  }    

  public EventHandler<ActionEvent> getSubmitButtonHandler() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent t) {
        if(LoginCredentials.getInstance().isAuthenticated()
           && startDatePicker.getSelectedDate()!=null 
           && endDatePicker.getSelectedDate()!=null) {
            if(validDatesSupplied(startDatePicker.getSelectedDate(), endDatePicker.getSelectedDate())) {
              // if start date is before end date. then process request
              // create request
              HolidayRequest request = 
                new HolidayRequest(
                        LoginCredentials.getInstance().getDriver(),
                        startDatePicker.getSelectedDate(),
                        endDatePicker.getSelectedDate()
                );
              // pass request
              HolidayRequestResponse response = 
                                holidayController.holidayRequest(request);

              processHolidayRequestResult(response);
            }
        } // if
      } // handle 
    };
  }
  
  public boolean validDatesSupplied(Date startDate, Date endDate) {
    
    Date currentDate = new Date();
    DateFormat format = new SimpleDateFormat(GMPTEConstants.DATE_FORMAT);
    try {
      currentDate = format.parse(format.format(currentDate));
    } catch (ParseException ex) {
      Logger.getLogger(HolidayRequestController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(currentDate);
    calendar.add(Calendar.DATE, 1);
    Date tomorrow = calendar.getTime();
    
    if(startDate.after(endDate)) {
      popErrorBox(GMPTEConstants.ERROR_START_DATE_MUST_PRECEDE_END_DATE);
      return false;
    } else if(startDate.compareTo(endDate)==0) {
      popErrorBox(GMPTEConstants.CANNOT_REQUEST_HOLIDAYS_FOR_LESS_THAN_DAY);
      return false;
    } else if(startDate.compareTo(currentDate)==0) {
      popErrorBox(GMPTEConstants.START_DATE_SHOULD_START_AT_LEAST_FROM_TMRW);
      return false;
    } else if(startDate.before(currentDate)) {
      popErrorBox(GMPTEConstants.CANNOT_TAKE_HOLIDAYS_IN_THE_PAST);
      return false;
    }
    
    return true;
  }
  
  public void setHolidayController(HolidayController controller) {
    this.holidayController = controller;
  }

  public void processHolidayRequestResult(HolidayRequestResponse response) {
    switch(response.getResponse()) {
      case GRANTED:
        // pop success box with message granted
        popSuccessBox(GMPTEConstants.HOLIDAY_REQUEST_APPROVED);
        Driver driver = LoginCredentials.getInstance().getDriver();
        driver.setHolidaysTaken(DriverInfo.getHolidaysTaken(driver.getDriverID()));
        // update info bar
        updateNumberOfHolidaysTaken();
        break;
      case NOT_GRANTED:
        // pop error box with message not granted
        handleRejectedRequest(response);
        break;
      default:
        popErrorBox(GMPTEConstants.ERROR_DURING_REQUEST);
        break;
    }
  }
  
  public DatePicker getStartDatePicker() {
    DatePicker datePicker = initializeDatePicker();
    datePicker.setAlignment(Pos.CENTER_LEFT);
    return datePicker;
  }
  
  public DatePicker getEndDatePicker() {
    DatePicker datePicker = initializeDatePicker();
    datePicker.setAlignment(Pos.CENTER_LEFT);
    
    return datePicker;
  }
  
  public DatePicker initializeDatePicker() {
    DatePicker datePicker = new DatePicker();
    datePicker.getStyleClass().add("large-textfield");
    DateFormat format = new SimpleDateFormat(GMPTEConstants.DATE_FORMAT);
    datePicker.setDateFormat(format);
    return datePicker;
  }
   
  public void popErrorBox(String message) {
    resultMessageHBox.getStyleClass().removeAll("success-hbox");
    resultMessageHBox.getStyleClass().add("error-hbox");
    resultMessageText.setText(message);
    resultMessageHBox.setVisible(true);
  }
  
  public void popSuccessBox(String message) {
    resultMessageHBox.getStyleClass().removeAll("error-hbox");
    resultMessageHBox.getStyleClass().add("success-hbox");
    resultMessageText.setText(message);
    resultMessageHBox.setVisible(true);
  }
  
  public void hideMessageBox() {
    resultMessageHBox.setVisible(false);
  }
  
  public void populateInfoTable() {
    Driver driver = LoginCredentials.getInstance().getDriver();
    numberOfHolidaysLeftLabel.setText(
        Integer.toString(
            GMPTEConstants.NUMBER_OF_HOLIDAYS_PER_YEAR-driver.getHolidaysTaken()
        ));
    holidaysTakenThisYearLabel.setText(
                            Integer.toString(driver.getHolidaysTaken())
                              );
  }
  
  public void putWelcomeDriverText() {
    String driverNameText = LoginCredentials.getInstance().getDriver().getName();
    driverName.setText(driverNameText);
  }
  
  public String getDeclinedMessage(String template, String reason) {
    return template.replaceAll("\\{reason\\}", reason);
  }
  
  public void updateNumberOfHolidaysTaken() {
    Driver driver = LoginCredentials.getInstance().getDriver();
    int numberOfHolidaysTaken = driver.getHolidaysTaken();
    holidaysTakenThisYearLabel.setText(Integer.toString(driver.getHolidaysTaken()));
    // update number of holidays left
    numberOfHolidaysLeftLabel.setText(
            Integer.toString(
                    GMPTEConstants.NUMBER_OF_HOLIDAYS_PER_YEAR-numberOfHolidaysTaken
            ));
  }
  
  
  @Override
  public void setMainController(MainControllerInterface controller) {
    mainController = controller;
  }
  
  public void logOut() throws Exception {
    mainController.onLogOut();
  }
 
  public void handleRejectedRequest(HolidayRequestResponse response) {
    RejectionReason reasonCode = response.getReasonCode();
    switch(reasonCode) {
      case MORE_THAN_TEN_PEOPLE_HAVE_HOLIDAYS:
        String dates = "";
        DateFormat format = new SimpleDateFormat(GMPTEConstants.DATE_FORMAT);
        for(Iterator<Date> it=response.getDatesWhenTenDriversAreOnHolidays().iterator(); it.hasNext();) {
          Date date = it.next();
          dates += "\n"+format.format(date);
        }
        System.out.println("Dates:"+dates);
        
        popErrorBox(GMPTEConstants.REQUEST_MORE_THAN_REQ_PEOP+dates);
        break;
      case NUMBER_OF_HOLIDAYS_ALLOWED_EXCEEDED:
        popErrorBox(GMPTEConstants.REQUEST_EXCEEDS_25_DAYS);
        break;
    }
  }

  @Override
  public void refresh() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  public void onBackButtonClick() {
    backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent t) {
        mainController.showControllerInterface();
      }
    });
  }
  
  public void onLogOutButtonClick() {
    logOutButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent t) {
        mainController.showMainPage();
      }

    });
  }
  
}
