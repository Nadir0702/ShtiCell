package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(Main.class.getResource("resources/MainAppView.fxml"));
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Hello World");
        stage.setScene(scene);
        stage.show();
    }
    
  
    
    public static void main(String[] args) {
        launch(args);
    }
}
