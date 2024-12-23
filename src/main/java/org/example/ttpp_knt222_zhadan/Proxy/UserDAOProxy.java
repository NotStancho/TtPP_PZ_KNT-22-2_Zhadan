package org.example.ttpp_knt222_zhadan.Proxy;

import org.example.ttpp_knt222_zhadan.Listener.UserDAOEventListener;
import org.example.ttpp_knt222_zhadan.dao.UserDAO;
import org.example.ttpp_knt222_zhadan.model.User;
import org.example.ttpp_knt222_zhadan.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class UserDAOProxy implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOProxy.class);
    private final UserDAO userDAO;
    private final UserService userService;

    public UserDAOProxy(UserDAO userDAO, UserService userService) {
        this.userDAO = userDAO;
        this.userService = userService;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User currentUser = userService.getUserByEmail(email);
            logger.info("Loaded current user: {}, role: {}", currentUser.getEmail(), currentUser.getRole());
            return currentUser;
        }
        logger.warn("Authentication is null or user is not authenticated");
        return null;
    }

    private void checkPermission(List<String> allowedRoles, String action) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            logger.warn("Access Denied: Current user is null. Action: {}", action);
            throw new RuntimeException("Current user is null");
        }
        if (!allowedRoles.contains(currentUser.getRole())) {
            String message = String.format("Access Denied: %s is allowed only for roles %s!", action, allowedRoles);
            logger.warn(message);
            throw new RuntimeException(message);
        }
    }

    @Override
    public List<User> getAllUsers() {
        checkPermission(List.of("ROLE_ADMIN"), "View all users");
        logger.info("Access granted: Viewing all users");
        return userDAO.getAllUsers();
    }

    @Override
    public User getUserById(int userId) {
        checkPermission(List.of("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_SERVICE_ENGINEER"), "View user by ID");
        logger.info("Access granted: Viewing user with ID {}", userId);
        return userDAO.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    @Override
    public void addUser(User user) {
        userDAO.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("Current user is null");
        }
        // Якщо користувач оновлює свій власний профіль
        if (currentUser.getUserId() == user.getUserId()) {
            logger.info("Access granted: Updating own profile for user with ID {}", user.getUserId());
        }
        // Якщо адміністратор оновлює дані будь-кого
        else if ("ROLE_ADMIN".equals(currentUser.getRole())) {
            logger.info("Access granted: Admin updating user with ID {}", user.getUserId());
        }
        // Якщо інша роль намагається оновити дані іншого користувача
        else {
            String message = String.format("Access Denied: User with ID %d can only update their own profile or requires admin privileges.", currentUser.getUserId());
            logger.warn(message);
            throw new RuntimeException(message);
        }

        userDAO.updateUser(user);
    }

    @Override
    public void deleteUser(int userId) {
        checkPermission(List.of("ROLE_ADMIN"), "Delete user");
        logger.info("Access granted: Deleting user with ID {}", userId);
        userDAO.deleteUser(userId);
    }

    @Override
    public void addEventListener(UserDAOEventListener listener) {
        userDAO.addEventListener(listener);
    }

    @Override
    public void removeEventListener(UserDAOEventListener listener) {
        userDAO.removeEventListener(listener);
    }
}
