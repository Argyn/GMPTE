/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.holidayrequest;

import eu.schudt.javafx.controls.calendar.DatePicker;
import gmpte.DateHelper;
import gmpte.Driver;
import gmpte.GMPTEConstants;
import gmpte.databaseinterface.DriverInfo;
import gmpte.login.LoginCredentials;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
public class HolidayRequestController implements Initializable {
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
  public Label welcomeDriverText;
  
  
  public DatePicker startDatePicker;
  
  public DatePicker endDatePicker;
  
  public HolidayController holidayController;
  
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
      
      // put welcome driver text
      putWelcomeDriverText();
      
      startDatePicker.setOnMouseClicked(new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
          hideMessageBox();
        }
      });
  }    

  public EventHandler<ActionEvent> getSubmitButtonHandler() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent t) {
        if(LoginCredentials.getInstance().isAuthenticated()
           && startDatePicker.getSelectedDate()!=null 
           && endDatePicker.getSelectedDate()!=null) {
            if(startDatePicker.getSelectedDate().before(endDatePicker.getSelectedDate())) {
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
            } else {
              // pop error box
              popErrorBox(GMPTEConstants.ERROR_START_DATE_MUST_PRECEDE_END_DATE);
            }
            
        } // if
      } // handle 
    };
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
        popErrorBox(getDeclinedMessage(GMPTEConstants.HOLIDAY_REQUEST_DECLINED, response.getReason()));
        
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
    String driverName = LoginCredentials.getInstance().getDriver().getName();
    String welcomeText = GMPTEConstants.WELCOME_DRIVER_TEXT
                          .replaceAll("\\{driver-name\\}", driverName);
    welcomeDriverText.setText(welcomeText);
    System.out.println(welcomeText);
    
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
  
}
