package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Objects;

public class ControllerCoach {

    private static UserModel loggedInUser = null;

    @FXML
    private TableView<UserModel> playersTableView, playersTakenTableView;
    @FXML
    private TableColumn<UserModel, String> namePlayer, cnpPlayer, nameTakenPlayer, cnpTakenPlayer;
    @FXML
    private TableColumn<UserModel, Integer> agePlayer, experiencePlayer, ageTakenPlayer, experienceTakenPlayer;
    @FXML
    private Label fullname, email, phone, cnp;
    @FXML
    private Button logout, apply;
    @FXML
    private RadioButton recruit, progress, absents;
    @FXML
    private TextField cnp_of_player;
    @FXML
    private TextField progress_bar;

    public void initialize() {
        loggedInUser = ControllerLogin.getLoggedInUser();

        if(loggedInUser != null) {
            fullname.setText(loggedInUser.getFname() + " " + loggedInUser.getLname());
            email.setText(loggedInUser.getEmail());
            phone.setText(loggedInUser.getPhone());
            cnp.setText(loggedInUser.getCnp());
        }

        setTable();
    }

    public void setTable() {
        namePlayer.setCellValueFactory(new PropertyValueFactory<>("FullName"));
        cnpPlayer.setCellValueFactory(new PropertyValueFactory<>("Cnp"));
        agePlayer.setCellValueFactory(new PropertyValueFactory<>("Age"));
        experiencePlayer.setCellValueFactory(new PropertyValueFactory<>("Yoe"));
        ObservableList<UserModel> list = FXCollections.observableArrayList();
        for(var i : Objects.requireNonNull(Database.getAllUsers()))
            if(i.getAccType().equals("PLAYER") &&
                    Objects.requireNonNull(Database.getPlayerData(i.getCnp())).getCoach().equals("NONE"))
                list.add(i);
        playersTableView.setItems(list);
    }

    @FXML
    public void handleRadioButtons() { progress_bar.setVisible(progress.isSelected()); }

    @FXML
    public void handleApply() throws IOException {
        if(recruit.isSelected()) {
            Database.recruit(cnp_of_player.getText(), loggedInUser.getFullName());
            cnp_of_player.setText("");
            //
            App.changeScene(apply, "coachScreen");
        }
        else if(progress.isSelected()) {
            int prg;
            try {
                prg = Integer.parseInt(progress_bar.getText());
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Progress must be integer!").showAndWait();
                e.printStackTrace();
                return;
            }
            Database.progress(cnp_of_player.getText(), prg);
            cnp_of_player.setText("");
            progress_bar.setText("");
        }
        else if(absents.isSelected()) {
            Database.absent(cnp_of_player.getText());
            cnp_of_player.setText("");
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
