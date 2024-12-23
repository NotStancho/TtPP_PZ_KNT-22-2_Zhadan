package org.example.ttpp_knt222_zhadan.service;

import org.example.ttpp_knt222_zhadan.Listener.EquipmentEventListener;
import org.example.ttpp_knt222_zhadan.Proxy.EquipmentDAOProxy;
import org.example.ttpp_knt222_zhadan.controller.ClaimController;
import org.example.ttpp_knt222_zhadan.dao.Factory.FabricMethodDAO;
import org.example.ttpp_knt222_zhadan.dao.Factory.TypeDAO;
import org.example.ttpp_knt222_zhadan.dao.Factory.DAOFactory;
import org.example.ttpp_knt222_zhadan.dao.EquipmentDAO;
import org.example.ttpp_knt222_zhadan.model.Equipment;
import org.example.ttpp_knt222_zhadan.model.builder.EquipmentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class EquipmentService {
    private static final Logger logger = LoggerFactory.getLogger(ClaimController.class);
    private final EquipmentDAO equipmentDAO;
    private final EquipmentEventListener equipmentEventListener;

    public EquipmentService(UserService userService){
        DAOFactory factory = FabricMethodDAO.getDAOFactory(TypeDAO.MYSQL);
        EquipmentDAO originalEquipmentDAO = factory.createEquipmentDAO();
        this.equipmentDAO = new EquipmentDAOProxy(originalEquipmentDAO, userService);
        this.equipmentEventListener = new EquipmentEventListener();
        this.equipmentDAO.addEventListener(equipmentEventListener);
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
        Equipment newEquipment = new EquipmentBuilder()
                .setSerialNumber(equipment.getSerialNumber())
                .setModel(equipment.getModel())
                .setType(equipment.getType())
                .setPurchaseDate(equipment.getPurchaseDate())
                .build();
        equipmentDAO.addEquipment(newEquipment);
    }

    public void updateEquipment(Equipment equipment) {
        Equipment updatedEquipment = new EquipmentBuilder()
                .setEquipmentId(equipment.getEquipmentId())
                .setSerialNumber(equipment.getSerialNumber())
                .setModel(equipment.getModel())
                .setType(equipment.getType())
                .setPurchaseDate(equipment.getPurchaseDate())
                .build();
        equipmentDAO.updateEquipment(updatedEquipment);
    }

    public void deleteEquipment(int equipmentId) {
        equipmentDAO.deleteEquipment(equipmentId);
    }
}
