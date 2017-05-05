package main.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import main.models.User;
import main.services.DBManager;

/**
 * Created by mgard on 5/2/2017.
 */

public class AddAdvertisementController {
    public TextField addAdvTitleTextField;
    public TextField addAdvPriceTextField;
    public TextArea advDetailTextArea;
    public ComboBox<String> advCatComboBox;
    public Label requiredLabel;
    public Label requiredLabel2;
    public Label requiredLabel3;
    public Label requiredLabel4;


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
        String advComboBox = String.valueOf(advCatComboBox.getSelectionModel().getSelectedItem());
        // a while loop that checks text fields are filed out.
        //noinspection Duplicates
        if (addAdvTitleTextField.getText().isEmpty() || advDetailTextArea.getText().isEmpty() || addAdvPriceTextField.getText().isEmpty() || advComboBox.isEmpty()) {
            requiredLabel.setVisible(true);
            requiredLabel2.setVisible(true);
            requiredLabel3.setVisible(true);
            requiredLabel4.setVisible(true);
            return;
        }

        Double enteredPrice;

        // checks to make sure Price is a double and turns it into a double
        //noinspection Duplicates
        try {
            enteredPrice = Double.parseDouble(addAdvPriceTextField.getText());
        } catch (NumberFormatException e) {
            // print out error, not a double
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not a valid price");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());

            alert.showAndWait();
            return;
        }

       dbManager.addAdvertisement(addAdvTitleTextField.getText(), advDetailTextArea.getText(),enteredPrice, advCatComboBox.getValue(), user.getUser_ID());

        Node source = (Node) actionEvent.getSource();
        // calling the getWindow method to get the stage. Have to case to Stage
        Stage currentStage = (Stage) source.getScene().getWindow();
        // Close the login window
        currentStage.close();
    }


}

