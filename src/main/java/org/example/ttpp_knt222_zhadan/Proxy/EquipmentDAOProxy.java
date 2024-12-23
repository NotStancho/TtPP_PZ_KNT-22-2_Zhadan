package org.example.ttpp_knt222_zhadan.Proxy;

import org.example.ttpp_knt222_zhadan.Listener.EquipmentDAOEventListener;
import org.example.ttpp_knt222_zhadan.dao.EquipmentDAO;
import org.example.ttpp_knt222_zhadan.model.Equipment;
import org.example.ttpp_knt222_zhadan.model.User;
import org.example.ttpp_knt222_zhadan.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class EquipmentDAOProxy implements EquipmentDAO {
    private static final Logger logger = LoggerFactory.getLogger(EquipmentDAOProxy.class);
    private final EquipmentDAO equipmentDAO;
    private final UserService userService;

    public EquipmentDAOProxy(EquipmentDAO equipmentDAO, UserService userService) {
        this.equipmentDAO = equipmentDAO;
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
    public List<Equipment> getAllEquipment() {
        checkPermission(List.of("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_SERVICE_ENGINEER"), "View all equipment");
        logger.info("Access granted: Viewing all equipment");
        return equipmentDAO.getAllEquipment();
    }

    @Override
    public Equipment getEquipmentById(int equipmentId) {
        checkPermission(List.of("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_SERVICE_ENGINEER", "ROLE_CLIENT"), "View equipment by ID");
        logger.info("Access granted: Viewing equipment with ID {}", equipmentId);
        return equipmentDAO.getEquipmentById(equipmentId);
    }

    @Override
    public Equipment getEquipmentBySerialNumber(String serialNumber) {
        checkPermission(List.of("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_SERVICE_ENGINEER", "ROLE_CLIENT"), "View equipment by serial number");
        logger.info("Access granted: Viewing equipment with serial number {}", serialNumber);
        return equipmentDAO.getEquipmentBySerialNumber(serialNumber);
    }

    @Override
    public void addEquipment(Equipment equipment) {
        checkPermission(List.of("ROLE_ADMIN", "ROLE_CLIENT"), "Add equipment");
        logger.info("Access granted: Adding equipment");
        equipmentDAO.addEquipment(equipment);
    }

    @Override
    public void updateEquipment(Equipment equipment) {
        checkPermission(List.of("ROLE_ADMIN", "ROLE_MANAGER"), "Update equipment");
        logger.info("Access granted: Updating equipment with ID {}", equipment.getEquipmentId());
        equipmentDAO.updateEquipment(equipment);
    }

    @Override
    public void deleteEquipment(int equipmentId) {
        checkPermission(List.of("ROLE_ADMIN"), "Delete equipment");
        logger.info("Access granted: Deleting equipment with ID {}", equipmentId);
        equipmentDAO.deleteEquipment(equipmentId);
    }

    @Override
    public void addEventListener(EquipmentDAOEventListener listener) {
        equipmentDAO.addEventListener(listener);
    }

    @Override
    public void removeEventListener(EquipmentDAOEventListener listener) {
        equipmentDAO.removeEventListener(listener);
    }
}
