package org.example.ttpp_knt222_zhadan.Listener;

import org.example.ttpp_knt222_zhadan.model.Equipment;

public interface EquipmentDAOEventListener {
    void onEquipmentAdded(Equipment equipment);
    void onEquipmentUpdated(Equipment equipment);
    void onEquipmentDeleted(int equipmentId);
}
