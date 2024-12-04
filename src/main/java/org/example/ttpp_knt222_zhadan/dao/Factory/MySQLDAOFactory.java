package org.example.ttpp_knt222_zhadan.dao.Factory;

import org.example.ttpp_knt222_zhadan.dao.MySQL.*;
import org.springframework.stereotype.Component;

@Component
public class MySQLDAOFactory implements DAOFactory {
    @Override
    public MySQLUserDAO createUserDAO() { return new MySQLUserDAO(); }

    @Override
    public MySQLClaimDAO createClaimDAO() {
        return new MySQLClaimDAO();
    }

    @Override
    public MySQLStatusDAO createStatusDAO() {
        return new MySQLStatusDAO();
    }

    @Override
    public MySQLEquipmentDAO createEquipmentDAO() {
        return new MySQLEquipmentDAO();
    }

    @Override
    public MySQLClaim_HistoryDAO createClaim_HistoryDAO(){ return new MySQLClaim_HistoryDAO(); }
}
