/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.rostering;

import gmpte.ControllerInterface;
import gmpte.MainControllerInterface;
import gmpte.UIInterfaceHelper;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author mbgm2rm2
 */
public class ControllerInterfaceController implements Initializable, ControllerInterface {
  
  @FXML
  private VBox generateRosterMenuItem;
  
  @FXML
  private Button backButton;
  
  
  private MainControllerInterface mainController;
  
  /**
   * Initializes the controller class.
   */
  
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // handle back button click
    handleBackButton();
  }  

  @Override
  public void setMainController(MainControllerInterface mainController) {
    this.mainController = mainController;
  }
  
  public void handleBackButton() {
    backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent t) {
        mainController.showMainPage();
      }
    
    });
  }
  
}
