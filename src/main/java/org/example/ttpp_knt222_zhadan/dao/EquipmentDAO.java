package org.example.ttpp_knt222_zhadan.dao;

import org.example.ttpp_knt222_zhadan.Listener.EquipmentDAOEventListener;
import org.example.ttpp_knt222_zhadan.model.Equipment;

import java.util.List;

public interface EquipmentDAO {
    List<Equipment> getAllEquipment();

    Equipment getEquipmentById(int equipmentId);

    Equipment getEquipmentBySerialNumber(String serialNumber);

    void addEquipment(Equipment equipment);

    void updateEquipment(Equipment equipment);

    void deleteEquipment(int equipmentId);

    void addEventListener(EquipmentDAOEventListener listener);
    void removeEventListener(EquipmentDAOEventListener listener);
}
