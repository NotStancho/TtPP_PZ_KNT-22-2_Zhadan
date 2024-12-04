package org.example.ttpp_knt222_zhadan.Listener;

import org.example.ttpp_knt222_zhadan.model.Claim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClaimEventListener implements ClaimDAOEventListener{
    private static final Logger logger = LoggerFactory.getLogger(ClaimEventListener.class);

    @Override
    public void onClaimAdded(Claim claim) {
        logger.info("Слухач: Додано нову заявку з ID: {}", claim.getClaimId());
    }

    @Override
    public void onClaimUpdated(Claim claim) {
        logger.info("Слухач: Оновлено заявку з ID: {}", claim.getClaimId());
    }

    @Override
    public void onClaimDeleted(int claimId) {
        logger.info("Слухач: Видалено заявку з ID: {}", claimId);
    }
}
