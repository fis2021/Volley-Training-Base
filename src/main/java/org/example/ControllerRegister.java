package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Objects;

public class ControllerRegister {

    @FXML
    private Button back;
    @FXML
    private ComboBox<String> accountType;
    @FXML
    private Label cnpChild_label, age_label, yearsOfExperience_label;
    @FXML
    private TextField yearsOfExperience, age, cnpChild, firstname, lastname, email, phone, cnp, username;
    @FXML
    private PasswordField password;

    @FXML
    public void handleBack() throws IOException {
        App.changeScene(back, "volleyLogin");
    }

    @FXML
    public void handleRegister() {
        if(cnp.getText().length() != 13) {
            new Alert(Alert.AlertType.ERROR, "CNP must be 13 characters long!").showAndWait();
            return;
        }

        UserModel user = null;
        switch (accountType.getValue()) {
            case "PLAYER":
                try {
                    Integer.parseInt(age.getText());
                    Integer.parseInt(yearsOfExperience.getText());
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Age or Years of experience is not a number");
                    return;
                }

                user = new UserModel(firstname.getText(), lastname.getText(), email.getText(), phone.getText(),
                        cnp.getText(), "PLAYER", "X", Integer.parseInt(age.getText()),
                        Integer.parseInt(yearsOfExperience.getText()), username.getText(),
                                Database.encodePasswordMD5(password.getText()));

                break;
            case "COACH":
                user = new UserModel(firstname.getText(), lastname.getText(), email.getText(), phone.getText(),
                        cnp.getText(), "COACH", "X", -1, -1, username.getText(),
                        Database.encodePasswordMD5(password.getText()));
                break;
            case "PARENT":
                if(Database.getPlayerData(cnpChild.getText()) == null) {
                    new Alert(Alert.AlertType.ERROR, "Child is not registered").showAndWait();
                    return;
                }
                user = new UserModel(firstname.getText(), lastname.getText(), email.getText(), phone.getText(),
                        cnp.getText(), "PARENT", cnpChild.getText(), -1, -1, username.getText(),
                        Database.encodePasswordMD5(password.getText()));
                break;
        }
        if(user != null) {
            Database.uploadUser(user);
            if(user.getAccType().equals("PLAYER"))
                Database.uploadPlayerData(new PlayerData(user.getCnp(), "NONE", 0, 0));
            else if(user.getAccType().equals("PARENT"))
                Database.uploadParentData(new ParentData(user.getCnp(),
                        Objects.requireNonNull(Database.getPlayerData(cnpChild.getText())).getAbsents(),
                        Objects.requireNonNull(Database.getPlayerData(cnpChild.getText())).getProgress()));

            firstname.setText("");
            lastname.setText("");
            email.setText("");
            phone.setText("");
            cnp.setText("");
            yearsOfExperience.setText("");
            age.setText("");
            cnpChild.setText("");
            username.setText("");
            password.setText("");
        }

    }

    @FXML
    public void handleComboBox() {
        age_label.setVisible(false);
        age.setVisible(false);
        yearsOfExperience_label.setVisible(false);
        yearsOfExperience.setVisible(false);
        cnpChild_label.setVisible(false);
        cnpChild.setVisible(false);

        switch (accountType.getValue()) {
            case "PLAYER":
                cnpChild_label.setVisible(false);
                cnpChild.setVisible(false);
                age_label.setVisible(true);
                age.setVisible(true);
                yearsOfExperience_label.setVisible(true);
                yearsOfExperience.setVisible(true);
                break;
            case "COACH":
                age_label.setVisible(false);
                age.setVisible(false);
                yearsOfExperience_label.setVisible(false);
                yearsOfExperience.setVisible(false);
                cnpChild_label.setVisible(false);
                cnpChild.setVisible(false);
                break;
            case "PARENT":
                age_label.setVisible(false);
                age.setVisible(false);
                yearsOfExperience_label.setVisible(false);
                yearsOfExperience.setVisible(false);
                cnpChild_label.setVisible(true);
                cnpChild.setVisible(true);
                break;
        }
    }

}
