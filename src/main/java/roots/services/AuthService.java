package roots.services;

import roots.models.User;

public interface AuthService {
    User login(String username, String password);
    boolean register(String fullname,String username,  String email, String password, String checkPasword);
}
