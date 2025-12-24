package api;

public interface AuthService {
    boolean authenticate(String username, String password);
    String getUserRole(String username);
    void changePassword(String username, String newPassword);
    void logout();
}