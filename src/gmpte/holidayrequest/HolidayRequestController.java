/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.holidayrequest;

import gmpte.DateHelper;
import gmpte.GMPTEConstants;
import gmpte.login.LoginCredentials;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

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
    
    public HolidayController holidayController;
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // set prompt text for combo box
        userMenuComboBox.setPromptText(LoginCredentials.getInstance().getDriver().getName());
        
        // add log out button
        submitButton.setOnAction(getSubmitButtonHandler());
    }    
    
    public EventHandler<ActionEvent> getSubmitButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                // check dates validity
                if(!DateHelper.isValidDateString(startDateTextField.getText(), 
                   GMPTEConstants.DATE_FORMAT)
                   || !DateHelper.isValidDateString(endDateTextField.getText(), 
                      GMPTEConstants.DATE_FORMAT)) {
                    // report error messages
                    return;
                } else {
                    Date startDate = null;
                    Date endDate = null;
                    
                    // convert strings to dates
                    try {
                        startDate = DateHelper.getDateFromString(
                                                    startDateTextField.getText(), 
                                                    GMPTEConstants.DATE_FORMAT);
                        endDate = DateHelper.getDateFromString(
                                                    endDateTextField.getText(), 
                                                    GMPTEConstants.DATE_FORMAT);
                    } catch (ParseException ex) {
                        Logger.getLogger(HolidayRequestController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if(LoginCredentials.getInstance().isAuthenticated()
                       && startDate!=null & endDate!=null) {
                        HolidayRequest request = 
                                new HolidayRequest(
                                        LoginCredentials.getInstance().getDriver(),
                                        startDate,
                                        endDate
                                );
                        holidayController.holidayRequest(request);
                        System.out.println("Request has been passed");
                    }
                    
                }
               
            }
        };
    }
    
    public void setHolidayController(HolidayController controller) {
        this.holidayController = controller;
    }
    
    
}
