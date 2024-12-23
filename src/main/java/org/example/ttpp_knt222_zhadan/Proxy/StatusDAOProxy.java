package org.example.ttpp_knt222_zhadan.Proxy;

import org.example.ttpp_knt222_zhadan.Listener.StatusDAOEventListener;
import org.example.ttpp_knt222_zhadan.dao.StatusDAO;
import org.example.ttpp_knt222_zhadan.model.Status;
import org.example.ttpp_knt222_zhadan.model.User;
import org.example.ttpp_knt222_zhadan.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class StatusDAOProxy implements StatusDAO {
    private static final Logger logger = LoggerFactory.getLogger(StatusDAOProxy.class);
    private final StatusDAO statusDAO;
    private final UserService userService;

    public StatusDAOProxy(StatusDAO statusDAO, UserService userService) {
        this.statusDAO = statusDAO;
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
    public List<Status> getAllStatus() {
        return statusDAO.getAllStatus();
    }

    @Override
    public Status getStatusById(int statusId) {
        return statusDAO.getStatusById(statusId);
    }

    @Override
    public void addStatus(Status status) {
        checkPermission(List.of("ROLE_ADMIN"), "Add status");
        logger.info("Access granted: Adding status");
        statusDAO.addStatus(status);
    }

    @Override
    public void updateStatus(Status status) {
        checkPermission(List.of("ROLE_ADMIN"), "Update status");
        logger.info("Access granted: Updating status with ID {}", status.getStatusId());
        statusDAO.updateStatus(status);
    }

    @Override
    public void deleteStatus(int statusId) {
        checkPermission(List.of("ROLE_ADMIN"), "Delete status");
        logger.info("Access granted: Deleting status with ID {}", statusId);
        statusDAO.deleteStatus(statusId);
    }

    @Override
    public void addEventListener(StatusDAOEventListener listener) {
        statusDAO.addEventListener(listener);
    }

    @Override
    public void removeEventListener(StatusDAOEventListener listener) {
        statusDAO.removeEventListener(listener);
    }
}
