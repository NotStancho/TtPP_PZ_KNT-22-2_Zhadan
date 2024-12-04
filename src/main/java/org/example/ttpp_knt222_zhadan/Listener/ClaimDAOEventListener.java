package org.example.ttpp_knt222_zhadan.Listener;

import org.example.ttpp_knt222_zhadan.model.Claim;

public interface ClaimDAOEventListener {
    void onClaimAdded(Claim claim);
    void onClaimUpdated(Claim claim);
    void onClaimDeleted(int claimId);
}
