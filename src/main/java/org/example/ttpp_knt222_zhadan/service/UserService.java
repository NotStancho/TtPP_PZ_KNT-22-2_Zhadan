package org.example.ttpp_knt222_zhadan.service;

import org.example.ttpp_knt222_zhadan.Listener.UserEventListener;
import org.example.ttpp_knt222_zhadan.Proxy.UserDAOProxy;
import org.example.ttpp_knt222_zhadan.dao.Factory.DAOFactory;
import org.example.ttpp_knt222_zhadan.dao.Factory.FabricMethodDAO;
import org.example.ttpp_knt222_zhadan.dao.Factory.TypeDAO;
import org.example.ttpp_knt222_zhadan.dao.UserDAO;
import org.example.ttpp_knt222_zhadan.model.User;
import org.example.ttpp_knt222_zhadan.model.builder.UserBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDAO userDAO;
    private final UserEventListener userEventListener;

    public UserService() {
        DAOFactory factory = FabricMethodDAO.getDAOFactory(TypeDAO.MYSQL);
        UserDAO originalUserDAO = factory.createUserDAO();
        this.userDAO = new UserDAOProxy(originalUserDAO, this);
        this.userEventListener = new UserEventListener();
        this.userDAO.addEventListener(userEventListener);
    }


    public List<User> getAllUsers() {
        logger.info("Отримання всіх користувачів");
        return userDAO.getAllUsers();
    }

    public User getUserById(int userId) {
        logger.info("Отримання користувача з ID: {}", userId);
        return userDAO.getUserById(userId);
    }

    public User getUserByEmail(String email) {
        logger.info("Отримання користувача за email: {}", email);
        return userDAO.getUserByEmail(email);
    }

    public void addUser(User user) {
        logger.info("Додавання нового користувача: {}", user.getEmail());
        User newUser = new UserBuilder()
                .setFirstname(user.getFirstname())
                .setLastname(user.getLastname())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setPhone(user.getPhone())
                .setRole(user.getRole())
                .build();
        userDAO.addUser(newUser);
    }

    public void updateUser(User user) {
        logger.info("Оновлення користувача з ID: {}", user.getUserId());
        User updatedUser = new UserBuilder()
                .setUserId(user.getUserId())
                .setFirstname(user.getFirstname())
                .setLastname(user.getLastname())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setPhone(user.getPhone())
                .setRole(user.getRole())
                .build();
        userDAO.updateUser(updatedUser);
    }

    public void deleteUser(int userId) {
        logger.info("Видалення користувача з ID: {}", userId);
        userDAO.deleteUser(userId);
    }

}
