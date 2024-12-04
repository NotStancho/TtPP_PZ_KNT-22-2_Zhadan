package org.example.ttpp_knt222_zhadan.Listener;

import org.example.ttpp_knt222_zhadan.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserEventListener implements UserDAOEventListener{
    private static final Logger logger = LoggerFactory.getLogger(UserEventListener.class);

    @Override
    public void onUserAdded(User user) {
        logger.info("Слухач: Додано нового користувача з ID: {}, Email: {}", user.getUserId(), user.getEmail());
    }

    @Override
    public void onUserUpdated(User user) {
        logger.info("Слухач: Оновлено користувача з ID: {}, Email: {}", user.getUserId(), user.getEmail());
    }

    @Override
    public void onUserDeleted(int userId) {
        logger.info("Слухач: Видалено користувача з ID: {}", userId);
    }
}
