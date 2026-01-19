package roots.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import roots.constrant.Error;
import roots.constrant.Message;
import roots.constrant.Success;
import roots.dao.LoginDAO;
import roots.models.User;

public class LoginController {
    @FXML private TextField usernameLG;
    @FXML private PasswordField passwordLG;


    private LoginDAO loginDAO = new LoginDAO();

    @FXML
    public void loginLG(){
         String name = usernameLG.getText();
         String pass = passwordLG.getText();

        User user = loginDAO.getUserByUsername(name);

        if(user != null && user.getPassword().equals(pass)){
            showAlert(Success.success, Message.wellcome + name + Message.comback);
        }
        else{
            showAlert(Error.fail, Error.failLogin);
        }

    }
    public void showAlert(String equal, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(equal);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
