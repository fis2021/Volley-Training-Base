package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.io.IOException;

public class ControllerParent {

    private static UserModel loggedInUser = null;
    private static ParentData parentData = null;

    @FXML
    private Label fullname, email, phone, cnp, cnp_child, progress, absents;
    @FXML
    private Button logout;

    public void initialize() {
        loggedInUser = ControllerLogin.getLoggedInUser();
        parentData = ControllerLogin.getParentData();

        if(loggedInUser != null) {
            fullname.setText(loggedInUser.getFname() + " " + loggedInUser.getLname());
            email.setText(loggedInUser.getEmail());
            phone.setText(loggedInUser.getPhone());
            cnp.setText(loggedInUser.getCnp());
            cnp_child.setText(loggedInUser.getCnp_child());
            absents.setText(parentData.getAbsents() + "");
            progress.setText(parentData.getProgress() + "");
        }
    }

    @FXML
    public void handleLogout() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if(alert.getResult().getText().equals("Yes"))
            App.changeScene(logout, "volleyLogin");
    }
}
