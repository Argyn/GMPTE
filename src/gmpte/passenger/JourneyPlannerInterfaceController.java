/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.passenger;

import gmpte.ControllerInterface;
import gmpte.MainControllerInterface;
import gmpte.db.AreaDBInfo;
import gmpte.db.BusStopInfo;
import gmpte.entities.Area;
import gmpte.entities.BusChange;
import gmpte.entities.BusStop;
import gmpte.entities.Path;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author mbgm2rm2
 */
public class JourneyPlannerInterfaceController implements Initializable, ControllerInterface {
  
  @FXML
  private Button backButton;
  
  @FXML
  private Button logOutButton;
  
  @FXML
  private ChoiceBox<BusStop> departureChoiceBox;
  
  @FXML
  private ChoiceBox<BusStop> destinationChoiceBox;
  
  @FXML
  private Button planJourneyButton;
  
  @FXML
  private Label journeyPathLabel;
  
  @FXML
  private VBox routeContainerVBox;
  
  @FXML
  private GridPane routeTable;
  
  @FXML
  private Button closeRouteWindowButton;
  
  @FXML
  private ChoiceBox<Area> deptAreaChoiceBox;
  
  @FXML
  private ChoiceBox<Area> destAreaChoiceBox;
  
  @FXML
  private AnchorPane journeyPlannerPane;
  
  @FXML
  private AnchorPane loadingIndicatorPane;
  
  @FXML
  private Label journeyFromToLabel;
  
  private MainControllerInterface mainController;
  
  private JourneyPlannerController jController;
  
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {    
    // handle back button click
    onBackButtonClick();
    
    // handle log out button
    onLogOutButtonClick();
   
    // handle journey plan button
    onPlanJourneyButtonClick();
    
    // handle results window close button
    onCloseRouteWindowButtonClick();
    
    // pre load needed data
    preLoadData();
    //jController = new JourneyPlannerController();
    //ArrayList<Area> areas = AreaDBInfo.getAllAreas();
    
    //BusStopInfo.getAllBusStops(areas);
  }  
  
