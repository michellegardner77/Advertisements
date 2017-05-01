package main.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class LoginController {
    public TextField userTextField;
    public ComboBox typeComboBox;
    public Button loginButton;

    // method called when Login Button is pressed
    public void loginAction(ActionEvent actionEvent) {
        System.out.println("Login button pressed.");
        System.out.println(userTextField.getText());
        System.out.println(typeComboBox.getValue());

    }
}
