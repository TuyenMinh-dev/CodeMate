package roots.services.impl;

import roots.constrant.Error;
import roots.constrant.Message;
import roots.constrant.Success;
import roots.dao.LoginDAO;
import roots.dao.RegisterDAO;
import roots.models.User;
import roots.services.AuthService;
import roots.utils.AlertUtils;


public class AuthServiceImpl implements AuthService {

    private LoginDAO loginDAO = new LoginDAO();
    private RegisterDAO registerDAO = new RegisterDAO();

    @Override
    public User login(String username, String password) {
        User user = loginDAO.checkAccountUser(username,password);
        return user;
    }

    @Override
    public User forgetPassword(String email, String newPassword) {
        User checkUser = loginDAO.getUserByEmail(email);
        checkUser.setPassword(newPassword);
        return checkUser;
    }


    @Override
    public boolean register(String fullname, String username, String email, String password, String checkPasword) {
        User checkUser = loginDAO.getUserByUsername(username);

        if(checkUser != null){
            System.out.println(Error.username);
            AlertUtils.showFailAlert(Error.fail, Error.username);
            return false;
        }
        if(!checkPasword.equals(password)){
            System.out.println(Error.checkPassword);
            AlertUtils.showFailAlert(Error.fail, Error.checkPassword);
            return false;
        }

        User newUser = new User();
        newUser.setFullname(fullname);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setPassword(password);

        return registerDAO.registerNewUser(newUser);
    }

}
