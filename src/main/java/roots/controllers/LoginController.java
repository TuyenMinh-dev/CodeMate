package roots.controllers;

import javafx.event.ActionEvent;
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
import roots.utils.ChangeFXML;

import java.io.IOException;


public class LoginController {
    @FXML private TextField usernameLG;
    @FXML private PasswordField passwordLG;
    @FXML private TextField emailFG;


    private LoginDAO loginDAO = new LoginDAO();

    @FXML
    public void loginLG(ActionEvent event){
         String name = usernameLG.getText();
         String pass = passwordLG.getText();
        AuthServiceImpl authService = new AuthServiceImpl();

        User user = authService.login(name, pass);
        if(user != null){
            AlertUtils.showSuccessAlert(Message.wellcome + name, Message.comback);
            ChangeFXML.changeFXML(event, "/view/register.fxml");
        }
        else {
            System.out.println(Error.failLogin);;
        }
    }
    @FXML
    public void forgetLG(ActionEvent event){
        ChangeFXML.changeFXML(event, "/view/forgetPassword.fxml");
    }
    @FXML
    public void registerLG(ActionEvent event){
        ChangeFXML.changeFXML(event, "/view/register.fxml");
    }



}
