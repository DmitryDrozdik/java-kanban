package src.managers.classes;

import src.managers.interfaces.HistoryManager;
import src.tasks.Task;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList history = new CustomLinkedList();

    @Override
    public void add(Task task) {
        history.linkLast(task);
    }

    @Override
    public void remove(int id) {
        history.removeNode(id);
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }
}
