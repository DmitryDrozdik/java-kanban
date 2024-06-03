package test.classes;

import src.managers.classes.InMemoryHistoryManager;
import src.managers.interfaces.HistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.tasks.Task;
import src.tasks.enums.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty(), "История задач должна быть пустой");
    }

    @Test
    void testDuplication() {
        Task task = new Task("Task 1", "Description 1");
        historyManager.add(task);
        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size(), "Дублирование задач в истории не должно приводить к увеличению размера истории");
        assertEquals(task, historyManager.getHistory().get(0), "Порядок задач в истории должен сохраняться");
    }

    @Test
    void testRemoveFromBeginning() {
        Task task1 = new Task("Task 1", "Description 1", 1, Status.NEW);
        historyManager.add(task1);
        Task task2 = new Task("Task 1", "Description 1", 2, Status.NEW);
        historyManager.add(task2);

        historyManager.remove(task1.getID());

        assertEquals(1, historyManager.getHistory().size(), "Удаление задачи из начала истории должно уменьшать размер истории");
        assertEquals(task2, historyManager.getHistory().get(0), "Порядок задач в истории должен сохраняться после удаления");
    }

    @Test
    void testRemoveFromMiddle() {
        Task task1 = new Task("Task 1", "Description 1", 1, Status.NEW);
        historyManager.add(task1);
        Task task2 = new Task("Task 1", "Description 1", 2, Status.NEW);
        historyManager.add(task2);
        Task task3 = new Task("Task 3", "Description 3", 3, Status.NEW);
        historyManager.add(task3);
        historyManager.remove(task2.getID());

        assertEquals(2, historyManager.getHistory().size(), "Удаление задачи из середины истории должно уменьшать размер истории");
        assertEquals(task1, historyManager.getHistory().get(0), "Порядок задач в истории должен сохраняться после удаления");
        assertEquals(task3, historyManager.getHistory().get(1), "Порядок задач в истории должен сохраняться после удаления");
    }

    @Test
    void testRemoveFromEnd() {
        Task task1 = new Task("Task 1", "Description 1", 1, Status.NEW);
        historyManager.add(task1);
        Task task2 = new Task("Task 1", "Description 1", 2, Status.NEW);
        historyManager.add(task2);

        historyManager.remove(task2.getID());

        assertEquals(1, historyManager.getHistory().size(), "Удаление задачи из конца истории должно уменьшать размер истории");
        assertEquals(task1, historyManager.getHistory().get(0), "Порядок задач в истории должен сохраняться после удаления");
    }
}
