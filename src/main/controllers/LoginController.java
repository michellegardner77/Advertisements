package main.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import main.models.Users;
import main.services.DBManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginController {
    public TextField userTextField;
    public ComboBox typeComboBox;

    // method called when Login Button is pressed
    public void loginAction(ActionEvent actionEvent) {
//        System.out.println("Login button pressed.");
//        System.out.println(userTextField.getText());
//        System.out.println(typeComboBox.getValue());

        boolean isModerator = false;

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
        List<Users> users = dbManager.getUser(userTextField.getText());
//        for (Users user:users)
//        {
//            System.out.println(user.getUser_ID());
//            System.out.println(user.getUserFirst_Name());
//            System.out.println(user.getUserLast_Name());
//        }
    }

}