  public void preLoadData() {
    
    Task<Void> loadTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        jController = new JourneyPlannerController();
    
        return null;
      }
    };
    
    loadTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

      @Override
      public void handle(WorkerStateEvent event) {
        // poulate departure point area box
        populateChoiceBoxWithArea(deptAreaChoiceBox);
        
        // poulate destination point area box
        populateChoiceBoxWithArea(destAreaChoiceBox);
        
        // hide loading indicator
        loadingIndicatorPane.setVisible(false);
        
        // show planner
        journeyPlannerPane.setVisible(true);
        
        //handle dep area
        onDepartureAreaChosen();
        
        // handle dest area
        onDestinationAreaChosen();
      }
    });
   
    new Thread(loadTask).start();

    
    
  }
  
  public void onBackButtonClick() {
    backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent t) {
        mainController.showPassengerInterface();
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

  @Override
  public void setMainController(MainControllerInterface mainController) {
    this.mainController = mainController;
  }
  
  public void populateChoiceBoxWithBusStops(ChoiceBox choiceBox) {
    choiceBox.setItems(FXCollections.observableArrayList(jController.getBusStops()));
  }
  
  public void populateChoiceBoxWithArea(ChoiceBox choiceBox) {
    choiceBox.setItems(FXCollections.observableArrayList(jController.getAreas()));
  }
  
  public void onPlanJourneyButtonClick() {
    planJourneyButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent t) {
        // do planning
        BusStop from = departureChoiceBox.getValue();
        BusStop to = destinationChoiceBox.getValue();
        if(from!=null && to!=null) {
          routeContainerVBox.setVisible(true);
          
          System.out.println("Planning the journey");
          
          Path path = jController.getJourneyPlan(from, to);
          
          setJourneyFromToLabel(from.toString(), to.toString());
          displayJourneyPlan(path.getFullPath(), to);
        }
      }
    });
  }
  
  

  @Override
  public void refresh() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  public void displayJourneyPlan(LinkedList<BusChange> journeyPlan, BusStop target) {
    int currentRow = 0;
    int currentColumn = 0;
    routeTable.getChildren().clear();
    
    BusChange currentChange = journeyPlan.poll();
    System.out.println(currentChange);
    LinkedList<BusStop> currentPath = currentChange.getPath();
    
    BusStop firstBStop = currentPath.poll();
    
    DateFormat format = new SimpleDateFormat("HH:mm");
    
    String boardString = "At "+format.format(currentChange.getBoardingTime())
                   +" board service "+currentChange.getRoute()+" on ";
    
    printJourneyStep(boardString, firstBStop.toString(), currentRow++, currentColumn);
    
    while(currentPath.peek()!=null) {
      BusStop nextStop = currentPath.poll();
      //printJourneyStep(null, nextStop.toString(), currentRow++, currentColumn);
      
      String disembarkMessage = "At "+format.format(currentChange.getDisembarkTime())+" disembark on";
      
      if(!nextStop.equals(target) && currentPath.peek()!=null)
        printJourneyStep(null, nextStop.toString(), currentRow++, currentColumn);
      else
        printJourneyStep(disembarkMessage, nextStop.toString(), currentRow++, currentColumn);
    }
    
    while(journeyPlan.peek()!=null) {
      currentChange = journeyPlan.poll();
      currentPath = currentChange.getPath();
      
      firstBStop = currentPath.poll();
      String changeServiceString = "At "+format.format(currentChange.getBoardingTime())
                          +" change to service "+currentChange.getRoute()+" on";
      
      printJourneyStep(changeServiceString, 
                            firstBStop.toString(), currentRow++, currentColumn);

      while(currentPath.peek()!=null) {
        BusStop nextStop = currentPath.poll();
        
        String disembarkMessage = "At "+format.format(currentChange.getDisembarkTime())+" disembark on";
        if(!nextStop.equals(target) && currentPath.peek()!=null)
          printJourneyStep(null, nextStop.toString(), currentRow++, currentColumn);
        else
          printJourneyStep(disembarkMessage, nextStop.toString(), currentRow++, currentColumn);
      }
    }
  }
  
  public void printJourneyStep(String text, String station, int row, int col) {
    if(text!=null) {
      Label label = new Label(text);
      routeTable.add(label, col, row);
    }
    
    if(station!=null) {
      Label label = new Label(station);
      routeTable.add(label, col+1, row);
    }
    
  }
  
  public void onCloseRouteWindowButtonClick() {
    closeRouteWindowButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent t) {
        routeContainerVBox.setVisible(false);
      }
    
    });
  }
  
  public void onDepartureAreaChosen() {
    deptAreaChoiceBox.getSelectionModel().selectedIndexProperty()
                              .addListener(new ChangeListener<Number>() {

      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        onAreaChoosen(jController.getAreas().get(newValue.intValue()), departureChoiceBox);
      }
      
    });
  }
  
  public void onDestinationAreaChosen() {
    destAreaChoiceBox.getSelectionModel().selectedIndexProperty()
                              .addListener(new ChangeListener<Number>() {

      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        onAreaChoosen(jController.getAreas().get(newValue.intValue()), destinationChoiceBox);
      }
      
    });
  }
  
  public void onAreaChoosen(Area area, ChoiceBox stationChoiceBox) {
    ArrayList<BusStop> values = jController.getAreasBusStop(area);
    
    if(values!=null) {
      stationChoiceBox.setItems(FXCollections.observableArrayList(values));
      stationChoiceBox.setValue(values.get(0));
    } else {
      stationChoiceBox.setItems(FXCollections.observableArrayList(new ArrayList<BusStop>()));
    }
  }
  
  public void setJourneyFromToLabel(String from, String to) {
    journeyFromToLabel.setText("Your journey from "+from+" to "+to);
    journeyFromToLabel.setWrapText(true);
  }
}
