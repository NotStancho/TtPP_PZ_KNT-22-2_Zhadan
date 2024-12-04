package org.example.ttpp_knt222_zhadan.Listener;

import org.example.ttpp_knt222_zhadan.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusEventListener implements StatusDAOEventListener{
    private static final Logger logger = LoggerFactory.getLogger(StatusEventListener.class);

    @Override
    public void onStatusAdded(Status status) {
        logger.info("Слухач: Додано новий статус з ID: {}, Назва: {}", status.getStatusId(), status.getName());
    }

    @Override
    public void onStatusUpdated(Status status) {
        logger.info("Слухач: Оновлено статус з ID: {}, Назва: {}", status.getStatusId(), status.getName());
    }

    @Override
    public void onStatusDeleted(int statusId) {
        logger.info("Слухач: Видалено статус з ID: {}", statusId);
    }
}
