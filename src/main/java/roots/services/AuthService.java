package roots.services;

import roots.models.User;

public interface AuthService {
    User login(String username, String password);
    User register(String username, String password);
}
