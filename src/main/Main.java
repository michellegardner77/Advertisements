package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("scenes/loginScene.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 320, 150));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
