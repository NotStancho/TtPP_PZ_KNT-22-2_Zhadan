package org.example.ttpp_knt222_zhadan.model;

import org.example.ttpp_knt222_zhadan.Memento.ClaimMemento;
import org.example.ttpp_knt222_zhadan.dao.EquipmentDAO;
import org.example.ttpp_knt222_zhadan.dao.StatusDAO;
import org.example.ttpp_knt222_zhadan.dao.UserDAO;

public class Claim {
    private int claimId;
    private User client;
    private Equipment equipment;
    private Status status;
    private String defectDescription;

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) { this.status = status; }

    public String getDefectDescription() {
        return defectDescription;
    }

    public void setDefectDescription(String defectDescription) {
        this.defectDescription = defectDescription;
    }

    public ClaimMemento saveStateToMemento() {
        return new ClaimMemento(
                this.claimId,
                this.client.getUserId(),
                this.equipment.getEquipmentId(),
                this.status.getStatusId(),
                this.defectDescription
        );
    }
    public void restoreStateFromMemento(ClaimMemento memento, UserDAO userDAO, EquipmentDAO equipmentDAO, StatusDAO statusDAO) {
        this.claimId = memento.getClaimId();
        this.client = userDAO.getUserById(memento.getClientId());
        this.equipment = equipmentDAO.getEquipmentById(memento.getEquipmentId());
        this.status = statusDAO.getStatusById(memento.getStatusId());
        this.defectDescription = memento.getDefectDescription();
    }
}
