package org.example.ttpp_knt222_zhadan.dao;

import org.example.ttpp_knt222_zhadan.config.DatabaseConnection;
import org.example.ttpp_knt222_zhadan.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    private final Connection connection;

    public UserDAO() {
        this.connection = DatabaseConnection.getConnection();
        logger.info("Підключення до бази даних встановлено");
    }

    public List<User> getAllUsers() {
        logger.info("Отримання всіх користувачів");
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            logger.error("Помилка під час отримання всіх користувачів", e);
            throw new RuntimeException("Помилка під час отримання всіх користувачів", e);
        }
        return users;
    }

    public User getUserById(int userId) {
        logger.info("Отримання користувача з ID: {}", userId);
        String sql = "SELECT * FROM user WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    logger.info("Користувача з ID: {} знайдено", userId);
                    return mapResultSetToUser(resultSet);
                } else {
                    logger.warn("Користувача з ID: {} не знайдено", userId);
                }
            }
        } catch (SQLException e) {
            logger.error("Помилка під час отримання користувача з ID: {}", userId, e);
            throw new RuntimeException("Помилка під час отримання користувача з ID: " + userId, e);
        }
        return null;
    }

    public User getUserByEmail(String email) {
        logger.info("Отримання користувача за email: {}", email);
        String sql = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    logger.info("Користувача з email: {} знайдено", email);
                    return mapResultSetToUser(resultSet);
                } else {
                    logger.warn("Користувача з email: {} не знайдено", email);
                }
            }
        } catch (SQLException e) {
            logger.error("Помилка під час пошуку користувача за email: {}", email, e);
            throw new RuntimeException("Помилка під час пошуку користувача за email: " + email, e);
        }
        return null;
    }

    public void addUser(User user) {
        logger.info("Додавання користувача: {}", user.getEmail());
        String sql = "INSERT INTO user (firstname, lastname, email, password, phone, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getFirstname());
            statement.setString(2, user.getLastname());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getPhone());
            statement.setString(6, user.getRole());
            statement.executeUpdate();
            logger.info("Користувача з email: {} успішно додано", user.getEmail());
        } catch (SQLException e) {
            logger.error("Помилка під час додавання користувача: {}", user.getEmail(), e);
            throw new RuntimeException("Помилка під час додавання користувача: " + user.getEmail(), e);
        }
    }

    public void updateUser(User user) {
        logger.info("Оновлення користувача з ID: {}", user.getUserId());
        String sql = "UPDATE user SET firstname = ?, lastname = ?, email = ?, password = ?, phone = ?, role = ? WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getFirstname());
            statement.setString(2, user.getLastname());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getPhone());
            statement.setString(6, user.getRole());
            statement.setLong(7, user.getUserId());
            statement.executeUpdate();
            logger.info("Користувача з ID: {} успішно оновлено", user.getUserId());
        } catch (SQLException e) {
            logger.error("Помилка під час оновлення користувача з ID: {}", user.getUserId(), e);
            throw new RuntimeException("Помилка під час оновлення користувача з ID: " + user.getUserId(), e);
        }
    }

    public void deleteUser(int userId) {
        logger.info("Видалення користувача з ID: {}", userId);
        String sql = "DELETE FROM user WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
            logger.info("Користувача з ID: {} успішно видалено", userId);
        } catch (SQLException e) {
            logger.error("Помилка під час видалення користувача з ID: {}", userId, e);
            throw new RuntimeException("Помилка під час видалення користувача з ID: " + userId, e);
        }
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getInt("user_id"));
        user.setFirstname(resultSet.getString("firstname"));
        user.setLastname(resultSet.getString("lastname"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setPhone(resultSet.getString("phone"));
        user.setRole(resultSet.getString("role"));
        return user;
    }
}
