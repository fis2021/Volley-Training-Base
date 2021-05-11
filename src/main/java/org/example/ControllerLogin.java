package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class ControllerLogin {
    @FXML
    private Button register;

    @FXML
    public void handleRegister() throws IOException {
        App.changeScene(register, "volleyRegister");
    }

}
