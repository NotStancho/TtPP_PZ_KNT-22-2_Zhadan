package org.example.ttpp_knt222_zhadan.service;

import org.example.ttpp_knt222_zhadan.Listener.ClaimEventListener;
import org.example.ttpp_knt222_zhadan.dao.ClaimDAO;
import org.example.ttpp_knt222_zhadan.dao.Factory.FabricMethodDAO;
import org.example.ttpp_knt222_zhadan.dao.Factory.TypeDAO;
import org.example.ttpp_knt222_zhadan.dao.Factory.DAOFactory;
import org.example.ttpp_knt222_zhadan.model.Claim;
import org.example.ttpp_knt222_zhadan.model.Equipment;
import org.example.ttpp_knt222_zhadan.model.Status;
import org.example.ttpp_knt222_zhadan.model.builder.StatusBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimService {
    private static final Logger logger = LoggerFactory.getLogger(ClaimService.class);
    private final ClaimDAO claimDAO;
    private final EquipmentService equipmentService;
    private final ClaimEventListener claimEventListener;

    @Autowired
    public ClaimService(EquipmentService equipmentService) {
        DAOFactory factory = FabricMethodDAO.getDAOFactory(TypeDAO.MYSQL);
        this.claimDAO = factory.createClaimDAO();
        this.equipmentService = equipmentService;

        this.claimEventListener = new ClaimEventListener();
        this.claimDAO.addEventListener(claimEventListener);
    }

    public List<Claim> getAllClaims() {
        logger.info("Отримання всіх заявок");
        return claimDAO.getAllClaim();
    }

    public List<Claim> getClientClaims(int clientId) {
        logger.info("Отримання заявок для клієнта з ID: {}", clientId);
        return claimDAO.getClientClaims(clientId);
    }

    public void addClaim(Claim claim, int employeeId) {
        logger.info("Клієнт ID: {}, Співробітник ID: {}", claim.getClient().getUserId(), employeeId);

        Equipment existingEquipment = equipmentService.getOrCreateEquipment(claim.getEquipment());
        if (existingEquipment != null && existingEquipment.getEquipmentId() > 0) {
            claim.setEquipment(existingEquipment);
        } else {
            logger.error("Не вдалося отримати або додати обладнання для серійного номеру: {}",
                    claim.getEquipment().getSerialNumber());
            throw new IllegalStateException("Помилка з обладнанням.");
        }

        Status receivedStatus = new StatusBuilder()
                .setStatusId(1) // Призначаємо статус "Отримано" (id = 1)
                .build();
        claim.setStatus(receivedStatus);

        claimDAO.addClaim(claim, employeeId);
        logger.info("Заявка успішно додана з ID: {}", claim.getClaimId());
    }

    public Claim getClaimById(int claimId) {
        logger.info("Отримання заявки з ID: {}", claimId);
        return claimDAO.getClaimById(claimId);
    }

    public void updateClaim(Claim claim, int employeeId, String actionDescription) {
        claimDAO.updateClaim(claim, employeeId, actionDescription);
        logger.info("Заявка з ID {} успішно оновлена", claim.getClaimId());
    }

    public void deleteClaim(int claimId) {
        logger.info("Видалення заявки з ID: {}", claimId);
        claimDAO.deleteClaim(claimId);
    }

}

