package org.example.ttpp_knt222_zhadan.Memento;

public class ClaimMemento {
    private final int claimId;
    private final int clientId;
    private final int equipmentId;
    private final int statusId;
    private final String defectDescription;

    public ClaimMemento(int claimId, int clientId, int equipmentId, int statusId, String defectDescription) {
        this.claimId = claimId;
        this.clientId = clientId;
        this.equipmentId = equipmentId;
        this.statusId = statusId;
        this.defectDescription = defectDescription;
    }

    public int getClaimId() {
        return claimId;
    }

    public int getClientId() {
        return clientId;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public int getStatusId() {
        return statusId;
    }

    public String getDefectDescription() {
        return defectDescription;
    }
}
