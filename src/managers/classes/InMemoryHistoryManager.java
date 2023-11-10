package managers.classes;

import managers.interfaces.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Task> history = new HashMap<>();
    private static  final int MAX_TASK_SIZE = 10;

    @Override
    public void add(Task task) {
        if (history.size() == MAX_TASK_SIZE) {
            history.remove(0);
        }
        history.put(task.getID(), task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history.values());
    }
}
