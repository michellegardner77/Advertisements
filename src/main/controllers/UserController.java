package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.models.Advertisements;
import main.models.User;
import main.services.DBManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by mgard on 5/2/2017.
 */
public class UserController {
    public TextField titleDescTextField;
    public TableView advTable;
    public TableView userAdvTable;
    public ComboBox catComBox;
    public ComboBox periodComBox;

    private User user;
    private DBManager dbManager;

    // method to pass DBManager to controller
    void setDbManager(DBManager dbManager){
        this.dbManager = dbManager;
    }

    // method to pass current user to controller
    void setUser(User user) {
        this.user = user;
    }

    // Method to get the values to pass into database statement for period where clause
    private String getPeriodComboBoxQueryValue() {

        String periodComBoxSelectionValue = periodComBox.getValue().toString();

        // Deal with either ALL time period or specific period (3, 6, 12, ...)
        // check if string is number
        // if string is then add it to period filter
        switch(periodComBoxSelectionValue) {
            case "3 Months":
                return "3";
            case "6 Months":
                return "6";
            case "12 Months":
                return "12";
            default:
                return "CURRENT_DATE()";
        }
    }

    // Method to get the values to pass into database for category where clause
    private String getCatComboBoxQueryValue() {

        String catComBoxSelectionValue = catComBox.getValue().toString();

        // check if cat passed in is All for a wild card search or a specified one
        if (Objects.equals(catComBoxSelectionValue, "All")) {
            // if all set to wildcard to get all cats
            return "%";
        } else {
            return catComBoxSelectionValue;
        }
    }

    // Method to get the values to pass into database for title/desc where clause
    private String getTitleDescriptionQueryValue(){
        // got title desc text field value
        String titleDescValue = titleDescTextField.getText();

        // if null set it to wildcard for sql statement
        if(titleDescValue.isEmpty()){
            titleDescValue = "%";
        }

        return titleDescValue;
    }

    public void reloadTable(){
        String catQueryValue = getCatComboBoxQueryValue();
        String periodQueryValue = getPeriodComboBoxQueryValue();
        String titleDescQueryValue = getTitleDescriptionQueryValue();

        List<Advertisements> advList = dbManager.getAdvertisements(catQueryValue, periodQueryValue, titleDescQueryValue);


    }

    public void goButtonPressed(ActionEvent actionEvent) {
        reloadTable();
    }

    public void editUserAdvButtonPressed(ActionEvent actionEvent) {

    }

    public void deleteUserAdvButtonPressed(ActionEvent actionEvent) {

    }

    public void addAdvButtonPressed(ActionEvent actionEvent) {

        try {
            // STAGE (window)
            // Create new stage fro addAdvertisementStage
            Stage addAdvertisementStage = new Stage();
            // Set Stage title (Window title)
            addAdvertisementStage.setTitle("Username: " + user.getUser_ID());
            // made stage / window not resizable
            addAdvertisementStage.setResizable(false);

            // STAGE Contents (Scene)
            // Load fxml file to FMXLoader(Make sure file patch is correct)
            FXMLLoader addAdvertisementFXMLLoader = new FXMLLoader(getClass().getResource("../scenes/AddAdvertisementScene.fxml"));

            // Create initial pane (anchorPane) for scene object.
            AnchorPane addAdvertisementAnchorPane = addAdvertisementFXMLLoader.load();

            // Get instance of addAdvertisementController to call it's methods to set properties
            AddAdvertisementController addAdvertisementController = addAdvertisementFXMLLoader.getController();

            // set db manager property on userController
            addAdvertisementController.setDbManager(dbManager);
            // ser user property on userController
            addAdvertisementController.setUser(user);

            // Create new scene passing it the AnchorPane object
            Scene addAdvertisementScene = new Scene(addAdvertisementAnchorPane);

            // Set the addAdvertisementStage to use the stage userStage
            // Think of the stage as the window with the "X" button and the scene the contents inside
            addAdvertisementStage.setScene(addAdvertisementScene);


            // setting new stage as a modal popup which will disable the primary stage until closed
            addAdvertisementStage.initModality(Modality.APPLICATION_MODAL);
            // Show the addAdvertisementStage which already has the userScene FXML loaded
            addAdvertisementStage.showAndWait();

        } catch (IOException ex) {
            //TODO: deal with later.
            // Could be issues loading FXML file or something.
            ex.printStackTrace();
        }

    }
}
