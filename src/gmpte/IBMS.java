/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;

import gmpte.databaseinterface.database;
import gmpte.holidayrequest.HolidayController;
import gmpte.holidayrequest.HolidayRequestController;
import gmpte.login.LoginController;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author mbgm2rm2
 */
public class IBMS extends Application implements MainControllerInterface {
    public Stage stage;
    
    private HolidayController holidayController;
    
    private LoginController loginController;
    
    private HolidayRequestController holidayRequestController;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        holidayController = new HolidayController();
        
        // open database connection
        database.openBusDatabase();
        
        stage = primaryStage;
        
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
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void onSuccessfullLogin() throws Exception {
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
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
}
