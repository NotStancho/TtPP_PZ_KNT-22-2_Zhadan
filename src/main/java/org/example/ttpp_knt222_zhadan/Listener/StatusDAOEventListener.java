package org.example.ttpp_knt222_zhadan.Listener;

import org.example.ttpp_knt222_zhadan.model.Status;

public interface StatusDAOEventListener {
    void onStatusAdded(Status status);
    void onStatusUpdated(Status status);
    void onStatusDeleted(int statusId);
}
