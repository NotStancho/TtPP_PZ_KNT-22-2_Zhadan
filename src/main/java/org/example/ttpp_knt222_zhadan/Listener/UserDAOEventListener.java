package org.example.ttpp_knt222_zhadan.Listener;

import org.example.ttpp_knt222_zhadan.model.User;

public interface UserDAOEventListener {
    void onUserAdded(User user);
    void onUserUpdated(User user);
    void onUserDeleted(int userId);
}
