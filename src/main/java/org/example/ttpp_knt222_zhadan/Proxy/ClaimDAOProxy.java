package org.example.ttpp_knt222_zhadan.Proxy;

import org.example.ttpp_knt222_zhadan.Listener.ClaimDAOEventListener;
import org.example.ttpp_knt222_zhadan.dao.ClaimDAO;
import org.example.ttpp_knt222_zhadan.model.Claim;
import org.example.ttpp_knt222_zhadan.model.User;
import org.example.ttpp_knt222_zhadan.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class ClaimDAOProxy implements ClaimDAO {
    private static final Logger logger = LoggerFactory.getLogger(ClaimDAOProxy.class);
    private final ClaimDAO claimDAO;
    private final UserService userService;

    public ClaimDAOProxy(ClaimDAO claimDAO, UserService userService) {
        this.claimDAO = claimDAO;
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
    public List<Claim> getAllClaim() {
        checkPermission(List.of("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_SERVICE_ENGINEER"), "View all claims");
        logger.info("Access granted: Viewing all claims");
        return claimDAO.getAllClaim();
    }

    @Override
    public List<Claim> getClientClaims(int clientId) {
        checkPermission(List.of("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_SERVICE_ENGINEER", "ROLE_CLIENT"), "View client claims");
        logger.info("Access granted: Viewing claims for client with ID {}", clientId);
        return claimDAO.getClientClaims(clientId);
    }

    @Override
    public void addClaim(Claim claim, int employeeId) {
        checkPermission(List.of("ROLE_CLIENT", "ROLE_ADMIN"), "Add claim");
        logger.info("Access granted: Adding claim by employee ID {}", employeeId);
        claimDAO.addClaim(claim, employeeId);
    }

    @Override
    public Claim getClaimById(int claimId) {
        checkPermission(List.of("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_SERVICE_ENGINEER", "ROLE_CLIENT"), "View claim details");
        logger.info("Access granted: Viewing claim with ID {}", claimId);
        return claimDAO.getClaimById(claimId);
    }

    @Override
    public void updateClaim(Claim claim, int employeeId, String description) {
        checkPermission(List.of("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_SERVICE_ENGINEER"), "Update claim");
        logger.info("Access granted: Updating claim with ID {}", claim.getClaimId());
        claimDAO.updateClaim(claim, employeeId, description);
    }

    @Override
    public void deleteClaim(int claimId) {
        checkPermission(List.of("ROLE_ADMIN"), "Delete claim");
        logger.info("Access granted: Deleting claim with ID {}", claimId);
        claimDAO.deleteClaim(claimId);
    }

    @Override
    public void addEventListener(ClaimDAOEventListener listener) {
        claimDAO.addEventListener(listener);
    }

    @Override
    public void removeEventListener(ClaimDAOEventListener listener) {
        claimDAO.removeEventListener(listener);
    }
}
