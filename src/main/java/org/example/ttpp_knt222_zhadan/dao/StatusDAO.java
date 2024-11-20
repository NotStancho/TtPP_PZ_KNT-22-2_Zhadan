package org.example.ttpp_knt222_zhadan.dao;

import org.example.ttpp_knt222_zhadan.config.DatabaseConnection;
import org.example.ttpp_knt222_zhadan.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatusDAO {
    private static final Logger logger = LoggerFactory.getLogger(StatusDAO.class);
    private final Connection connection;

    public StatusDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public List<Status> getAllStatuses() {
        List<Status> statuses = new ArrayList<>();
        String sql = "SELECT * FROM status";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                statuses.add(mapResultSetToStatus(resultSet));
            }
            logger.info("Отримано всі статуси. Кількість: {}", statuses.size());
        } catch (SQLException e) {
            logger.error("Помилка під час отримання всіх статусів", e);
        }
        return statuses;
    }

    public Status getStatusById(int statusId) {
        String sql = "SELECT * FROM status WHERE status_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, statusId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Status status = mapResultSetToStatus(resultSet);
                    logger.info("Отримано статус з ID: {}", statusId);
                    return status;
                } else {
                    logger.warn("Статус з ID {} не знайдено", statusId);
                }
            }
        } catch (SQLException e) {
            logger.error("Помилка під час отримання статусу з ID: " + statusId, e);
        }
        return null;
    }

    public void addStatus(Status status) {
        String sql = "INSERT INTO status (name, description) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.getName());
            statement.setString(2, status.getDescription());
            statement.executeUpdate();
            logger.info("Статус успішно додано: {}", status.getName());
        } catch (SQLException e) {
            logger.error("Помилка під час додавання статусу: " + status.getName(), e);
        }
    }

    public void updateStatus(Status status) {
        String sql = "UPDATE status SET name = ?, description = ? WHERE status_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.getName());
            statement.setString(2, status.getDescription());
            statement.setInt(3, status.getStatusId());
            statement.executeUpdate();
            logger.info("Статус оновлено: {}", status.getName());
        } catch (SQLException e) {
            logger.error("Помилка під час оновлення статусу: " + status.getName(), e);
        }
    }

    public void deleteStatus(int statusId) {
        String sql = "DELETE FROM status WHERE status_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, statusId);
            statement.executeUpdate();
            logger.info("Статус з ID {} видалено", statusId);
        } catch (SQLException e) {
            logger.error("Помилка під час видалення статусу з ID: " + statusId, e);
        }
    }

    private Status mapResultSetToStatus(ResultSet resultSet) throws SQLException {
        Status status = new Status();
        status.setStatusId(resultSet.getInt("status_id"));
        status.setName(resultSet.getString("name"));
        status.setDescription(resultSet.getString("description"));
        return status;
    }
}
