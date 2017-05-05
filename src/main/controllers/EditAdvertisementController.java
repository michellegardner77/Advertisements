package main.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.models.Advertisements;
import main.models.User;
import main.services.DBManager;

/**
 * Created by mgard on 5/4/2017.
 */
public class EditAdvertisementController {
    public TextField advTitleTextField;
    public TextArea advDescTextArea;
    public TextField advPriceTextField;
    public Label requiredLabel1;
    public Label requiredLabel2;
    public Label requiredLabel3;
    public Label requiredLabel4;
    public ComboBox<String> advTypeCombox;

    private User user;
    private DBManager dbManager;
    private Advertisements advertisement;

    // method to pass DBManager to controller
    void setDbManager(DBManager dbManager){
        this.dbManager = dbManager;
    }

    // method to pass current user to controller
    void setUser(User user) {
        this.user = user;
    }

    void setAdvertisement(Advertisements advertisement){
        this.advertisement  = advertisement;

        advTitleTextField.setText(advertisement.getAdvTitle());
        advDescTextArea.setText(advertisement.getAdvDetails());
        advPriceTextField.setText(String.valueOf(advertisement.getPrice()));
        advTypeCombox.setValue(advertisement.getCategory_ID());

    }

    public void userSubmitEditButtonPressed(ActionEvent actionEvent) {

        String editComboBox = String.valueOf(advTypeCombox.getSelectionModel().getSelectedItem());

        //noinspection Duplicates
        if (advTitleTextField.getText().isEmpty() || advDescTextArea.getText().isEmpty() || advPriceTextField.getText().isEmpty() || editComboBox.isEmpty()) {
            requiredLabel1.setVisible(true);
            requiredLabel2.setVisible(true);
            requiredLabel3.setVisible(true);
            requiredLabel4.setVisible(true);
            return;
        }

        // checks to make sure Price is a double and turns it into a double
        //noinspection Duplicates
        try {
            Double.parseDouble(advPriceTextField.getText());
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
        // dbManager.editAdvertisement(this will do a update not an insert)
//     dbManager.addAdvertisement(editAdvertisementTitle, editAdvertisementDesc, editAdvPrice, editAdvertisementCategory, " " );

        Node source = (Node) actionEvent.getSource();
        // calling the getWindow method to get the stage. Have to case to Stage
        Stage currentStage = (Stage) source.getScene().getWindow();

        // Close the login window
        currentStage.close();
    }
}

