package roots.services.impl;

import roots.constrant.Error;
import roots.constrant.Message;
import roots.constrant.Success;
import roots.dao.LoginDAO;
import roots.dao.RegisterDAO;
import roots.models.User;
import roots.services.AuthService;
import roots.utils.AlertUtils;
import roots.utils.HashedPasswordUtils;


public class AuthServiceImpl implements AuthService {

    private LoginDAO loginDAO = new LoginDAO();
    private RegisterDAO registerDAO = new RegisterDAO();

    @Override
    public User login(String username, String password) {
        User user = loginDAO.getUserByUsername(username);

        if(user != null && HashedPasswordUtils.checkHashedPassword(password, user.getPassword())){
            return user;
        }
        return null;
    }

    @Override
    public User forgetPassword(String email, String newPassword) {
        User checkUser = loginDAO.getUserByEmail(email);
        if(checkUser != null){
            String hashNewPass = HashedPasswordUtils.hashedPassword(newPassword);
            checkUser.setPassword(hashNewPass);
            loginDAO.updateUser(checkUser);
        }

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
        User checkEmail = loginDAO.getUserByEmail(email);
        if(checkEmail != null){
            System.out.println(Error.checkEmail);
            AlertUtils.showFailAlert(Error.fail, Error.checkEmail);
            return false;
        }

        User newUser = new User();
        newUser.setFullname(fullname);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);

        String hashPass = HashedPasswordUtils.hashedPassword(password);
        newUser.setPassword(hashPass);

        return registerDAO.registerNewUser(newUser);
    }

}
