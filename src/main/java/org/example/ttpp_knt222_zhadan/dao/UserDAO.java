package org.example.ttpp_knt222_zhadan.dao;

import org.example.ttpp_knt222_zhadan.Listener.UserDAOEventListener;
import org.example.ttpp_knt222_zhadan.model.User;
import java.util.List;

public interface UserDAO {
    List<User> getAllUsers();
    User getUserById(int userId);
    User getUserByEmail(String email);
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(int userId);

    void addEventListener(UserDAOEventListener listener);
    void removeEventListener(UserDAOEventListener listener);
}
