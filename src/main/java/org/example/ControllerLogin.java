package org.example;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.ArrayList;
public class ControllerLogin {
    @FXML
    private Button register;
    @FXML
    private static UserModel loggedInUser = null;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    public void handleLogin() {
        loggedInUser = null;
        ArrayList<UserModel> userModels = Database.getAllUsers();
        if(userModels == null)
            new Alert(Alert.AlertType.ERROR, "No users have been registered yet").showAndWait();
        else {
            for(var i : userModels)
                if(i.getUsername().equals(username.getText()) &&
                        i.getPassword().equals(Database.encodePasswordMD5(password.getText()))) {
                    loggedInUser = i;
                    break;
                }
            if(loggedInUser == null)
                new Alert(Alert.AlertType.ERROR, "Username or Password is incorrect").showAndWait();
            else
                System.out.println("Log in done");
        }
    }
    @FXML
    public void handleRegister() throws IOException {
        App.changeScene(register, "volleyRegister");
    }
}