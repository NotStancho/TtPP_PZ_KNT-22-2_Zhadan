package org.example.ttpp_knt222_zhadan.dao;

import org.springframework.stereotype.Component;

@Component
public class MySQLDAOFactory implements DAOFactory {

    @Override
    public UserDAO createUserDAO() {
        return new UserDAO();
    }

    @Override
    public ClaimDAO createClaimDAO() {
        return new ClaimDAO();
    }

    @Override
    public StatusDAO createStatusDAO() {
        return new StatusDAO();
    }

    @Override
    public EquipmentDAO createEquipmentDAO() {
        return new EquipmentDAO();
    }

    @Override
    public Claim_HistoryDAO createClaim_HistoryDAO(){ return new Claim_HistoryDAO(); }
}
