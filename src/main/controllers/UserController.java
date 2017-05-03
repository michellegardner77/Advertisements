package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by mgard on 5/2/2017.
 */
public class UserController {
    public TextField titleDescTextField;
    public TableView advTable;
    public TableView userAdvTable;

    //TODO: Overload init method to load table data?

    public void goButtonPressed(ActionEvent actionEvent) {

    }

    public void editUserAdvButtonPressed(ActionEvent actionEvent) {

    }

    public void deleteUserAdvButtonPressed(ActionEvent actionEvent) {

    }

    public void addAdvButtonPressed(ActionEvent actionEvent) {

        try {
            // STAGE (window)
            // Create new stage for advertisementStage
            Stage advertisementStage = new Stage();
            // Set Stage title (Window title)
            advertisementStage.setTitle("Add Advertisement");
            // made stage / window not resizable
            advertisementStage.setResizable(false);

            // STAGE Contents (Scene)
            // Load fxml file to generate scene (Make sure file patch is correct)
            Parent addAdvertisementSceneFxml = FXMLLoader.load(getClass().getResource("../scenes/AddAdvertisementScene.fxml"));
            // Create new scene passing it the loaded Parent FXML object (file)
            Scene addAdvertisementScene = new Scene(addAdvertisementSceneFxml);
            // Set the advertisementStage to use the stage advertisementStage
            // Think of the stage as the window with the "X" button and the scene the contents inside
            advertisementStage.setScene(addAdvertisementScene);


            // setting new stage as a modal popup which will disable the primary stage until closed
            advertisementStage.initModality(Modality.APPLICATION_MODAL);
            // Show the advertisementStage which already has the userScene FXML loaded
            advertisementStage.showAndWait();

        } catch (IOException ex) {
            //TODO: deal with later.
            // Could be issues loading FXML file or something.
            ex.printStackTrace();
        }

    }
}
