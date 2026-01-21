package roots.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import roots.constrant.Error;
import roots.constrant.Message;
import roots.constrant.Success;
import roots.dao.LoginDAO;
import roots.models.User;
import roots.services.impl.AuthServiceImpl;

public class LoginController {
    @FXML private TextField usernameLG;
    @FXML private PasswordField passwordLG;


    private LoginDAO loginDAO = new LoginDAO();

    @FXML
    public void loginLG(){
         String name = usernameLG.getText();
         String pass = passwordLG.getText();
        AuthServiceImpl authService = new AuthServiceImpl();

        authService.login(name, pass);
    }


}
