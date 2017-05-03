package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.models.User;
import main.services.DBManager;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    public TextField userTextField;
    public ComboBox typeComboBox;
    public Label requiredLabel;

    // method called when Login Button is pressed
    public void loginAction(ActionEvent actionEvent) {
//        System.out.println("Login button pressed.");
//        System.out.println(userTextField.getText());
//        System.out.println(typeComboBox.getValue());

        // evaluating that userTextField is populated
        // if not it will show the require label
        if(userTextField.getText().isEmpty()){
            requiredLabel.setVisible(true);
            return;
        }

        // made it here due to userTextField populated
        // make sure requred label is hidden
        if(requiredLabel.isVisible()){
            requiredLabel.setVisible(false);
        }

        // Create new DBManager object that is used to connect to the Database and run queries
        DBManager dbManager = new DBManager();

        //intiate database connection with DBManager
        try {
            dbManager.connect("adv_user", "1234", "localhost", "3306", "advertisements");
        } catch (Exception ex){
            ex.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Connection Error");
            alert.setHeaderText(null);
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
            return;
        }

        // check that user exists in database
        User user = dbManager.getUser(userTextField.getText());

        // check to make sure user exists
        // shows error if nothing returns from database
        if(user == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("User Error");
            alert.setHeaderText(null);
            alert.setContentText("User does not exist!");

            alert.showAndWait();
            return;
        }

        // checking that the type is Moderator by checking moderators table
        if(Objects.equals(typeComboBox.getValue().toString(), "Moderator")){
            boolean isModerator = dbManager.isModerator(user.getUser_ID());
            user.setModerator(isModerator);

            if(!user.isModerator()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Not Moderator");
                alert.setHeaderText(null);
                alert.setContentText("You're not a moderator, please select User as type.");

                alert.showAndWait();
                return;
            }

        }

        if(user.isModerator()) {
            //TODO: go to moderator scene
        }else{
            try {
                // Create new stage fro userScene
                Stage userSceneStage = new Stage();
                // Set Stage title (Window title)
                userSceneStage.setTitle("Username: " + user.getUser_ID());

                // Load fxml file to generate scene (Make sure file patch is correct)
                Parent userSceneFxml = FXMLLoader.load(getClass().getResource("../scenes/userScene.fxml"));
                // Create new scene passing it the loaded Parent FXML object (file)
                Scene userScene = new Scene(userSceneFxml);
                // Set the userSceneStage to use the stage userStage
                // Think of the stage as the window with the "X" button and the scene the contents inside
                userSceneStage.setScene(userScene);

                // Get current stage and close it.
                // get source Node from actionEvent passed to action method
                Node source = (Node) actionEvent.getSource();
                // calling the getWindow method to get the stage. Have to case to Stage
                Stage loginStage = (Stage) source.getScene().getWindow();
                // Close the login window
                loginStage.close();

                // Show the userSceneStage which already has the userScene FXML loaded
                userSceneStage.show();

            } catch (IOException ex) {
                //TODO: deal with later.
                // Could be issues loading FXML file or something.
                ex.printStackTrace();
            }


        }


    }

}
