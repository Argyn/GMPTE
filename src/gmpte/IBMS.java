/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;

import gmpte.db.database;
import gmpte.holidayrequest.HolidayController;
import gmpte.holidayrequest.HolidayRequestController;
import gmpte.login.LoginController;
import gmpte.login.LoginCredentials;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author mbgm2rm2
 */
public class IBMS extends Application implements MainControllerInterface {
  public Stage stage;

  private HolidayController holidayController;

  private LoginController loginController;
  
  private StartupController startUpController;
  
  private HolidayRequestController holidayRequestController;
  
  private ControllerInterface controllerInterfaceController;
  
  private ControllerInterface driverInterfaceController;
  
  private ControllerInterface rosterViewController;
  
  private Scene mainPageScene;
  private Scene loginPageScene;
  private Scene startUpPageScene;
  private Scene holidayRequestPageScene;
  private Scene controllerInterfacePageScene;
  private Scene driverInterfacePageScene;
  private Scene rosterViewScene;
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    
    holidayController = new HolidayController();

    // open database connection
    database.openBusDatabase();

    stage = primaryStage;
    
    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
    
    //stage.setMaxWidth((double)visualBounds.getWidth() / 1.5);
    //stage.setMaxHeight((double)visualBounds.getHeight() / 1.2);
    
    stage.setMinWidth(900);
    stage.setMinHeight(700);
     
    // load fonts
    loadFonts();
    
    // show login page
    //showLoginPage();
    showMainPage();
  }
  
  @Override
  public void showLoginPage() {
    try {
      
      if(loginPageScene==null) {
        
        
        // Load login page first
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
              "/resources/Login.fxml"
            )
        );

        Parent root = (Parent)loader.load();

        loginController = 
            loader.<LoginController>getController();
        loginController.setMainController(this);
      
        loginPageScene = new Scene(root);

      }

      stage.setScene(loginPageScene);
      
      // set window title
      setWindowTitle(stage, GMPTEConstants.IBMS_SYSTEM+" : "+GMPTEConstants.LOGIN_WINDOW_TITLE);

      stage.show();
    } catch(IOException exception) {
      // close the programm, report the error
      System.out.println(exception.getMessage());
    }
    
  }
  
  @Override
  public void showMainPage() {
   
    try {
      if(mainPageScene==null) {
        // Load login page first
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
              "/resources/Main.fxml"
            )
        );

        Parent root = (Parent)loader.load();

        startUpController = 
            loader.<StartupController>getController();
        startUpController.setMainController(this);
      
      
        mainPageScene = new Scene(root);
      }

      stage.setScene(mainPageScene);

      // set window title
      setWindowTitle(stage, GMPTEConstants.IBMS_SYSTEM+" : "+GMPTEConstants.LOGIN_WINDOW_TITLE);

      stage.show();
    } catch(IOException exception) {
      // exit the program, report the error
    }
  }
  
  @Override
  public void showHolidayRequestPage() {
    try {
      if(holidayRequestPageScene==null) {
        // Load login page first
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
              "/resources/HolidayRequest.fxml"
            )
        );


        Parent root = (Parent)loader.load();

        holidayRequestController = 
            loader.<HolidayRequestController>getController();
        
        holidayRequestController.setHolidayController(holidayController);
        holidayRequestController.setMainController(this);
        
        holidayRequestPageScene = new Scene(root);
        holidayRequestPageScene.getStylesheets().add("/resources/calendarstyle.css");
      }

      stage.setScene(holidayRequestPageScene);

      // set window title
      setWindowTitle(stage, GMPTEConstants.IBMS_SYSTEM+" : "+GMPTEConstants.HOLIDAY_REQUEST_WINDOW_TITLE);

      stage.show();
    } catch(IOException exception) {
      // close the programm
      
    }
  }
  
  @Override
  public void showControllerInterface() {
    try {
      if(controllerInterfacePageScene==null) {
        // Load login page first
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
              "/resources/ControllerInterface.fxml"
            )
        );


        Parent root = (Parent)loader.load();

        controllerInterfaceController = 
            loader.<ControllerInterface>getController();

        controllerInterfaceController.setMainController(this);
        
        controllerInterfacePageScene = new Scene(root);
      }
      

      stage.setScene(controllerInterfacePageScene);

      // set window title
      setWindowTitle(stage, GMPTEConstants.IBMS_SYSTEM+" : "+GMPTEConstants.CONTROLLER_INTERFACE_WINDOW);

      stage.show();
    } catch(IOException exception) {
      // close the programm
      
    }
  }
  
  @Override
  public void showDriverInterface() {
    try {
      if(driverInterfacePageScene==null) {
        // Load login page first
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
              "/resources/DriverInterface.fxml"
            )
        );


        Parent root = (Parent)loader.load();

        driverInterfaceController = 
            loader.<ControllerInterface>getController();

        driverInterfaceController.setMainController(this);
        
        driverInterfacePageScene = new Scene(root);
      }
      

      stage.setScene(driverInterfacePageScene);

      // set window title
      setWindowTitle(stage, GMPTEConstants.IBMS_SYSTEM+" : "+GMPTEConstants.DRIVER_INTERFACE_WINDOW);

      stage.show();
    } catch(IOException exception) {
      // close the programm
      
    }
  }
  
  @Override
  public void showRosterView() {
    try {
      if(rosterViewScene==null) {
        // Load login page first
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
              "/resources/RosterView.fxml"
            )
        );


        Parent root = (Parent)loader.load();

        rosterViewController = 
            loader.<ControllerInterface>getController();

        rosterViewController.setMainController(this);
        
        rosterViewScene = new Scene(root);
      }
      

      stage.setScene(rosterViewScene);

      // set window title
      setWindowTitle(stage, GMPTEConstants.IBMS_SYSTEM+" : "+GMPTEConstants.ROSTER_VIEW_WINDOW);

      stage.show();
    } catch(IOException exception) {
      // close the programm
      
    }
  }
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void onSuccessfullLogin() throws Exception {
    ///showHolidayRequestPage();
    showDriverInterface();
  }

  public void setWindowTitle(Stage stage, String title) {
    stage.setTitle(title);
  }

  @Override
  public void onLogOut() throws IOException {
    LoginCredentials.getInstance().setDriver(null);
    showLoginPage();
  }
  
  public void loadFonts() {
    Font.loadFont(IBMS.class.getResource("/resources/OpenSans-Regular.ttf").toExternalForm(), 10);
  }
}
