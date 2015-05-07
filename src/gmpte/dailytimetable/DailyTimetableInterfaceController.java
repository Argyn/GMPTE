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
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
  
  @FXML
  private Label refreshTimerLabel;
  
  @FXML
  private Text marqueueMessagesText;
  
  @FXML
  private HBox marqueueMessagesBox;
  
  private DailyTimetableController tController;
  
  private MainControllerInterface mainController;
 
  private ArrayList<BusStop> busStopsChoices;
  
  private TranslateTransition marqueueTransition;
  
  private BusStop currentBusStop;
  
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
        
        busStopsChoices = new ArrayList<>(tController.getAllBusStops());
        busStopsChoices.add(0, null);
        
        // populate accordion services
        populateRoutesAccordionWithAllBusStops();
        
        populateBusStopChoiceBox();
        
        onBusStopChoiceChange();
        
        constructMarqueueMessages();
        
        showMarqueueMessage();
        
        refreshData();
        
        new Thread(new DelayCancelController(tController.getAllServices())).start();
        
        
      }
    });
   
    new Thread(loadTask).start();
  }
  
  public void refreshData() {
    
    final Task<Void> loadTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        int seconds = 50;
        while(seconds>0) {
          Thread.sleep(1000);
          seconds-=1;
          //refreshTimerLabel.setText(seconds+" seconds");
        }
        loadingIndicatorPane.setVisible(true);
        mainContentPane.setVisible(false);

        tController.refresh();
        
        constructMarqueueMessages();
        
        showMarqueueMessage();
        
        return null;
      }
    };
    
    loadTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

      @Override
      public void handle(WorkerStateEvent event) {
        loadingIndicatorPane.setVisible(false);
        mainContentPane.setVisible(true);
        
        clearTimetable();
        
        if(currentBusStop!=null) {
          populateRoutesAccordionWithBusStop(currentBusStop);
        } else {
          populateRoutesAccordionWithAllBusStops();
        }
        
        refreshData();
      }
    });
   
    new Thread(loadTask).start();
  }
  
  public void populateRoutesAccordionWithAllBusStops() {
    
    Date nowDate = new Date();
    
    for(Route route : tController.getRoutes()) {
      TitledPane titledPane = new TitledPane();
      titledPane.setText(route.toString());
      
      GridPane bStopsGridPane = new GridPane();
      int row = 0;
      int col = 0;
      
      ArrayList<Service2> routeServices = tController.getServicesOfRoute(route);
      
      for(BusStop stop : tController.getBusStopsOfRoute(route)) {
        
        
        Label firstToArrive = null;
        Date firstToArriveDate = null;
        
        Label bStopLabel = new Label(stop.toString());
        
        bStopsGridPane.add(bStopLabel, col, row);
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
               
             
              
              if(!service.doesTerminateAtTime(arriveTime)) {
                timeLabel.getStyleClass().add("timeteable-time-depart-label");
                if(arriveTime.after(nowDate)) { 
                  if(firstToArriveDate==null || (firstToArriveDate!=null 
                                    && arriveTime.before(firstToArriveDate))) {
                    firstToArrive = timeLabel;
                    firstToArriveDate = arriveTime;
                  }
                }
              } else
                timeLabel.getStyleClass().add("timetable-time-arrive-label");
              
              if(service.isDelayed() && bStop.getIds().get(0)>=service.getDelayPoint()) {
                
                timeLabel.setText(timeLabel.getText()+"(D)");
                timeLabel.getStyleClass().add("delayed-service-time-label");
              }
              
              if(service.isCancelled()) {
                timeLabel.setText(timeLabel.getText()+"(C)");
                timeLabel.getStyleClass().add("cancelled-service-time-label");
              }
              
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
        
        if(firstToArrive!=null)
          firstToArrive.getStyleClass().add("next-service-time-label");
      }
      
      ScrollPane scrollPane = new ScrollPane();
      AnchorPane anchorPane = new AnchorPane();
      anchorPane.getChildren().add(bStopsGridPane);
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
    Date nowDate = new Date();

    Label firstToArrive = null;
    Date firstToArriveDate = null;
   
    for(Route route : tController.getRoutes()) {
      firstToArrive = null;
      firstToArriveDate = null;
      
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
      
      int timesCol = 1;
      boolean hasServices = false;
      for(Service2 service : routeServices) {
        int index = 0;
        for(BusStop bStop : service.getBusStops()) {
          if(stop.equals(bStop)) {
            hasServices= true;
            Date arriveTime = service.getTimes().get(index);
              Label timeLabel = new Label(DateHelper.formatDateToString("HH:mm", 
              arriveTime));
            
              
              if(!service.doesTerminateAtTime(arriveTime)) {
                timeLabel.getStyleClass().add("timeteable-time-depart-label");
                if(arriveTime.after(nowDate)) { 
                  if(firstToArriveDate==null || (firstToArriveDate!=null 
                                    && arriveTime.before(firstToArriveDate))) {
                    firstToArrive = timeLabel;
                    firstToArriveDate = arriveTime;
                  }
                }
              } else
                timeLabel.getStyleClass().add("timetable-time-arrive-label");
              
              if(service.isDelayed() && bStop.getIds().get(0)>=service.getDelayPoint()) {
                timeLabel.setText(timeLabel.getText()+"(D)");
                timeLabel.getStyleClass().add("delayed-service-time-label");
              }
              
              if(service.isCancelled()) {
                timeLabel.setText(timeLabel.getText()+"(C)");
                timeLabel.getStyleClass().add("cancelled-service-time-label");
              }
              
              bStopsGridPane.add(timeLabel, timesCol++, row);
          }
          index++;
          
          
        }

        if(timesCol>12) {
          timesCol = 1;
          row++;
        }
      }
      
      if(hasServices) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(bStopsGridPane);
        scrollPane.setContent(anchorPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        titledPane.setContent(scrollPane);
        
        servicesAccordion.getPanes().add(titledPane);
        
        if(firstToArrive!=null)
            firstToArrive.getStyleClass().add("next-service-time-label");
        
      }
    }
    
    //servicesAccordion.setExpandedPane(servicesAccordion.getPanes().get(0));
  }
  
  public void populateBusStopChoiceBox() {
    busStopChoiceBox.setItems(
                    FXCollections.observableArrayList(busStopsChoices));
  }
  
  public void onBusStopChoiceChange() {
    busStopChoiceBox.getSelectionModel().selectedIndexProperty()
                              .addListener(new ChangeListener<Number>() {

      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        clearTimetable();
        
        if(newValue.intValue()==-1)
          currentBusStop = null;
        else
          currentBusStop = busStopsChoices.get(newValue.intValue());
        
        if(currentBusStop!=null)
          populateRoutesAccordionWithBusStop(currentBusStop);
        else
          populateRoutesAccordionWithAllBusStops();
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
  
  public void constructMarqueueMessages() {
    marqueueMessagesText.setText("");
    for(Service2 service : tController.getCancelledServices()) {
      String currentText = marqueueMessagesText.getText();
      if(currentText.length()>0)
        currentText+=";  ";
      
      marqueueMessagesText.setText(currentText+"Service "+service.getId()+" has been cancelled "+service.getReason());
    }
    
    for(Service2 service : tController.getDelayedServices()) {
      String currentText = marqueueMessagesText.getText();
      if(currentText.length()>0)
        currentText+=";  ";
      
      marqueueMessagesText.setText(currentText+"Service "+service.getId()+" on route "+service.getRoute()+" is delayed by "
                            +service.getDelayTime()+" minutes "+service.getReason());
    }
  }
  
  
  public void showMarqueueMessage() {
    marqueueTransition = TranslateTransitionBuilder.create()
        .duration(new Duration(10))
        .node(marqueueMessagesText)
        .interpolator(Interpolator.LINEAR)
        .cycleCount(1)
        .build();

    marqueueTransition.setOnFinished(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            rerunAnimation();
        }
    });

    rerunAnimation();
  }
  
  private void rerunAnimation() {
    marqueueTransition.stop();
    // if needed set different text on "node"
    recalculateTransition();
    marqueueTransition.playFromStart();
  }
  
  private void recalculateTransition() {
    marqueueTransition.setToX(marqueueMessagesBox.getBoundsInLocal().getMaxX() * -1 - 100);
    marqueueTransition.setFromX(1200 + 100);

    double distance = marqueueMessagesBox.widthProperty().get() + 2 * marqueueMessagesText.getBoundsInLocal().getMaxX();
    marqueueTransition.setDuration(new Duration(distance / 0.1));
  }
  
  
  
}
