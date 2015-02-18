/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.holidayrequest;

import eu.schudt.javafx.controls.calendar.DatePicker;
import gmpte.DateHelper;
import gmpte.GMPTEConstants;
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
  public ComboBox userMenuComboBox;

  @FXML
  public HBox resultMessageHBox;
  
  @FXML
  public Label resultMessageText;
  
  @FXML
  public GridPane holidayForm;
  
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
      
      
      // set prompt text for combo box
      userMenuComboBox.setPromptText(LoginCredentials.getInstance()
                                          .getDriver().getName());

      // add log out button
      submitButton.setOnAction(getSubmitButtonHandler());
  }    

  public EventHandler<ActionEvent> getSubmitButtonHandler() {
    return new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent t) {
        if(LoginCredentials.getInstance().isAuthenticated()
           && startDatePicker.getSelectedDate()!=null 
           && endDatePicker.getSelectedDate()!=null) {
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
        resultMessageHBox.getStyleClass().removeAll("error-hbox");
        resultMessageHBox.getStyleClass().add("success-hbox");
        resultMessageText.setText(GMPTEConstants.HOLIDAY_REQUEST_APPROVED);
        
        break;
      case NOT_GRANTED:
        resultMessageHBox.getStyleClass().removeAll("success-hbox");
        resultMessageHBox.getStyleClass().add("error-hbox");
        resultMessageText.setText(GMPTEConstants.HOLIDAY_REQUEST_DECLINED);
        break;
      default:
        resultMessageHBox.getStyleClass().add("error-hbox");
        resultMessageText.setText(GMPTEConstants.ERROR_DURING_REQUEST);
        break;
    }
    
    resultMessageHBox.setVisible(true);
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
    
}
