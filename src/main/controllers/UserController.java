package main.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.models.Advertisements;
import main.models.User;
import main.services.DBManager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Created by mgard on 5/2/2017.
 */
public class UserController {
    public TextField titleDescTextField;
    public TableView<Advertisements> allAdvTableView;
    public TableView userAdvTable;
    public ComboBox catComBox;
    public ComboBox periodComBox;
    // user adv table cols
    public TableColumn<Advertisements, String> userAdvTitleCol;
    public TableColumn<Advertisements, String> userAdvDescCol;
    public TableColumn<Advertisements, Double> userAdvPriceCol;
    public TableColumn<Advertisements, String> userAdvDateCol;
    // my adv table cols
    public TableColumn<Advertisements, Integer> userMyAdvIDCol;
    public TableColumn<Advertisements, String> userMyTitleCol;
    public TableColumn<Advertisements, String> userMyAdvDescCol;
    public TableColumn<Advertisements, Double> userMyPriceCol;
    public TableColumn<Advertisements, String> userMyStatusCol;
    public TableColumn<Advertisements, String> userMyAdvDate;
    public TabPane userTabPane;

    private User user;
    private DBManager dbManager;

    // List if filtered Adv for All Adv list
    private List<Advertisements> filteredAllAdvList = null;
    // ObservableList of advs to populate all adv table view
    private ObservableList<Advertisements> advertisementsObservableList = null;

    private int selectedTabIndex;

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

    void reloadAllAdvTable(){
        // Get selected cat query value to pass SQL statement
        String catQueryValue = getCatComboBoxQueryValue();
        // Get selected period query value to pass SQL statement
        String periodQueryValue = getPeriodComboBoxQueryValue();
        // Get selected title/desc query value to pass SQL statement
        String titleDescQueryValue = getTitleDescriptionQueryValue();

        // Get advertisements for advTable based on users filter selections
        filteredAllAdvList = dbManager.getAdvertisements(catQueryValue, periodQueryValue, titleDescQueryValue);

        // Add filtered Advs to ObservableList to populate table
        advertisementsObservableList = FXCollections.observableList(filteredAllAdvList);

        // specify what properties to set the table columns
        // You enter in the NAME of the class property. ex: Advertisements has a advTitle property
        userAdvTitleCol.setCellValueFactory(new PropertyValueFactory<>("advTitle"));
        userAdvDescCol.setCellValueFactory(new PropertyValueFactory<>("advDetails"));
        userAdvPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        userAdvDateCol.setCellValueFactory(new PropertyValueFactory<>("advDateTime"));

        // Initially set the Adv table
        allAdvTableView.setItems(advertisementsObservableList);
    }

    public void goButtonPressed(ActionEvent actionEvent) {
        reloadAllAdvTable();
    }

    public void editUserAdvButtonPressed(ActionEvent actionEvent) {
        // TODO: get row selected information
        try {
            // STAGE (window)
            // Create new stage for userEditAdvertisementStage
            Stage userEditAdvertisementStage = new Stage();
            // Set Stage title (Window title)
            userEditAdvertisementStage.setTitle("Username: " + user.getUser_ID());
            // made stage / window not resizable
            userEditAdvertisementStage.setResizable(false);

            // STAGE Contents (Scene)
            // Load fxml file to FMXLoader(Make sure file patch is correct)
            FXMLLoader userEditAdvertisementFXMLLoader = new FXMLLoader(getClass().getResource("../scenes/UserEditAdvertisementScene.fxml"));

            // Create initial pane (anchorPane) for scene object.
            AnchorPane userEditAdvertisementAnchorPane = userEditAdvertisementFXMLLoader.load();

            // Get instance of userEditAdvertisementController to call it's methods to set properties
            EditAdvertisementController userEditAdvertisementController = userEditAdvertisementFXMLLoader.getController();

            // set db manager property on userEditController
            userEditAdvertisementController.setDbManager(dbManager);
            // ser user property on userEditController
            userEditAdvertisementController.setUser(user);

            // Create new scene passing it the AnchorPane object
            Scene userEditAdvertisementScene = new Scene(userEditAdvertisementAnchorPane);

            // Set the addAdvertisementStage to use the stage userStage
            // Think of the stage as the window with the "X" button and the scene the contents inside
            userEditAdvertisementStage.setScene(userEditAdvertisementScene);


            // setting new stage as a modal popup which will disable the primary stage until closed
            userEditAdvertisementStage.initModality(Modality.APPLICATION_MODAL);
            // Show the addAdvertisementStage which already has the userScene FXML loaded
            userEditAdvertisementStage.showAndWait();

        } catch (IOException ex) {
            //TODO: deal with later.
            // Could be issues loading FXML file or something.
            ex.printStackTrace();
        }

    }

    // select advertisement to delete
    // TODO: test later
    public void deleteUserAdvButtonPressed(ActionEvent actionEvent) {
        ObservableList<Advertisements> advertisementsSelected, allAdvertisements;
        allAdvertisements = allAdvTableView.getItems();
        advertisementsSelected = allAdvTableView.getSelectionModel().getSelectedItems();
        // TODO: delete row from database
        advertisementsSelected.forEach(allAdvertisements::remove); // for all ads that have been selected, remove them from allAdvertisements


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

    public void allAdvTabSelectionChanged(Event event) {
        int eventTabIndex = userTabPane.getSelectionModel().getSelectedIndex();
        if (eventTabIndex != selectedTabIndex) {
            selectedTabIndex = eventTabIndex;
            //Call refresh/set AllFilteredAdb table view
            reloadAllAdvTable();
        }
    }

    public void myAdvTabSelectionChanged(Event event) {
        int eventTabIndex = userTabPane.getSelectionModel().getSelectedIndex();
        if (eventTabIndex != selectedTabIndex) {
            selectedTabIndex = eventTabIndex;
            //TODO: Call refresh/set MyAdv table view
        }
    }

}
