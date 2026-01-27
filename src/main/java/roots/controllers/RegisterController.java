package roots.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import roots.services.impl.AuthServiceImpl;
import roots.utils.ChangeFXML;

public class RegisterController {
    @FXML
    private TextField fullnameRGS;
    @FXML
    private TextField usernameRGS;
    @FXML
    private TextField emailRGS;
    @FXML
    private PasswordField passwordRGS;
    @FXML
    private PasswordField checkPasswordRGS;

    @FXML
    public void continueRGS(ActionEvent event) {
        String fullname = fullnameRGS.getText();
        String username = usernameRGS.getText();
        String email = emailRGS.getText();
        String password = passwordRGS.getText();
        String checkPassword = checkPasswordRGS.getText();

        AuthServiceImpl authService = new AuthServiceImpl();
        authService.register(fullname,username,email,password,checkPassword);


    }
    @FXML
    public void loginRGS(ActionEvent event){
        ChangeFXML.changeFXML(event, "/view/login.fxml");
    }

}

