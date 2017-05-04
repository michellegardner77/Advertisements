package main.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.models.User;
import main.services.DBManager;

/**
 * Created by mgard on 5/2/2017.
 */
public class ModeratorController {
    public Tab modUnclaimedPanel;
    public Pane moderatorUnclaimedTab;
    public ComboBox categoryComBox;
    public TextField uncTitleAdvTextField;
    public TableView unclaimedAdvTable;
    public ComboBox periodComBox;
    public Tab moderatorAdvTab;
    public TableView moderatorAdvTable;

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


    public void uncGoButtonPressed(ActionEvent actionEvent) {
    }

    public void approveButtonPressed(ActionEvent actionEvent) {
    }
}
