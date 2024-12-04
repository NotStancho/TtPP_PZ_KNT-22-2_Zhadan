package org.example.ttpp_knt222_zhadan.dao;

import org.example.ttpp_knt222_zhadan.Listener.StatusDAOEventListener;
import org.example.ttpp_knt222_zhadan.model.Status;

import java.util.List;

public interface StatusDAO {
    List<Status> getAllStatus();
    Status getStatusById(int statusId);
    void addStatus(Status status);
    void updateStatus(Status status);
    void deleteStatus(int statusId);

    void addEventListener(StatusDAOEventListener listener);
    void removeEventListener(StatusDAOEventListener listener);
}
