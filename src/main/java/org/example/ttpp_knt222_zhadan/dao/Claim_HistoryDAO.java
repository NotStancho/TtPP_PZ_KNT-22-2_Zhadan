package org.example.ttpp_knt222_zhadan.dao;

import org.example.ttpp_knt222_zhadan.model.Claim_History;

import java.util.List;

public interface Claim_HistoryDAO {
    List<Claim_History> getClaimHistoryByClaimId(int claimId);
}
