package org.example.ttpp_knt222_zhadan.dao.Factory;

import org.example.ttpp_knt222_zhadan.dao.*;

public interface DAOFactory {
    UserDAO createUserDAO();
    ClaimDAO createClaimDAO();
    StatusDAO createStatusDAO();
    EquipmentDAO createEquipmentDAO();
    Claim_HistoryDAO createClaim_HistoryDAO();
}