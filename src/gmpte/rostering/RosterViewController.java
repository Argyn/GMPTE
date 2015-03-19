/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.rostering;

import eu.schudt.javafx.controls.calendar.DatePicker;
import gmpte.ControllerInterface;
import gmpte.GMPTEConstants;
import gmpte.MainControllerInterface;
import gmpte.db.BusInfo;
import gmpte.db.DriverInfo;
import gmpte.db.RosterDB;
import gmpte.db.ServiceDB;
import gmpte.db.database;
import gmpte.entities.Bus;
import gmpte.entities.Driver;
import gmpte.entities.Roster;
import gmpte.entities.Route;
import gmpte.entities.Service;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author mbgm2rm2
 */
public class RosterViewController implements Initializable, ControllerInterface {
  @FXML
  private GridPane rosterTable;
  
  @FXML
  private Button searchOptionsButton;
  
  @FXML
  private Button searchBoxCloseButton;
  
  @FXML
  private VBox searchOptionsBox;
  
  @FXML
  private ChoiceBox<Driver> driversChooseMenu;
  
  @FXML
  private ChoiceBox<Integer> routesChooseMenu;
  
  @FXML
  private HBox progressBarHbox;
  
  @FXML
  private ChoiceBox<Service> serviceChooseMenu;
  
  @FXML
  private GridPane searchOptionsGrid;
  
  @FXML
  private Button searchBySettingsButton;
  
  @FXML
  private ChoiceBox<Bus> busChoiceMenu;
  
  private DatePicker searchDatePicker;
  
  private MainControllerInterface mainController;
  
  
  @FXML
  private TextField durationTextField;
  
  /* Search Fields Values */
  
  private Driver searchByDriver;
  
  private Integer searchByRoute;
  
  private Service searchByService;
  
  private Date searchByDate;
  
  private int searchByDuration;
  
  
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    
    // listen click event for options button
    onSearchOptionsButtonClick();
    
    // listen search options close button
    onSearchOptionCloseButtonClick();
    
    // populate search options
    Thread th = populateSearchOptions();
    
    // add options change listeners
    addOptionsChangeListeners();
    
