package main.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import main.models.User;
import main.services.DBManager;

/**
 * Created by mgard on 5/2/2017.
 */

public class AddAdvertisementController {
    public TextField addAdvTitleTextField;
    public TextField addAdvPriceTextField;
    public TextArea advDetailTextArea;
    public Label requiredLabel;
    public Label requiredLabel2;
    public Label requiredLabel3;
    public Label requiredLabel4;
    public ComboBox typeComboBox;

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


    public void addAdvertisementButtonPressed(ActionEvent actionEvent) {
        String advComboBox = String.valueOf(typeComboBox.getSelectionModel().getSelectedItem());
        // TODO: add advertisement stuff and validation
        // a while loop that checks text fields are filed out.
        if (addAdvTitleTextField.getText().isEmpty() || advDetailTextArea.getText().isEmpty() || addAdvPriceTextField.getText().isEmpty() || advComboBox.isEmpty()) {
            advComboBox = String.valueOf(typeComboBox.getSelectionModel().getSelectedItem());
            requiredLabel.setVisible(true);
            requiredLabel2.setVisible(true);
            requiredLabel3.setVisible(true);
            requiredLabel4.setVisible(true);
            return;
        }

        String advertisementTitle;
        String advertisementDetail;
        String advertisementPrice;
        String advertisementCategory;
        Double advPrice;

        advertisementTitle = addAdvTitleTextField.getText();
        advertisementDetail = advDetailTextArea.getText();
        advertisementPrice = addAdvPriceTextField.getText();
        advertisementCategory = String.valueOf(typeComboBox.getSelectionModel().getSelectedItem());

        // checks to make sure Price is a double and turns it into a double
        try {
            advPrice = Double.parseDouble(advertisementPrice);
        } catch (NumberFormatException e) {
            // print out error, not a double
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not a valid price");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());

            alert.showAndWait();
            return;
        }
//
//        String selected_type = advTypeCombox.getValue().toString();
        // TODO: get user from previous page to use for addAD user_id

        // pass dbManager object from loginController to other scenes
        // addAdv modal return to main then add or have modal do insert statement
        // refresh table and have new rows added in
//        // evaluating that addAdvTitleTextField is populated
//        // if not it will show the require label
//        if(addAdvTitleTextField.getText().isEmpty()){
//            requiredLabel.setVisible(true);
//            return;
//        }
////        else{
//////            requiredLabel.setVisible(false);
////        }
//        // made it here due to addAdvTitleTextField populated
//        // make sure required label is hidden
////        if(requiredLabel.isVisible()){
////            requiredLabel.setVisible(false);
////        }
//
//        // check for empty details field
//        if(advDetailTextArea.getText().isEmpty()){
//            requiredLabel2.setVisible(true);
//            return;
//        }
////        else {
////            requiredLabel2.setVisible(false);
////        }
//
////        // make sure required label is hidden
////        if(requiredLabel2.isVisible()){
////            requiredLabel2.setVisible(false);
////        }
//
//        // required price
//        if(addAdvPriceTextField.getText().isEmpty()){
//            requiredLabel3.setVisible(true);
//            return;
//        }
////        else{
////            requiredLabel3.setVisible(false);
////
////        }
////        if(requiredLabel3.isVisible()){
////            requiredLabel3.setVisible(false);
////            return;
////        }


        // TODO: need to validate strings : title entered is a string, details are a string, categories type, price has to be a double


        // Get current stage and close it.
        // get source Node from actionEvent passed to action method     cat


       dbManager.addAdvertisement(advertisementTitle, advertisementDetail, advPrice, advertisementCategory, " " );

        Node source = (Node) actionEvent.getSource();
        // calling the getWindow method to get the stage. Have to case to Stage
        Stage currentStage = (Stage) source.getScene().getWindow();
        // Close the login window
        currentStage.close();
    }


}

