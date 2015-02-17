package gmpte.login;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 *
 * @author Rauan Mukhamejanov
 */
public class DriverLogin extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        URL location = getClass().getResource("Login.fxml");
        Parent root = FXMLLoader.load(location);
        
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
    
}

