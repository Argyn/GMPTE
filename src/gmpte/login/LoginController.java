/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.login;

import gmpte.DateHelper;
import gmpte.GMPTEConstants;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author argyn
 */
public class LoginController implements Initializable {
    
    @FXML
    public TextField driverIDTextField;
    
    @FXML
    public TextField driverDateOfBirthTextField;
    
    @FXML
    public Button signInButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Sign In Button Pressed
        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Sign in button pressed");
                System.out.println("Driver ID:"+driverIDTextField.getText());
                System.out.println("Driver birth date:"+driverDateOfBirthTextField.getText());
                if(!DateHelper.isValidDateString(driverDateOfBirthTextField.getText(), 
                                                    GMPTEConstants.DATE_FORMAT)) {
                    driverDateOfBirthTextField.getStyleClass().add("textfield-error");
                    System.out.println("Wrong date");
                }
            }
        });
        
    }    
}
