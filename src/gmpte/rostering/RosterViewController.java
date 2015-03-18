/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.rostering;

import gmpte.ControllerInterface;
import gmpte.GMPTEConstants;
import gmpte.MainControllerInterface;
import gmpte.db.database;
import gmpte.entities.Roster;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
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
  private VBox searchOptionsBox;
  
  private MainControllerInterface mainController;
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
    //ColumnConstraints col1 = new ColumnConstraints();
    //col1.setPercentWidth(40);
    
    //rosterTable.getColumnConstraints().addAll(col1, col1, col1, col1, col1);
    
    
    // listen click event for options button
    onSearchOptionsButtonClick();
    
    // show global roster
    showRoster();
    
    
  }  

  @Override
  public void setMainController(MainControllerInterface mainController) {
    this.mainController = mainController;
  }
  
  public void showRoster() {
    String[] orderBy = {"date"};
    String[] order = {"desc"};
    
    // fetching the roster
    ArrayList<Roster> rosters = database.busDatabase.getGlobalRoster(orderBy, order);
    
    Iterator<Roster> rosterIt = rosters.iterator();
    int row = 0;
    for(; rosterIt.hasNext();) {
      Roster roster = rosterIt.next();
      displaySingleRoster(roster, row++);
    }
  }
  
  public void displaySingleRoster(Roster roster, int row) {
    int column = 0;
    
    Label driverName = new Label(roster.getDriver().getName());
    applyLabelStyle(driverName);
    rosterTable.add(wrapLabelInHBox(driverName, Pos.TOP_LEFT), column++, row);
    
    Label route = new Label(Integer.toString(roster.getService().getRoute()));
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
    
  }
  
  public HBox wrapLabelInHBox(Label label, Pos pos) {
    HBox hbox = new HBox();
    hbox.setAlignment(pos);
    hbox.getChildren().add(label);
    return hbox;
  }
  
  public void applyLabelStyle(Label label) {
    label.getStyleClass().add(GMPTEConstants.CSS_MEDIUM_LABEL);
  }
  
  public void onSearchOptionsButtonClick() {
    searchOptionsButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent t) {
        searchOptionsBox.setVisible(true);
      }
      
    });
  }
  
  
}
