package roots.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import roots.constrant.Error;
import roots.constrant.Message;
import roots.constrant.Success;
import roots.dao.LoginDAO;
import roots.models.User;
import roots.services.impl.AuthServiceImpl;
import roots.utils.AlertUtils;

import java.io.IOException;


public class LoginController {
    @FXML private TextField usernameLG;
    @FXML private PasswordField passwordLG;
    @FXML private TextField emailFG;


    private LoginDAO loginDAO = new LoginDAO();

    @FXML
    public void loginLG(){
         String name = usernameLG.getText();
         String pass = passwordLG.getText();
        AuthServiceImpl authService = new AuthServiceImpl();

        User user = authService.login(name, pass);
        if(user != null){
            AlertUtils.showSuccessAlert(Message.wellcome + name, Message.comback);
        }
        else {
            System.out.println(Error.failLogin);;
        }
    }
    @FXML
    public void forgetLG(){

    }



}
