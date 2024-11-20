package org.example.ttpp_knt222_zhadan.service;

import org.example.ttpp_knt222_zhadan.controller.ClaimController;
import org.example.ttpp_knt222_zhadan.dao.EquipmentDAO;
import org.example.ttpp_knt222_zhadan.dao.DAOFactory;
import org.example.ttpp_knt222_zhadan.model.Equipment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {
    private static final Logger logger = LoggerFactory.getLogger(ClaimController.class);
    private final EquipmentDAO equipmentDAO;

    @Autowired
    public EquipmentService(DAOFactory factory){
        this.equipmentDAO = factory.createEquipmentDAO();
    }

    public List<Equipment> getAllEquipment() {
        return equipmentDAO.getAllEquipment();
    }

    public Equipment getEquipmentBySerialNumber(String serialNumber) {
        return equipmentDAO.getEquipmentBySerialNumber(serialNumber);
    }

    public Equipment getOrCreateEquipment(Equipment equipment) {
        Equipment existingEquipment = getEquipmentBySerialNumber(equipment.getSerialNumber());
        if (existingEquipment == null) {
            logger.info("Обладнання не знайдено, додаємо нове: {}", equipment.getSerialNumber());
            addEquipment(equipment);
            existingEquipment = getEquipmentBySerialNumber(equipment.getSerialNumber());
            logger.info("Обладнання додано з ID: {}", existingEquipment.getEquipmentId());
        }
        return existingEquipment;
    }


    public Equipment getEquipmentById(int equipmentId) {
        return equipmentDAO.getEquipmentById(equipmentId);
    }

    public void addEquipment(Equipment equipment) {
        equipmentDAO.addEquipment(equipment);
    }

    public void updateEquipment(Equipment equipment) {
        equipmentDAO.updateEquipment(equipment);
    }

    public void deleteEquipment(int equipmentId) {
        equipmentDAO.deleteEquipment(equipmentId);
    }
}
