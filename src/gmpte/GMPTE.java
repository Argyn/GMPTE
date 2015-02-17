/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte;

import java.text.ParseException;
import java.util.Date;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author argyn
 */
public class GMPTE extends Application {
    public TextField startDatePicker;
    public TextField endDatePicker;
    public HolidayController holidayController;
    
    @Override
    public void start(Stage primaryStage) {
        // initialize holiday controller
        holidayController = new HolidayController();
        
        BorderPane border = new BorderPane();
      
        // login form in the center
        StackPane root = new StackPane();
        root.getChildren().add(getCenter());
        
        Scene scene = new Scene(root, 900, 750);
        
        primaryStage.setTitle("GMPTE");
        primaryStage.setScene(scene);
        
        // load style-sheets
        scene.getStylesheets().add(
             getClass().getResource("style.css").toExternalForm()
        );
        
        primaryStage.show();
    }
    
    public GridPane getHolidayForm() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(50, 5, 50, 5));
        
        // Adding start-date
        Text startDateTitle = new Text("Start Date");
        startDateTitle.getStyleClass().add("start-date-label");
        grid.add(startDateTitle, 0, 1);
        
        startDatePicker = new TextField();
        grid.add(startDatePicker, 1, 1);
        
        // Adding end-date
        Text endDateTitle = new Text("End Date");
        endDateTitle.getStyleClass().add("end-date-label");
        grid.add(endDateTitle, 0, 2);
        
        endDatePicker = new TextField();
        grid.add(endDatePicker, 1, 2);
        
        grid.getStyleClass().add("holiday-form");
        grid.setPrefHeight(750);
        
        grid.add(getSubmitButton(),1,5);
        return grid;
    }
    
    public ImageView getLogo() {
        // loading the logo
        Image logoImage = new Image(
                        getClass().getResourceAsStream("logo.png")
                                    );
        
        // resize the image to have width 100 while
        // preserving the ratio
        ImageView logoIV = new ImageView();
        logoIV.setImage(logoImage);
        logoIV.setFitWidth(100);
        logoIV.setPreserveRatio(true);
        logoIV.setSmooth(true);
        logoIV.setCache(true);
        
        return logoIV;
    }
    
    public HBox getSubmitButton() {
        Button btn = new Button("Submit");
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().add(btn);
        btn.setAlignment(Pos.TOP_RIGHT);
        btn.setOnAction(getSubmitButtonHandler());
        btn.getStyleClass().add("holiday-submit-button");
        return hBox;
    }
    
    public HBox getTopHorizontalBox() {
        HBox hbox = new HBox();
        
        hbox.getChildren().addAll(getLogo());
        hbox.setPadding(new Insets(5,5,5,5));
        return hbox;
    }
    
    public VBox getCenter() {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPadding(new Insets(50, 150, 50, 150));
        vbox.getChildren().addAll(getTopHorizontalBox(), getHolidayForm());
        return vbox;
    }
    
    public EventHandler<ActionEvent> getSubmitButtonHandler() {
        return new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                Driver driver = new Driver(60167);
                
                Date startDate = null;
                Date endDate = null;
                
                try {
                   startDate = DateHelper.getDateFromString(startDatePicker.getText(),GMPTEConstants.DATE_FORMAT); 
                } catch(ParseException exception) {
                    
                    // Report error message
                }
                
                try {
                    endDate = DateHelper.getDateFromString(endDatePicker.getText(),GMPTEConstants.DATE_FORMAT);
                } catch(ParseException exception) {
                    // Report error message
                }
                
                if(startDate!=null && endDate!=null) {
                    // create holiday request
                    HolidayRequest holidayRequest = new HolidayRequest(driver,
                            startDate,
                            endDate);

                    // Call controller with request
                    processHolidayResponse(
                            holidayController.holidayRequest(holidayRequest)
                    );
                }
            }
        };
    }
    
    public void processHolidayResponse(HolidayRequestResponse response) {
        Text resultText;
        switch(response.getResponse()) {
            case GRANTED:
                
                resultText = new Text("Holiday Request Has Been Approved.");
                resultText.getStyleClass().addAll("result-text", "result-granted");
                resultText.setFill(Color.web("#43770c"));
                popRequestResultWindow("Holiday has been granted",
                                        resultText);
                break;
            case NOT_GRANTED:
                resultText = new Text("Holiday Request Has Been Declined.");
                resultText.getStyleClass().addAll("result-text", "result-not-granted");
                resultText.setFill(Color.web("#b61818"));
                popRequestResultWindow("Holiday has not been granted",
                                        resultText);
                break;
        }
    }
    
    public void popRequestResultWindow(String title, Text text) {
        Stage stage = new Stage();
        stage.setTitle(title);
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(text);
        
        Scene scene = new Scene(root, 900, 750);
        
        stage.setScene(scene);
        
        scene.getStylesheets().add(
             getClass().getResource("holiday-result.css").toExternalForm()
        );
        stage.show();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
    
    
}
