package org.example.ttpp_knt222_zhadan.Listener;

import org.example.ttpp_knt222_zhadan.model.Equipment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EquipmentEventListener implements EquipmentDAOEventListener{
    private static final Logger logger = LoggerFactory.getLogger(EquipmentEventListener.class);

    @Override
    public void onEquipmentAdded(Equipment equipment) {
        logger.info("Слухач: Додано нове обладнання з ID: {}, серійний номер: {}",
                equipment.getEquipmentId(), equipment.getSerialNumber());
    }

    @Override
    public void onEquipmentUpdated(Equipment equipment) {
        logger.info("Слухач: Оновлено обладнання з ID: {}, серійний номер: {}",
                equipment.getEquipmentId(), equipment.getSerialNumber());
    }

    @Override
    public void onEquipmentDeleted(int equipmentId) {
        logger.info("Слухач: Видалено обладнання з ID: {}", equipmentId);
    }
}
