package main.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.models.User;
import main.services.DBManager;

/**
 * Created by mgard on 5/4/2017.
 */
public class EditAdvertisementController {


    public TextField userEditTextField;
    public TextArea userEditDescTextArea;
    public TextField userEditPriceTextField;
    public Label requiredLabel;
    public Label requiredLabel2;
    public Label requiredLabel3;
    public Label requiredLabel4;
    public ComboBox userEditComboBox;

    private User user;
    private DBManager dbManager;

    // method to pass DBManager to controller
    void setDbManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    // method to pass current user to controller
    void setUser(User user) {
        this.user = user;
    }

    public void userSubmitEditButtonPressed(ActionEvent actionEvent) {


        if (userEditTextField.getText().isEmpty() || userEditDescTextArea.getText().isEmpty() || userEditPriceTextField.getText().isEmpty() || userEditComboBox.isEmpty()) {
            userEditComboBox = String.valueOf(userEditComboBox.getSelectionModel().getSelectedItem());
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

        advertisementTitle = userEditTextField.getText();
        advertisementDetail = userEditDescTextArea.getText();
        advertisementPrice = userEditPriceTextField.getText();
        advertisementCategory = String.valueOf(userEditComboBox.getSelectionModel().getSelectedItem());

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


        //TODO: create an edit advertisement method for this vv
//     dbManager.addAdvertisement(advertisementTitle, advertisementDetail, advPrice, advertisementCategory, " " );

        Node source = (Node) actionEvent.getSource();
        // calling the getWindow method to get the stage. Have to case to Stage
        Stage currentStage = (Stage) source.getScene().getWindow();

        // Close the login window
        currentStage.close();
    }
}

