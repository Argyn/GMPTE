/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.login;

import gmpte.Driver;
import gmpte.MainControllerInterface;
import gmpte.databaseinterface.DriverInfo;
import gmpte.databaseinterface.database;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
/**
 * FXML Controller class
 *
 * @author argyn
 */
public class LoginController implements Initializable {
    
    @FXML
    public TextField driverIDTextField;
    
    @FXML
    public Button submitButton;
    
    @FXML
    public HBox wrongCredentialsBox;
    
    public MainControllerInterface mainController = null;
    
    /**
     * Initialises the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

      // Sign In Button Pressed
      submitButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          // log in
          submitLogIn();
        }
      });

      // allow submit by pressing the enter key
      addSubmitOnEnterListener();
    }
    
    
    
    public Driver initializeDriver(int driverID, int driverNumber) {
      Driver driver = null;

      if(DriverInfo.findDriver(Integer.toString(driverNumber))!=0) {
        // driver has been found
        driver = new Driver(driverID, driverNumber);
        driver.setName(DriverInfo.getName(driverID));
        driver.setHolidaysTaken(DriverInfo.getHolidaysTaken(driverID));
        driver.setHoursThisWeek(DriverInfo.getHoursThisWeek(driverID));
        driver.setHoursThisYear(DriverInfo.getHoursThisYear(driverID));
        driver.setAvailable(DriverInfo.isAvailable(driverID));
      }

      return driver;
    }
    
    public void saveDriverCredentials(int driverID, int driverNumber) {
        Driver driver = initializeDriver(driverID, driverNumber);
        LoginCredentials.getInstance().setDriver(driver);
        System.out.println("Credentials saved");
    }
    
    public void setMainController(MainControllerInterface mainController) {
        this.mainController = mainController;
    }
    
    public void addSubmitOnEnterListener() {
      driverIDTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

        @Override
        public void handle(KeyEvent t) {
          if(t.getCode().equals(KeyCode.ENTER)) {
            // submit login
            submitLogIn();
          }
        }
        
      });
    }
    
    public void submitLogIn() {
    int driverNumber = Integer.parseInt(driverIDTextField.getText());
      int driverID;
      if((driverID = DriverInfo.findDriver(Integer.toString(driverNumber)))==0) {
        // no driver found
        driverIDTextField.getStyleClass().add("textfield-error");
        wrongCredentialsBox.setVisible(true);
      } else {
        // Launch
        saveDriverCredentials(driverID, driverNumber);

        try {
          mainController.onSuccessfullLogin();
        } catch (Exception ex) {
          Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    
    
}
