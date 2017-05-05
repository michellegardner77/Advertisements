package main.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import main.models.Advertisements;
import main.models.User;
import main.services.DBManager;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by mgard on 5/2/2017.
 */
public class ModeratorController {
    public Pane moderatorUnclaimedTab;
    public TableView<Advertisements> unclaimedAdvTable;
    public TableView<Advertisements> moderatorAdvTable;
    public TextField unclaimedAdvTitleDescFilter;
    public ComboBox unclaimedCatFilterComBox;
    public ComboBox unclaimedPeriodFilterComBox;
    public TableColumn<Object, Object> unclaimedAdvIdCol;
    public TableColumn<Object, Object> unclaimedAdvTitleCol;
    public TableColumn<Object, Object> unclaimedAdvDescCol;
    public TableColumn<Object, Object> unclaimedAdvPriceCol;
    public TableColumn<Object, Object> unclaimedAdvDateCol;
    public TableColumn<Object, Object> unclaimedAdvUserCol;
    public TabPane tabPane;
    public TableColumn<Object, Object> moderatorAdvStatusCol;
    public TableColumn<Object, Object> moderatorAdvIdCol;
    public TableColumn<Object, Object> moderatorAdvTitleCol;
    public TableColumn<Object, Object> moderatorAdvDescCol;
    public TableColumn<Object, Object> moderatorAdvPriceCol;
    public TableColumn<Object, Object> moderatorAdvDateCol;
    public TableColumn<Object, Object> moderatorAdvUserCol;

    private User user;
    private DBManager dbManager;

    // var to hold the selected tab index
    private int selectedTabIndex;

    // List of unclaimed filtered Adv for All Adv list
    private List<Advertisements> unclaimedFilteredAdvList = null;
    // ObservableList of advs to populate all adv table view
    private ObservableList<Advertisements> unclaimedFilteredAdvObservableList = null;

    // List of users Advs
    private List<Advertisements> myAdvList = null;
    // ObservableList of advs to populate all my adv table view
    private ObservableList<Advertisements> myAdvObservableList = null;

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

        String periodComBoxSelectionValue = unclaimedPeriodFilterComBox.getValue().toString();

        // Deal with either ALL time period or specific period (3, 6, 12, ...)
        // check if string is number
        // if string is then add it to period filter
        //noinspection Duplicates
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

        String catComBoxSelectionValue = unclaimedCatFilterComBox.getValue().toString();

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
        String titleDescValue = unclaimedAdvTitleDescFilter.getText();

        // if null set it to wildcard for sql statement
        if(titleDescValue.isEmpty()){
            titleDescValue = "%";
        }

        return titleDescValue;
    }

    // get unclaimed data and load table with records
    void reloadUnclaimedAdvTable(){
        // Get selected cat query value to pass SQL statement
        String catQueryValue = getCatComboBoxQueryValue();
        // Get selected period query value to pass SQL statement
        String periodQueryValue = getPeriodComboBoxQueryValue();
        // Get selected title/desc query value to pass SQL statement
        String titleDescQueryValue = getTitleDescriptionQueryValue();

        // Get advertisements for unclaimed advTable based on users filter selections
        unclaimedFilteredAdvList = dbManager.getFilteredAdvertisements(catQueryValue, periodQueryValue, titleDescQueryValue, "PN", "NULL");

        // Add filtered Advs to ObservableList to populate table
        unclaimedFilteredAdvObservableList = FXCollections.observableList(unclaimedFilteredAdvList);

        // specify what properties to set the table columns
        // You enter in the NAME of the class property. ex: Advertisements has a advTitle property
        unclaimedAdvIdCol.setCellValueFactory(new PropertyValueFactory<>("advertisement_ID"));
        unclaimedAdvTitleCol.setCellValueFactory(new PropertyValueFactory<>("advTitle"));
        unclaimedAdvDescCol.setCellValueFactory(new PropertyValueFactory<>("advDetails"));
        unclaimedAdvPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        unclaimedAdvDateCol.setCellValueFactory(new PropertyValueFactory<>("advDateTime"));
        unclaimedAdvUserCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));

        // Initially set the Adv table
        unclaimedAdvTable.setItems(unclaimedFilteredAdvObservableList);
    }

    // get unclaimed data and load table with records
    private void reloadModAdvTable(){

        // Get my advertisements
        myAdvList = dbManager.getModAdvertisements(user.getUser_ID());

        // Add Advs to ObservableList to populate table
        myAdvObservableList = FXCollections.observableList(myAdvList);

        // specify what properties to set the table columns
        // You enter in the NAME of the class property. ex: Advertisements has a advTitle property
        moderatorAdvIdCol.setCellValueFactory(new PropertyValueFactory<>("advertisement_ID"));
        moderatorAdvTitleCol.setCellValueFactory(new PropertyValueFactory<>("advTitle"));
        moderatorAdvDescCol.setCellValueFactory(new PropertyValueFactory<>("advDetails"));
        moderatorAdvPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        moderatorAdvStatusCol.setCellValueFactory(new PropertyValueFactory<>("status_ID"));
        moderatorAdvDateCol.setCellValueFactory(new PropertyValueFactory<>("advDateTime"));
        moderatorAdvUserCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));

        // Initially set the Adv table
        moderatorAdvTable.setItems(myAdvObservableList);
    }


    // refresh table when you press go button
    public void uncGoButtonPressed(ActionEvent actionEvent) {
        reloadUnclaimedAdvTable();
    }

    public void approveButtonPressed(ActionEvent actionEvent) {
        Advertisements selectedMyAdvertisement = moderatorAdvTable.getSelectionModel().getSelectedItem();

        // doesn't do anything if nothing is selected
        if(selectedMyAdvertisement == null){
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Approve");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to approve this advertisement?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK){
            return;
        }

        // call db to update selected adv to be claimed
        dbManager.approveAdvertisement(selectedMyAdvertisement.getAdvertisement_ID());

        // refresh table
        reloadModAdvTable();
    }

    public void claimSelectedAd(ActionEvent actionEvent) {

        Advertisements selectedUnclaimedAdvertisement = unclaimedAdvTable.getSelectionModel().getSelectedItem();

        // doesn't do anything if nothing is selected
        if(selectedUnclaimedAdvertisement == null){
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Claim");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to claim this advertisement?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK){
            return;
        }

        // call db to update selected adv to be claimed
        dbManager.claimAdvertisement(selectedUnclaimedAdvertisement.getAdvertisement_ID(), user.getUser_ID());

        // refresh table
        reloadUnclaimedAdvTable();
    }

    // press enter in title/desk field refreshes table
    public void onGoEnter(ActionEvent actionEvent) {
        uncGoButtonPressed(actionEvent);
    }

    public void unclaimedTabSelectionChanged(Event event) {
        int eventTabIndex = tabPane.getSelectionModel().getSelectedIndex();
        if (eventTabIndex != selectedTabIndex) {
            selectedTabIndex = eventTabIndex;
            //Call refresh/set AllFilteredAdb table view
            reloadUnclaimedAdvTable();
        }
    }

    public void modMyAdvTabSelectionChanged(Event event) {
        int eventTabIndex = tabPane.getSelectionModel().getSelectedIndex();
        if (eventTabIndex != selectedTabIndex) {
            selectedTabIndex = eventTabIndex;
            //Call refresh/set MyAdv table view
            reloadModAdvTable();
        }
    }
}
