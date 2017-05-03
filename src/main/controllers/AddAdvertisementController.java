package main.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

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
    Connection connection;
    public ComboBox typeComboBox;

    // We want to take the inputted data and put it into our table
    public boolean addAd(String adTitle, String adDetails, double Price, String category_id, String user_id) {
        PreparedStatement stmt = null;

        String query = "INSERT into Advertisements (AdvTitle, AdvDetails, AdvDateTime, Price, Category_ID, User_ID, Moderator_ID, Status_ID) VALUES (?,?,CURRENT_DATE(),?,?,?,NULL, 'PN')";

        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, adTitle); //binding the parameter with the given string
            stmt.setString(2, adDetails);
            stmt.setDouble(3, Price);      //I AM NOT SURE IF YOU SKIP 3 BECAUSE OF DATE
            stmt.setString(4, category_id);
            stmt.setString(5, user_id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public void addAdvertisementButtonPressed(ActionEvent actionEvent) {

        // TODO: add advertisement stuff and validation
        // a while loop that checks text fields are filed out.
        while (addAdvTitleTextField.getText().isEmpty() || advDetailTextArea.getText().isEmpty() || addAdvPriceTextField.getText().isEmpty()) {
            requiredLabel.setVisible(true);
            requiredLabel2.setVisible(true);
            requiredLabel3.setVisible(true);
            return;
        }

        String advertisementTitle;
        String advertisementDetail;
        String advertisementPrice;
        Double advPrice;

        advertisementTitle = addAdvTitleTextField.getText();
        advertisementDetail = advDetailTextArea.getText();
        advertisementPrice = addAdvPriceTextField.getText();

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

        String selected_text = typeComboBox.getValue().toString();
        // TODO: get user from previous page to use for addAD user_id


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
        addAd(advertisementTitle, advertisementDetail, advPrice, selected_text, " " );

        Node source = (Node) actionEvent.getSource();
        // calling the getWindow method to get the stage. Have to case to Stage
        Stage currentStage = (Stage) source.getScene().getWindow();
        // Close the login window
        currentStage.close();
    }


}

