package org.example.ttpp_knt222_zhadan.service;

import org.example.ttpp_knt222_zhadan.dao.StatusDAO;
import org.example.ttpp_knt222_zhadan.dao.DAOFactory;
import org.example.ttpp_knt222_zhadan.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {
    private final StatusDAO statusDAO;

    @Autowired
    public StatusService(DAOFactory factory) {
        this.statusDAO = factory.createStatusDAO();
    }

    public List<Status> getAllStatuses() {
        return statusDAO.getAllStatuses();
    }

    public Status getStatusById(int statusId) {
        return statusDAO.getStatusById(statusId);
    }

    public void addStatus(Status status) {
        statusDAO.addStatus(status);
    }

    public void updateStatus(Status status){
        statusDAO.updateStatus(status);
    }

    public void deleteStatus(int statusId){
        statusDAO.deleteStatus(statusId);
    }
}
