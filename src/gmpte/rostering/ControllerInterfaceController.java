/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.rostering;

import gmpte.ControllerInterface;
import gmpte.MainControllerInterface;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import gmpte.helpers.MessageHelper;
import javafx.event.Event;

/**
 * FXML Controller class
 *
 * @author mbgm2rm2
 */
public class ControllerInterfaceController implements Initializable, ControllerInterface {
  
  @FXML
  private VBox generateRosterMenuItem;
  
  @FXML
  private VBox viewRosterMenuItem;
  
  @FXML
  private Button backButton;
  
  @FXML
  private AnchorPane rosterGenerationPane;
  
  @FXML
  private AnchorPane contentPane;
  
  @FXML
  private ProgressIndicator rosterGenerationProgress;
  
  @FXML
  private Label rosterGenerationStatusText;
  
  private MainControllerInterface mainController;
  
  /**
   * Initializes the controller class.
   */
  
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        rosterGenerationPane.setVisible(false);
      }
    });
    //contentPane.setVisible(false);
    // handle back button click
    handleBackButton();
    
    // handle generate roster click
    onRosterGenerationItemClick();
    
    // handle view roster item
    onRosterViewMenuItemClick();
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
  
  public void onRosterGenerationItemClick() {
    generateRosterMenuItem.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        contentPane.setVisible(false);
        rosterGenerationPane.setVisible(true);
        
        final Task<RosterGenerationResponse> task = new Task<RosterGenerationResponse>() {

          @Override
          protected RosterGenerationResponse call() throws Exception {
            RosterController controller = new RosterController();
            controller.addRoute(65);
            controller.addRoute(66);
            controller.addRoute(67);
            controller.addRoute(68);

            RosterGenerationResponse response = controller.generateRoster();
            
            updateProgress(100, 100);
            
            return response;
          }
        };
        
        rosterGenerationProgress.progressProperty().bind(task.progressProperty());
        
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

          @Override
          public void handle(WorkerStateEvent event) {
            System.out.println("Succeedded called");
            RosterGenerationResponse response = task.getValue();
            rosterGenerationStatusText.setText(
                    MessageHelper.prepareRosterGeneratedMessage(
                                response.getFromDate(), response.getToDate()));
          }
        });
        
        task.setOnFailed(new EventHandler<WorkerStateEvent>() {

          @Override
          public void handle(WorkerStateEvent event) {
            System.out.println("Failed");
            System.out.println(task.getException().getMessage());
          }
        });
        
        task.setOnRunning(new EventHandler<WorkerStateEvent>() {

          @Override
          public void handle(WorkerStateEvent event) {
            System.out.println("Running");
          }
        });
        
        new Thread(task).start();
        
      }
      
    });
            
  }
  
  public void onRosterViewMenuItemClick() {
    viewRosterMenuItem.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent t) {
        // show roster view
        mainController.showRosterView();
      }
      
    });
  }
}
