package api;

import model.User;
import java.util.List;

public interface UserService {
    boolean login(String username, String password);
    void register(User user);
    void updateUser(User user);
    void deleteUser(int id);
    List<User> getAllUsers();
    User getUserById(int id);
    User getUserByUsername(String username);
}