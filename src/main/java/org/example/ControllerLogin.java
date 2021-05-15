package org.example;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.ArrayList;
public class ControllerLogin {

    private static UserModel loggedInUser = null;
    private static PlayerData playerData = null;
    private static ParentData parentData = null;

    @FXML
    private Button register, login;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    public void handleLogin() throws IOException {
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
                switch(loggedInUser.getAccType()) {
                    case "PLAYER":
                        playerData = Database.getPlayerData(loggedInUser.getCnp());
                        App.changeScene(login, "playerScreen");
                        break;
                    case "PARENT":
                        parentData = Database.getParentData(loggedInUser.getCnp());
                        App.changeScene(login, "parentScreen");
                        break;
                    case "COACH":
                        App.changeScene(login, "coachScreen");
                        break;
                }
        }
    }

    @FXML
    public void handleRegister() throws IOException {
        App.changeScene(register, "volleyRegister");
    }

    public static UserModel getLoggedInUser() { return loggedInUser; }
    public static PlayerData getPlayerData() { return playerData; }
    public static ParentData getParentData() { return parentData; }
}