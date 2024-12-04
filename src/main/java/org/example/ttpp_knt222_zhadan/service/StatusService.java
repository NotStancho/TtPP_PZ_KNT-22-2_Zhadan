package org.example.ttpp_knt222_zhadan.service;

import org.example.ttpp_knt222_zhadan.Listener.StatusEventListener;
import org.example.ttpp_knt222_zhadan.dao.Factory.FabricMethodDAO;
import org.example.ttpp_knt222_zhadan.dao.Factory.TypeDAO;
import org.example.ttpp_knt222_zhadan.dao.Factory.DAOFactory;
import org.example.ttpp_knt222_zhadan.dao.StatusDAO;
import org.example.ttpp_knt222_zhadan.model.Status;
import org.example.ttpp_knt222_zhadan.model.builder.StatusBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {
    private final StatusDAO statusDAO;
    private final StatusEventListener statusEventListener;

    public StatusService() {
        DAOFactory factory = FabricMethodDAO.getDAOFactory(TypeDAO.MYSQL);
        this.statusDAO = factory.createStatusDAO();
        this.statusEventListener = new StatusEventListener();
        this.statusDAO.addEventListener(statusEventListener);
    }

    public List<Status> getAllStatuses() {
        return statusDAO.getAllStatus();
    }

    public Status getStatusById(int statusId) {
        return statusDAO.getStatusById(statusId);
    }

    public void addStatus(Status status) {
        Status newStatus = new StatusBuilder()
                .setName(status.getName())
                .setDescription(status.getDescription())
                .build();
        statusDAO.addStatus(newStatus);
    }

    public void updateStatus(Status status){
        Status updatedStatus = new StatusBuilder()
                .setStatusId(status.getStatusId())
                .setName(status.getName())
                .setDescription(status.getDescription())
                .build();
        statusDAO.updateStatus(updatedStatus);
    }

    public void deleteStatus(int statusId){
        statusDAO.deleteStatus(statusId);
    }
}
