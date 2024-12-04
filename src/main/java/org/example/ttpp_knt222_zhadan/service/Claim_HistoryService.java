package org.example.ttpp_knt222_zhadan.service;

import org.example.ttpp_knt222_zhadan.dao.Claim_HistoryDAO;
import org.example.ttpp_knt222_zhadan.dao.Factory.FabricMethodDAO;
import org.example.ttpp_knt222_zhadan.dao.Factory.TypeDAO;
import org.example.ttpp_knt222_zhadan.dao.MySQL.MySQLClaim_HistoryDAO;
import org.example.ttpp_knt222_zhadan.dao.Factory.DAOFactory;
import org.example.ttpp_knt222_zhadan.model.Claim_History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Claim_HistoryService {

    private final Claim_HistoryDAO claimHistoryDAO;

    public Claim_HistoryService() {
        DAOFactory factory = FabricMethodDAO.getDAOFactory(TypeDAO.MYSQL);
        this.claimHistoryDAO = factory.createClaim_HistoryDAO();
    }

    public List<Claim_History> getClaimHistory(int claimId) {
        return claimHistoryDAO.getClaimHistoryByClaimId(claimId);
    }
}
