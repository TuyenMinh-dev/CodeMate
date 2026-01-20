package roots.services.impl;

import javafx.scene.control.Alert;
import roots.constrant.Error;
import roots.constrant.Message;
import roots.constrant.Success;
import roots.dao.LoginDAO;
import roots.dao.RegisterDAO;
import roots.models.User;
import roots.services.AuthService;

public class AuthServiceImpl implements AuthService {

    private LoginDAO loginDAO = new LoginDAO();
    private RegisterDAO registerDAO = new RegisterDAO();

    @Override
    public User login(String username, String password) {
        User user = loginDAO.checkAccountUser(username,password);

        if(user != null && user.getPassword().equals(password)){
            showAlert(Success.success, Message.wellcome + username + Message.comback);
            return user;
        }
        else{
            showAlert(roots.constrant.Error.fail, Error.failLogin);
            return null;
        }
    }

    @Override
    public User register(String username, String password) {
        User checkUser = loginDAO.getUserByUsername(username);

        if(checkUser != null){
            System.out.println(Error.username);
            return null;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);

        boolean registerNewUser = registerDAO.registerNewUser(newUser);
        if(registerNewUser){
            showAlert(Success.success, Message.newWellcome);
            return newUser;
        }

        return null;
    }

    public void showAlert(String equal, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(equal);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
