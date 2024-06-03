package test.classes;

import src.managers.classes.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.tasks.Epic;
import src.tasks.Subtask;
import src.tasks.Task;
import src.tasks.enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    @BeforeEach
    protected void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void testSubtaskEpicPresence() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Description 1", epic.getID());
        taskManager.createSubtask(subtask);

        assertTrue(taskManager.getEpicByID(epic.getID()).getSubtaskList().contains(subtask.getID()),
                "Эпик должен содержать созданную подзадачу");
    }

    @Test
    void testEpicStatusCalculation() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic.getID());
        subtask1.setStatus(Status.NEW);
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic.getID());
        subtask2.setStatus(Status.DONE);
        taskManager.createSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, taskManager.getEpicByID(epic.getID()).getStatus(),
                "Статус эпика должен быть IN_PROGRESS, когда часть подзадач имеет статус NEW, а часть - DONE");
    }

    @Test
    void testIntersectionCalculation() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setStartTime(LocalDateTime.of(2023, 6, 10, 10, 0));
        task1.setDuration(Duration.ofHours(2));
        taskManager.createTask(task1);

        Task task2 = new Task("Task 2", "Description 2");
        task2.setStartTime(LocalDateTime.of(2023, 6, 10, 11, 0));
        task2.setDuration(Duration.ofHours(1));

        assertTrue(taskManager.hasIntersections(task2),
                "Должно быть обнаружено пересечение интервалов");

        Task task3 = new Task("Task 3", "Description 3");
        task3.setStartTime(LocalDateTime.of(2023, 6, 10, 14, 0));
        task3.setDuration(Duration.ofHours(1));

        assertFalse(taskManager.hasIntersections(task3),
                "Не должно быть обнаружено пересечение интервалов");
    }
}
