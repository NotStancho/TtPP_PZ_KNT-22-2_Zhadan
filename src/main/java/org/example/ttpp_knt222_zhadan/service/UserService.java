package org.example.ttpp_knt222_zhadan.service;

import org.example.ttpp_knt222_zhadan.dao.DAOFactory;
import org.example.ttpp_knt222_zhadan.dao.UserDAO;
import org.example.ttpp_knt222_zhadan.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDAO userDAO;

    @Autowired
    public UserService(DAOFactory factory) {
        this.userDAO = factory.createUserDAO();
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
        userDAO.addUser(user);
    }

    public void updateUser(User user) {
        logger.info("Оновлення користувача з ID: {}", user.getUserId());
        userDAO.updateUser(user);
    }

    public void deleteUser(int userId) {
        logger.info("Видалення користувача з ID: {}", userId);
        userDAO.deleteUser(userId);
    }

}
