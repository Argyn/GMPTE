/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.dailytimetable;

import gmpte.ControllerInterface;
import gmpte.MainControllerInterface;
import gmpte.db.BusStopInfo;
import gmpte.entities.BusStop;
import gmpte.entities.Route;
import gmpte.entities.Service2;
import gmpte.helpers.DateHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author mbgm2rm2
 */
public class DailyTimetableInterfaceController implements Initializable, ControllerInterface {
  
  @FXML
  private Accordion servicesAccordion;
  
  @FXML
  private AnchorPane loadingIndicatorPane;
  
  @FXML
  private AnchorPane mainContentPane;
  
  @FXML
  private Button backButton;
  
  @FXML
  private Button logOutButton;
  
  @FXML
  private ChoiceBox busStopChoiceBox;
  
  private DailyTimetableController tController;
  
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
    
    
    // hide main content pane
    mainContentPane.setVisible(false);
    
    // pre load data now
    preLoadData();
    //tController = new DailyTimetableController();
    
  } 
  
  public void preLoadData() {
    
    Task<Void> loadTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        tController = new DailyTimetableController();
        
        
        
        return null;
      }
    };
    
    loadTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

      @Override
      public void handle(WorkerStateEvent event) {
        loadingIndicatorPane.setVisible(false);
        mainContentPane.setVisible(true);
        
        // populate accordion services
        populateRoutesAccordionWithAllBusStops();
        
        populateBusStopChoiceBox();
        
        onBusStopChoiceChange();
      }
    });
   
    new Thread(loadTask).start();
  }
  
  public void populateRoutesAccordionWithAllBusStops() {
    for(Route route : tController.getRoutes()) {
      TitledPane titledPane = new TitledPane();
      titledPane.setText(route.toString());
      
      GridPane bStopsGridPane = new GridPane();
      int row = 0;
      int col = 0;
      
      ArrayList<Service2> routeServices = tController.getServicesOfRoute(route);

      for(BusStop stop : tController.getBusStopsOfRoute(route)) {
        
        bStopsGridPane.add(new Label(stop.toString()), col, row);
        bStopsGridPane.setHgap(10);
        bStopsGridPane.setVgap(5);

        int timesCol = 1;
        for(Service2 service : routeServices) {
          int index = 0;
          for(BusStop bStop : service.getBusStops()) {
            if(stop.equals(bStop)) {
              Date arriveTime = service.getTimes().get(index);
              Label timeLabel = new Label(DateHelper.formatDateToString("HH:mm", 
              arriveTime));

              if(!service.doesTerminateAtTime(arriveTime))
                timeLabel.getStyleClass().add("medium-label");
              
              bStopsGridPane.add(timeLabel, timesCol++, row);
            }
            index++;
          }
          
          if(timesCol>12) {
            timesCol = 1;
            row++;
          }
        }
        
        row++;
      }
      
      ScrollPane scrollPane = new ScrollPane();
      AnchorPane anchorPane = new AnchorPane(bStopsGridPane);
      scrollPane.setContent(anchorPane);
      scrollPane.setFitToHeight(true);
      scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
      titledPane.setContent(scrollPane);
      
      servicesAccordion.getPanes().add(titledPane);
    }
    
    servicesAccordion.setExpandedPane(servicesAccordion.getPanes().get(0));
  }
  
  public void clearTimetable() {
    servicesAccordion.getPanes().clear();
  }
  
  public void populateRoutesAccordionWithBusStop(BusStop stop) {
    for(Route route : tController.getRoutes()) {
      TitledPane titledPane = new TitledPane();
      titledPane.setText(route.toString());
      
      ScrollPane scrollPane = new ScrollPane();
      GridPane bStopsGridPane = new GridPane();
      int row = 0;
      int col = 0;
      
      ArrayList<Service2> routeServices = tController.getServicesOfRoute(route);

      bStopsGridPane.add(new Label(stop.toString()), col, row);
      bStopsGridPane.setHgap(10);
      bStopsGridPane.setVgap(3);
      
      ArrayList<Date> times = tController.getRouteBusStopTimes(route, stop);
      if(times!=null) {
 
        int timesCol = 1;
        for(Date time : times) {
          if(timesCol>18) {
            timesCol = 1;
            row++;
          }
          bStopsGridPane.add(new Label(DateHelper.formatDateToString("HH:mm", time)), timesCol++, row);
        }

        row++;
        
        AnchorPane anchorPane = new AnchorPane(bStopsGridPane);
        scrollPane.setContent(anchorPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        titledPane.setContent(scrollPane);
        
        servicesAccordion.getPanes().add(titledPane);
      }
    }
    
    servicesAccordion.setExpandedPane(servicesAccordion.getPanes().get(0));
  }
  
  public void populateBusStopChoiceBox() {
    busStopChoiceBox.setItems(
                    FXCollections.observableArrayList(tController.getAllBusStops()));
  }
  
  public void onBusStopChoiceChange() {
    busStopChoiceBox.getSelectionModel().selectedIndexProperty()
                              .addListener(new ChangeListener<Number>() {

      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        clearTimetable();
        populateRoutesAccordionWithBusStop(tController.getAllBusStops().get(newValue.intValue()));
      }
      
    });
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

  @Override
  public void refresh() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  
  
}
