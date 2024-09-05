package gui;

import gui.grid.GridBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        GridBuilder gridBuilder = new GridBuilder(50, 20, 35, 110);
        BorderPane root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("resources/MainAppView.fxml")));
        root.setCenter(gridBuilder.build());
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Hello SASON");
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("resources/naruto-wallpaper-2560x1440.jpg"))));
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
