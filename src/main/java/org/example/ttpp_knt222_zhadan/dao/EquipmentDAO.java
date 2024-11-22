package org.example.ttpp_knt222_zhadan.dao;

import org.example.ttpp_knt222_zhadan.config.DatabaseConnection;
import org.example.ttpp_knt222_zhadan.model.Equipment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAO {
    private static final Logger logger = LoggerFactory.getLogger(EquipmentDAO.class);
    private final Connection connection;

    public EquipmentDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public List<Equipment> getAllEquipment() {
        List<Equipment> equipmentList = new ArrayList<>();
        String sql = "SELECT * FROM equipment";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                equipmentList.add(mapResultSetToEquipment(resultSet));
            }
            logger.info("Отримано все обладнання. Кількість: {}", equipmentList.size());
        } catch (SQLException e) {
            logger.error("Помилка під час отримання всього обладнання", e);
        }
        return equipmentList;
    }

    public Equipment getEquipmentById(int equipmentId) {
        String sql = "SELECT * FROM equipment WHERE equipment_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, equipmentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Equipment equipment = mapResultSetToEquipment(resultSet);
                    logger.info("Отримано обладнання з ID: {}", equipmentId);
                    return equipment;
                } else {
                    logger.warn("Обладнання з ID {} не знайдено", equipmentId);
                }
            }
        } catch (SQLException e) {
            logger.error("Помилка під час отримання обладнання з ID: " + equipmentId, e);
        }
        return null;
    }

    public Equipment getEquipmentBySerialNumber(String serialNumber) {
        String sql = "SELECT * FROM equipment WHERE serial_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, serialNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Equipment equipment = mapResultSetToEquipment(resultSet);
                    logger.info("Обладнання з серійним номером {} знайдено", serialNumber);
                    return equipment;
                } else {
                    logger.warn("Обладнання з серійним номером {} не знайдено", serialNumber);
                }
            }
        } catch (SQLException e) {
            logger.error("Помилка під час отримання обладнання за серійним номером: " + serialNumber, e);
        }
        return null;
    }


    public void addEquipment(Equipment equipment) {
        String sql = "INSERT INTO equipment (serial_number, model, type, purchase_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, equipment.getSerialNumber());
            statement.setString(2, equipment.getModel());
            statement.setString(3, equipment.getType());
            statement.setDate(4, new java.sql.Date(equipment.getPurchaseDate().getTime()));
            statement.executeUpdate();
            logger.info("Обладнання успішно додано: {}", equipment.getSerialNumber());
        } catch (SQLException e) {
            logger.error("Помилка під час додавання обладнання", e);
        }
    }

    public void updateEquipment(Equipment equipment) {
        String sql = "UPDATE equipment SET serial_number = ?, model = ?, type = ?, purchase_date = ? WHERE equipment_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, equipment.getSerialNumber());
            statement.setString(2, equipment.getModel());
            statement.setString(3, equipment.getType());
            statement.setDate(4, new java.sql.Date(equipment.getPurchaseDate().getTime()));
            statement.setInt(5, equipment.getEquipmentId());
            statement.executeUpdate();
            logger.info("Обладнання оновлено: {}", equipment.getSerialNumber());
        } catch (SQLException e) {
            logger.error("Помилка під час оновлення обладнання", e);
        }
    }

    public void deleteEquipment(int equipmentId) {
        String sql = "DELETE FROM equipment WHERE equipment_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, equipmentId);
            statement.executeUpdate();
            logger.info("Обладнання з ID {} видалено", equipmentId);
        } catch (SQLException e) {
            logger.error("Помилка під час видалення обладнання з ID: " + equipmentId, e);
        }
    }

    private Equipment mapResultSetToEquipment(ResultSet resultSet) throws SQLException {
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(resultSet.getInt("equipment_id"));
        equipment.setSerialNumber(resultSet.getString("serial_number"));
        equipment.setModel(resultSet.getString("model"));
        equipment.setType(resultSet.getString("type"));
        equipment.setPurchaseDate(resultSet.getDate("purchase_date"));
        return equipment;
    }
}
