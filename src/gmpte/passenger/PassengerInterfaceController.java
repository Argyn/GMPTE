/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.passenger;

import gmpte.ControllerInterface;
import gmpte.MainControllerInterface;
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
public class PassengerInterfaceController implements Initializable, ControllerInterface {
  
  @FXML
  private Button backButton;
  
  @FXML
  private Button logOutButton;
  
  @FXML
  private VBox journeyPlannerMenuItem;
  
  @FXML
  private VBox checkTimetableMenuItem;
  
  private MainControllerInterface mainController;
  
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
    
    // handle back button click
    onBackButtonClick();
    
    // handle log out button
    onLogOutButtonClick();
    
    // handle journey planner menu item
    onJourneyPlannerMenuItemClick();
    
    // handle check timetable menu item
    onCheckTimetableMenuItemClick();
  }  
  
  public void onJourneyPlannerMenuItemClick() {
    journeyPlannerMenuItem.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent t) {
        mainController.showJourneyPlanner();
      }
    
    });
  }
  
  public void onCheckTimetableMenuItemClick() {
    checkTimetableMenuItem.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent t) {
        mainController.showDailyTimetable();
      }
    
    });
  }

  @Override
  public void setMainController(MainControllerInterface mainController) {
    this.mainController = mainController;
  }

  @Override
  public void refresh() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  public void onBackButtonClick() {
    backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent t) {
        mainController.showMainPage();
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
