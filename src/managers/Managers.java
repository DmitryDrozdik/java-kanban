package managers;

import managers.classes.InMemoryHistoryManager;
import managers.classes.InMemoryTaskManager;
import managers.interfaces.HistoryManager;
import managers.interfaces.TaskManager;

public class Managers {
    public static TaskManager getDefaultManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
