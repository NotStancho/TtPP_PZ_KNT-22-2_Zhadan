package org.example.ttpp_knt222_zhadan.dao.MySQL;

import org.example.ttpp_knt222_zhadan.Listener.StatusDAOEventListener;
import org.example.ttpp_knt222_zhadan.config.DatabaseConnection;
import org.example.ttpp_knt222_zhadan.dao.StatusDAO;
import org.example.ttpp_knt222_zhadan.model.Status;
import org.example.ttpp_knt222_zhadan.model.builder.StatusBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MySQLStatusDAO implements StatusDAO {
    private static final Logger logger = LoggerFactory.getLogger(MySQLStatusDAO.class);
    private final Connection connection;
    private final List<StatusDAOEventListener> listeners = new CopyOnWriteArrayList<>();


    public MySQLStatusDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public List<Status> getAllStatus() {
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

    @Override
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

    @Override
    public void addStatus(Status status) {
        String sql = "INSERT INTO status (name, description) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, status.getName());
            statement.setString(2, status.getDescription());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    status.setStatusId(generatedId);
                    logger.info("Статус успішно додано: {}, з ID: {}", status.getName(), generatedId);
                } else {
                    logger.warn("Не вдалося отримати згенерований ID для статусу: {}", status.getName());
                }
            }

            notifyStatusAdded(status);
        } catch (SQLException e) {
            logger.error("Помилка під час додавання статусу: " + status.getName(), e);
        }
    }

    @Override
    public void updateStatus(Status status) {
        String sql = "UPDATE status SET name = ?, description = ? WHERE status_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.getName());
            statement.setString(2, status.getDescription());
            statement.setInt(3, status.getStatusId());
            statement.executeUpdate();
            logger.info("Статус оновлено: {}", status.getName());
            notifyStatusUpdated(status);
        } catch (SQLException e) {
            logger.error("Помилка під час оновлення статусу: " + status.getName(), e);
        }
    }

    @Override
    public void deleteStatus(int statusId) {
        String sql = "DELETE FROM status WHERE status_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, statusId);
            statement.executeUpdate();
            logger.info("Статус з ID {} видалено", statusId);
            notifyStatusDeleted(statusId);
        } catch (SQLException e) {
            logger.error("Помилка під час видалення статусу з ID: " + statusId, e);
        }
    }

    @Override
    public void addEventListener(StatusDAOEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeEventListener(StatusDAOEventListener listener) {
        listeners.remove(listener);
    }

    private void notifyStatusAdded(Status status) {
        for (StatusDAOEventListener listener : listeners) {
            listener.onStatusAdded(status);
        }
    }

    private void notifyStatusUpdated(Status status) {
        for (StatusDAOEventListener listener : listeners) {
            listener.onStatusUpdated(status);
        }
    }

    private void notifyStatusDeleted(int statusId) {
        for (StatusDAOEventListener listener : listeners) {
            listener.onStatusDeleted(statusId);
        }
    }

    private Status mapResultSetToStatus(ResultSet resultSet) throws SQLException {
        return new StatusBuilder()
                .setStatusId(resultSet.getInt("status_id"))
                .setName(resultSet.getString("name"))
                .setDescription(resultSet.getString("description"))
                .build();
    }
}
