package org.example.ttpp_knt222_zhadan.Memento;

import java.util.Stack;

public class ClaimHistory {
    private final Stack<ClaimMemento> history = new Stack<>();

    public void save(ClaimMemento memento) {
        history.push(memento);
    }
    public ClaimMemento undo() {
        if (!history.isEmpty()) {
            return history.pop();
        }
        return null;
    }
    public boolean hasHistory() {
        return !history.isEmpty();
    }
}
