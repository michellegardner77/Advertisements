package main.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.models.User;
import main.services.DBManager;

import java.util.List;
import java.util.Objects;

public class LoginController {
    public TextField userTextField;
    public ComboBox typeComboBox;
    public Label requiredLable;

    // method called when Login Button is pressed
    public void loginAction(ActionEvent actionEvent) {
//        System.out.println("Login button pressed.");
//        System.out.println(userTextField.getText());
//        System.out.println(typeComboBox.getValue());

        boolean isModerator = false;

        // evaluating that userTextField is populated
        // if not it will show the require label
        if(userTextField.getText().isEmpty()){
            requiredLable.setVisible(true);
            return;
        }

        // made it here due to userTextField populated
        // make sure requred label is hidden
        if(requiredLable.isVisible()){
            requiredLable.setVisible(false);
        }

        DBManager dbManager = new DBManager();
        try {
            dbManager.connect("adv_user", "1234", "localhost", "3306", "advertisements");
        } catch (Exception ex){
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }

        // checking that the type is Moderator
        if(Objects.equals(typeComboBox.getValue().toString(), "Moderator")){
            System.out.println("Is Moderator!");
            isModerator = true;
        }

        // check that user exists in database
        User user = dbManager.getUser(userTextField.getText());

        // check to make sure user exists
        // shows error if nothing returns from database
        if(user == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("User does not exist!");

            alert.showAndWait();
            return;
        }


    }

}
