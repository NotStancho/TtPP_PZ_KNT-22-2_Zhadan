package org.example.ttpp_knt222_zhadan.dao.MySQL;

import org.example.ttpp_knt222_zhadan.Listener.EquipmentDAOEventListener;
import org.example.ttpp_knt222_zhadan.config.DatabaseConnection;
import org.example.ttpp_knt222_zhadan.dao.EquipmentDAO;
import org.example.ttpp_knt222_zhadan.model.Equipment;
import org.example.ttpp_knt222_zhadan.model.builder.EquipmentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MySQLEquipmentDAO implements EquipmentDAO {
    private static final Logger logger = LoggerFactory.getLogger(MySQLEquipmentDAO.class);
    private final Connection connection;
    private final List<EquipmentDAOEventListener> listeners = new CopyOnWriteArrayList<>();

    public MySQLEquipmentDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public void addEquipment(Equipment equipment) {
        String sql = "INSERT INTO equipment (serial_number, model, type, purchase_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, equipment.getSerialNumber());
            statement.setString(2, equipment.getModel());
            statement.setString(3, equipment.getType());
            statement.setDate(4, new java.sql.Date(equipment.getPurchaseDate().getTime()));
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    equipment.setEquipmentId(generatedId);
                    logger.info("Обладнання успішно додано: {}, з ID: {}", equipment.getSerialNumber(), generatedId);
                } else {
                    logger.warn("Не вдалося отримати згенерований ID для обладнання: {}", equipment.getSerialNumber());
                }
            }
            notifyEquipmentAdded(equipment);
        } catch (SQLException e) {
            logger.error("Помилка під час додавання обладнання", e);
        }
    }
    @Override
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
            notifyEquipmentUpdated(equipment);
        } catch (SQLException e) {
            logger.error("Помилка під час оновлення обладнання", e);
        }
    }
    @Override
    public void deleteEquipment(int equipmentId) {
        String sql = "DELETE FROM equipment WHERE equipment_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, equipmentId);
            statement.executeUpdate();
            logger.info("Обладнання з ID {} видалено", equipmentId);
            notifyEquipmentDeleted(equipmentId);
        } catch (SQLException e) {
            logger.error("Помилка під час видалення обладнання з ID: " + equipmentId, e);
        }
    }

    @Override
    public void addEventListener(EquipmentDAOEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeEventListener(EquipmentDAOEventListener listener) {
        listeners.remove(listener);
    }

    private void notifyEquipmentAdded(Equipment equipment) {
        for (EquipmentDAOEventListener listener : listeners) {
            listener.onEquipmentAdded(equipment);
        }
    }

    private void notifyEquipmentUpdated(Equipment equipment) {
        for (EquipmentDAOEventListener listener : listeners) {
            listener.onEquipmentUpdated(equipment);
        }
    }

    private void notifyEquipmentDeleted(int equipmentId) {
        for (EquipmentDAOEventListener listener : listeners) {
            listener.onEquipmentDeleted(equipmentId);
        }
    }

    private Equipment mapResultSetToEquipment(ResultSet resultSet) throws SQLException {
        return new EquipmentBuilder()
                .setEquipmentId(resultSet.getInt("equipment_id"))
                .setSerialNumber(resultSet.getString("serial_number"))
                .setModel(resultSet.getString("model"))
                .setType(resultSet.getString("type"))
                .setPurchaseDate(resultSet.getDate("purchase_date"))
                .build();
    }
}
