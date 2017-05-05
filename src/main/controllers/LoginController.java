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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.models.User;
import main.services.DBManager;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    public TextField userTextField;
    public ComboBox typeComboBox;
    public Label requiredLabel;
    public DBManager dbManager;


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
        // make sure required label is hidden
        if(requiredLabel.isVisible()){
            requiredLabel.setVisible(false);
        }

        // Create new DBManager object that is used to connect to the Database and run queries
        DBManager dbManager = new DBManager();

        //initiate database connection with DBManager
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
            try {
                // STAGE (window)
                // Create new stage fro moderatorScene
                Stage moderatorStage = new Stage();
                // Set Stage title (Window title)
                moderatorStage.setTitle("Username: " + user.getUser_ID());
                // made stage / window not resizable
                moderatorStage.setResizable(false);

                // STAGE Contents (Scene)
                // Load fxml file to FMXLoader(Make sure file patch is correct)
                FXMLLoader moderatorSceneLoader = new FXMLLoader(getClass().getResource("../scenes/moderatorScene.fxml"));

                // Create initial pane (anchorPane) for scene object.
                AnchorPane moderatorAnchorPane = moderatorSceneLoader.load();

                // Get instance of moderatorController to call it's methods to set properties
                ModeratorController moderatorController = moderatorSceneLoader.getController();

                // set db manager property on userController
                moderatorController.setDbManager(dbManager);
                // ser user property on userController
                moderatorController.setUser(user);

                // Create new scene passing it the AnchorPane object
                Scene userScene = new Scene(moderatorAnchorPane);

                // Set the userStage to use the stage userStage
                // Think of the stage as the window with the "X" button and the scene the contents inside
                moderatorStage.setScene(userScene);


                // Get current stage and close it.
                // get source Node from actionEvent passed to action method
                Node source = (Node) actionEvent.getSource();
                // calling the getWindow method to get the stage. Have to case to Stage
                Stage loginStage = (Stage) source.getScene().getWindow();
                // Close the login window
                loginStage.close();

                // Show the moderatorStage which already has the moderatorScene FXML loaded
                moderatorStage.show();

            }catch(IOException ex) {
                //TODO: deal with later.
                // Could be issues loading FXML file or something.
                ex.printStackTrace();
            }

        }else{
            // user stage
            try {
                // STAGE (window)
                // Create new stage fro userScene
                Stage userStage = new Stage();
                // Set Stage title (Window title)
                userStage.setTitle("Username: " + user.getUser_ID());
                // made stage / window not resizable
                userStage.setResizable(false);

                // STAGE Contents (Scene)
                // Load fxml file to FMXLoader(Make sure file patch is correct)
                FXMLLoader userSceneLoader = new FXMLLoader(getClass().getResource("../scenes/userScene.fxml"));

                // Create initial pane (anchorPane) for scene object.
                AnchorPane userAnchorPane = userSceneLoader.load();

                // Get instance of userController to call it's methods to set properties
                UserController userController = userSceneLoader.getController();

                // set db manager property on userController
                userController.setDbManager(dbManager);
                // ser user property on userController
                userController.setUser(user);

                // Create new scene passing it the AnchorPane object
                Scene userScene = new Scene(userAnchorPane);

                // Set the userStage to use the stage userStage
                // Think of the stage as the window with the "X" button and the scene the contents inside
                userStage.setScene(userScene);

                // refresh AllFiltered Adv Table before showing scene
                userController.reloadAllAdvTable();

                // Get current stage and close it.
                // get source Node from actionEvent passed to action method
                Node source = (Node) actionEvent.getSource();
                // calling the getWindow method to get the stage. Have to case to Stage
                Stage loginStage = (Stage) source.getScene().getWindow();
                // Close the login window
                loginStage.close();

                // Show the userStage which already has the userScene FXML loaded
                userStage.show();

            } catch (IOException ex) {
                //TODO: deal with later.
                // Could be issues loading FXML file or something.
                ex.printStackTrace();
            }


        }


    }

}
