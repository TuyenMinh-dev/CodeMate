package roots.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML
    private TextField fullnameRGS;
    @FXML
    private TextField usernameRGS;
    @FXML
    private PasswordField passwordRGS;
    @FXML
    private PasswordField checkPasswordRGS;

    @FXML
    public void continueRGS() {
        String fullname = fullnameRGS.getText();
        String username = usernameRGS.getText();
        String password = passwordRGS.getText();
        String checkPassword = checkPasswordRGS.getText();


    }
}

