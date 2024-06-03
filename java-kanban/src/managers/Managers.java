package src.managers;

import src.managers.classes.InMemoryHistoryManager;
import src.managers.classes.InMemoryTaskManager;
import src.managers.interfaces.HistoryManager;
import src.managers.interfaces.TaskManager;

public class Managers {
    public static TaskManager getDefaultManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
