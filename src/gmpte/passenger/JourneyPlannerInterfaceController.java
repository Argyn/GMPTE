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
  
          
  private ArrayList<BusStop> busStops;
  
  private ArrayList<Area> areas;
  
  private MainControllerInterface mainController;
  
  private JourneyPlannerController jController;
  
  private HashMap<Area, ArrayList<BusStop>> areaStationsMap;
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
  }  
  
  public void preLoadData() {
    
    Task<Void> loadTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        jController = new JourneyPlannerController();
        
        // getting all areas
        areas = AreaDBInfo.getAllAreas();

        // getting all bus stops
        busStops = BusStopInfo.getAllBusStops(areas);
       
        
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
        
        
        // crea mapping between stations and areas
        formAreasStationsMap();
        
        // populate departure choice box with stations
        //populateChoiceBoxWithBusStops(departureChoiceBox);

        // populate destination choice box with stations
       // populateChoiceBoxWithBusStops(destinationChoiceBox);
        
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
    choiceBox.setItems(FXCollections.observableArrayList(busStops));
  }
  
  public void populateChoiceBoxWithArea(ChoiceBox choiceBox) {
    choiceBox.setItems(FXCollections.observableArrayList(areas));
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
          
          displayJourneyPlan(path.getFullPath());
        }
      }
    });
  }
  
  

  @Override
  public void refresh() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  public void displayJourneyPlan(LinkedList<BusChange> journeyPlan) {
    int currentRow = 0;
    int currentColumn = 0;
    routeTable.getChildren().clear();
    
    BusChange currentChange = journeyPlan.poll();
    System.out.println(currentChange);
    LinkedList<BusStop> currentPath = currentChange.getPath();
    
    BusStop firstBStop = currentPath.poll();
    printJourneyStep("Board service "+currentChange.getRoute()+" on ", 
                          firstBStop.toString(), currentRow++, currentColumn);
    
    while(currentPath.peek()!=null) {
      BusStop nextStop = currentPath.poll();
      printJourneyStep(null, nextStop.toString(), currentRow++, currentColumn);
    }
    
    while(journeyPlan.peek()!=null) {
      currentChange = journeyPlan.poll();
      currentPath = currentChange.getPath();
      
      firstBStop = currentPath.poll();
      printJourneyStep("Change service to "+currentChange.getRoute()+" on ", 
                            firstBStop.toString(), currentRow++, currentColumn);

      while(currentPath.peek()!=null) {
        BusStop nextStop = currentPath.poll();
        printJourneyStep(null, nextStop.toString(), currentRow++, currentColumn);
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
  
  public void formAreasStationsMap() {
    areaStationsMap = new HashMap<>();
    
    for(BusStop bStop : busStops) {
      System.out.println(bStop.getArea());
      if(areaStationsMap.get(bStop.getArea())!=null) {
        areaStationsMap.get(bStop.getArea()).add(bStop);
      } else {
        ArrayList<BusStop> list = new ArrayList<>();
        list.add(bStop);
        areaStationsMap.put(bStop.getArea(), list);
      }
    }
  }
  
  public void onDepartureAreaChosen() {
    deptAreaChoiceBox.getSelectionModel().selectedIndexProperty()
                              .addListener(new ChangeListener<Number>() {

      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        onAreaChoosen(areas.get(newValue.intValue()), departureChoiceBox);
      }
      
    });
  }
  
  public void onDestinationAreaChosen() {
    destAreaChoiceBox.getSelectionModel().selectedIndexProperty()
                              .addListener(new ChangeListener<Number>() {

      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        onAreaChoosen(areas.get(newValue.intValue()), destinationChoiceBox);
      }
      
    });
  }
  
  public void onAreaChoosen(Area area, ChoiceBox stationChoiceBox) {
    ArrayList<BusStop> values = areaStationsMap.get(area);
    
    if(values!=null) {
      stationChoiceBox.setItems(FXCollections.observableArrayList(values));
      stationChoiceBox.setValue(values.get(0));
    }
  }
  
}
