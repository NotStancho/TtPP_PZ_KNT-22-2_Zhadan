package org.example.ttpp_knt222_zhadan.service;

import org.example.ttpp_knt222_zhadan.dao.Claim_HistoryDAO;
import org.example.ttpp_knt222_zhadan.dao.DAOFactory;
import org.example.ttpp_knt222_zhadan.model.Claim_History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Claim_HistoryService {

    private final Claim_HistoryDAO claim_HistoryDAO;

    @Autowired
    public Claim_HistoryService(DAOFactory factory) { this.claim_HistoryDAO = factory.createClaim_HistoryDAO(); }

    public List<Claim_History> getClaimHistory(int claimId) {
        return claim_HistoryDAO.getClaimHistoryByClaimId(claimId);
    }
}
