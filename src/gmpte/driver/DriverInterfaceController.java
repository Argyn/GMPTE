/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.driver;

import gmpte.ControllerInterface;
import gmpte.MainControllerInterface;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author mbgm2rm2
 */
public class DriverInterfaceController implements Initializable, ControllerInterface {
  
  private MainControllerInterface mainController;
  
  @FXML
  private VBox requestHolidaysMenuItem;
  
  @FXML
  private VBox checkRosterMenuItem;
  
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    //handle request holidays page
    onRequestHolidays();
    
    // handle checkRosterMenuItem
    onCheckRoster();
  }  

  @Override
  public void setMainController(MainControllerInterface mainController) {
    this.mainController = mainController;
  }
  
  public void onRequestHolidays() {
    requestHolidaysMenuItem.setOnMouseClicked(new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent t) {
        mainController.showHolidayRequestPage();
      }
    });
  }
  
  public void onCheckRoster() {
    checkRosterMenuItem.setOnMouseClicked(new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent t) {
        //mainController.showHolidayRequestPage();
      }
    });
  }
}
