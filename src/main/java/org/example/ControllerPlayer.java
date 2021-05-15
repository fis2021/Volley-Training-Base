package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.io.IOException;

public class ControllerPlayer {

    private static UserModel loggedInUser = null;
    private static PlayerData playerData = null;
    @FXML
    private Button logout;
    @FXML
    private Label fullname, email, phone, cnp, age, yoe, absents, coach, progress;

    public void initialize() {
        loggedInUser = ControllerLogin.getLoggedInUser();
        playerData = ControllerLogin.getPlayerData();

        if(loggedInUser != null) {
            fullname.setText(loggedInUser.getFname() + " " + loggedInUser.getLname());
            email.setText(loggedInUser.getEmail());
            phone.setText(loggedInUser.getPhone());
            cnp.setText(loggedInUser.getCnp());
            age.setText(loggedInUser.getAge() + "");
            yoe.setText(loggedInUser.getYoe() + "");
            absents.setText(playerData.getAbsents() + "");
            coach.setText(playerData.getCoach());
            progress.setText(playerData.getProgress() + "");
        }
    }

    public void handleLogout() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if(alert.getResult().getText().equals("Yes"))
            App.changeScene(logout, "volleyLogin");
    }

    public static void setLoggedInUser(UserModel loggedInUser) { ControllerPlayer.loggedInUser = loggedInUser; }
    public static UserModel getLoggedInUser() { return loggedInUser; }
}