    // show global roster
    showRoster(th);
    
    
  }  

  @Override
  public void setMainController(MainControllerInterface mainController) {
    this.mainController = mainController;
  }
  
  
  public void showRoster(final Thread waitThread) {
    final Task<ArrayList<Roster>> task = new Task<ArrayList<Roster>>() {

      @Override
      protected ArrayList<Roster> call() throws Exception {
        waitThread.join();
        
        return RosterDB.getGlobalRoster();
      };
    };
    
    // On failed data loading
    task.setOnFailed(new EventHandler<WorkerStateEvent>() {

      @Override
      public void handle(WorkerStateEvent event) {
        task.getException().printStackTrace();
      }
    });
    
    // On succeeded data loading
    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

      @Override
      public void handle(WorkerStateEvent event) {
        Iterator<Roster> rosterIt = task.getValue().iterator();
        int row = 0;
        for(; rosterIt.hasNext();) {
          final Roster roster = rosterIt.next();
          displaySingleRoster(roster, row++);
        }
        
        progressBarHbox.setVisible(false);
      }
    });
    
    new Thread(task).start();
  }
  
  public void displaySingleRoster(Roster roster, int row) {
    int column = 0;
    
    Label driverName = new Label(roster.getDriver().getName());
    applyLabelStyle(driverName);
    rosterTable.add(wrapLabelInHBox(driverName, Pos.TOP_LEFT), column++, row);
    
    Label route = new Label(Integer.toString(roster.getRoute().getRouteID()));
    applyLabelStyle(route);
    rosterTable.add(wrapLabelInHBox(route, Pos.CENTER), column++, row);
    
    Label service = new Label(Integer.toString(roster.getService().getServiceId()));
    applyLabelStyle(service);
    rosterTable.add(wrapLabelInHBox(service, Pos.CENTER), column++, row);
    
    DateFormat dateFormat = new SimpleDateFormat(GMPTEConstants.DATE_FORMAT);
    Label dateLabel = new Label(dateFormat.format(roster.getDate()));
    applyLabelStyle(dateLabel);
    rosterTable.add(wrapLabelInHBox(dateLabel, Pos.CENTER), column++, row);
    
    Label durationLabel = new Label(Integer.toString(roster.getServiceTime()));
    applyLabelStyle(durationLabel);
    rosterTable.add(wrapLabelInHBox(durationLabel, Pos.CENTER), column++, row);
    
    dateFormat = new SimpleDateFormat(GMPTEConstants.DAY_TIME_FORMAT);
    Label startTimeLabel = new Label(dateFormat.format(roster.getService().getStartTimeDate()));
    applyLabelStyle(startTimeLabel);
    rosterTable.add(wrapLabelInHBox(startTimeLabel, Pos.CENTER), column++, row);
    
    Label endTimeLabel = new Label(dateFormat.format(roster.getService().getEndTimeDate()));
    applyLabelStyle(endTimeLabel);
    rosterTable.add(wrapLabelInHBox(endTimeLabel, Pos.CENTER), column++, row);
    
    Label busLabel = new Label(Integer.toString(roster.getBus().getBusNumber()));
    applyLabelStyle(busLabel);
    rosterTable.add(wrapLabelInHBox(busLabel, Pos.CENTER), column++, row);

  }
  
  public HBox wrapLabelInHBox(Label label, Pos pos) {
    HBox hbox = new HBox();
    hbox.setAlignment(pos);
    hbox.getChildren().add(label);
    return hbox;
  }
  
  public void applyLabelStyle(Label label) {
    label.getStyleClass().add(GMPTEConstants.CSS_MEDIUM_LABEL);
    label.setWrapText(true);
  }
  
  public void onSearchOptionsButtonClick() {
    searchOptionsButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent t) {
        searchOptionsBox.setVisible(true);
      }
      
    });
  }
  
  public void onSearchOptionCloseButtonClick() {
    searchBoxCloseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        searchOptionsBox.setVisible(false);
      }
      
    });
  }

  public Thread populateSearchOptions() {
    // populate with drivers 
    Thread th = populateDriversSearchOption();
    
    // populate route options
    populateRoutesOptions();
    
    // populate services options
    th = populateServicesOptions(th);
    
    // populate buses search option
    th = populateBusesSearchOption(th);
    
    // put date picker for date
    populateSearchDateOption();
    return th;
  }
  
  private Thread populateDriversSearchOption() {
    
    final Task<ArrayList<Driver>> task = new Task<ArrayList<Driver>>() {

      @Override
      protected ArrayList<Driver> call() throws Exception {
        String[] orderBy = {"name"};
        String[] order = {"asc"};

        return DriverInfo.fetchDrivers(new String[]{}, new String[]{}, 
                                                                orderBy, order);
      }
      
    };
    
    
    // On failed data loading
    task.setOnFailed(new EventHandler<WorkerStateEvent>() {

      @Override
      public void handle(WorkerStateEvent event) {
        task.getException().printStackTrace();
      }
    });
    
    // On success put drivers in choose menu
    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        task.getValue().add(0, null);
        ObservableList<Driver> oListDrivers = FXCollections.observableArrayList(task.getValue());
        driversChooseMenu.setItems(oListDrivers);
      }
    });
    
    Thread th = new Thread(task);
    th.start();
    
    return th;
  }
  
  private void populateRoutesOptions() {
    ArrayList<Integer> routes = new ArrayList<Integer>(Arrays.asList(GMPTEConstants.ROUTES));
    routes.add(0, null);
    routesChooseMenu.setItems(FXCollections.observableArrayList(routes));
  }
  
  private Thread populateServicesOptions(final Thread waitThread) {
    final Task<ArrayList<Service>> task = new Task<ArrayList<Service>>() {
        
      @Override
      protected ArrayList<Service> call() throws Exception {
        waitThread.join();
        return ServiceDB.fetchAllServices();
      }
      
    };
    
    // On failed data loading
    task.setOnFailed(new EventHandler<WorkerStateEvent>() {

      @Override
      public void handle(WorkerStateEvent event) {
        task.getException().printStackTrace();
      }
    });
    
    // On success put drivers in choose menu
    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        task.getValue().add(0, null);
        ObservableList<Service> oListServices = FXCollections.observableArrayList(task.getValue());
        serviceChooseMenu.setItems(oListServices);
      }
    });
    
    Thread th = new Thread(task);
    th.start();
    
    return th;
  }
  
  private Thread populateBusesSearchOption(final Thread waitThread) {
    
    final Task<ArrayList<Bus>> task = new Task<ArrayList<Bus>>() {
        
      @Override
      protected ArrayList<Bus> call() throws Exception {
        
        if(waitThread!=null)
          waitThread.join();
        
        return BusInfo.fetchAllBuses();
      }
      
    };
    
    // On failed data loading
    task.setOnFailed(new EventHandler<WorkerStateEvent>() {

      @Override
      public void handle(WorkerStateEvent event) {
        task.getException().printStackTrace();
      }
    });
    
    // On success put drivers in choose menu
    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        task.getValue().add(0, null);
        ObservableList<Bus> oListBuses = FXCollections.observableArrayList(task.getValue());
        busChoiceMenu.setItems(oListBuses);
      }
    });
    
    Thread th = new Thread(task);
    th.start();
    
    return th;
  }
  
  private void populateSearchDateOption() {
    searchDatePicker = new DatePicker();
    searchOptionsGrid.add(searchDatePicker, 1, 4);
  }
  
  private void addOptionsChangeListeners() {
    // handle search button
    onSearchBySettingsButtonClicked();
  }
  
  private void onSearchBySettingsButtonClicked() {
    searchBySettingsButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent t) {
        Driver driver = driversChooseMenu.getValue();
        Integer route = routesChooseMenu.getValue();
        Service service = serviceChooseMenu.getValue();
        Integer duration = null;
        Bus bus = busChoiceMenu.getValue();
        
        if(!durationTextField.getText().equals(""))
          duration = Integer.parseInt(durationTextField.getText());
        Date date = searchDatePicker.getSelectedDate();
        
        searchOptionsBox.setVisible(false);
        
        showOptionsRoster(driver, route, bus, service, duration, date);
      }
    });
  }
  
  public void showOptionsRoster(final Driver driver, final Integer route, final Bus bus, 
                                final Service service, final Integer duration, 
                                final java.util.Date date) {
    final Task<ArrayList<Roster>> task = new Task<ArrayList<Roster>>() {

      @Override
      protected ArrayList<Roster> call() throws Exception {
        return RosterDB.getRosterBy(driver, route, bus, service, duration, date);
      };
    };
    
    // On failed data loading
    task.setOnFailed(new EventHandler<WorkerStateEvent>() {

      @Override
      public void handle(WorkerStateEvent event) {
        task.getException().printStackTrace();
      }
    });
    
    // On succeeded data loading
    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

      @Override
      public void handle(WorkerStateEvent event) {
        Iterator<Roster> rosterIt = task.getValue().iterator();
        int row = 0;
        for(; rosterIt.hasNext();) {
          final Roster roster = rosterIt.next();
          displaySingleRoster(roster, row++);
        }
        
        progressBarHbox.setVisible(false);
      }
    });
    
    clearRosterTable();
    progressBarHbox.setVisible(true);
    
    new Thread(task).start();
  }
  
  public void clearRosterTable() {
    rosterTable.getChildren().clear();
  }
  
  @Override
  public void refresh() {

  }
  
  
}
