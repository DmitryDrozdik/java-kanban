package tests.tasks;

import managers.classes.InMemoryTaskManager;
import managers.interfaces.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.enums.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicStatusTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }
    @Test
    void testAllSubtasksNew() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic.getID());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic.getID());
        taskManager.createSubtask(subtask2);

        epic.addSubtask(subtask1.getID());
        epic.addSubtask(subtask2.getID());

        Epic updatedEpic = taskManager.getEpicByID(epic.getID());
        assertEquals(Status.NEW, updatedEpic.getStatus(), "Статус эпика должен быть NEW, когда все подзадачи имеют статус NEW");
    }

    @Test
    void testAllSubtasksDone() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic.getID());
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic.getID());
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic updatedEpic = taskManager.getEpicByID(epic.getID());
        assertEquals(Status.DONE, updatedEpic.getStatus(), "Статус эпика должен быть DONE, когда все подзадачи имеют статус DONE");
    }

    @Test
    void testSubtasksNewAndDone() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic.getID());
        subtask1.setStatus(Status.NEW);
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic.getID());
        subtask2.setStatus(Status.DONE);
        taskManager.createSubtask(subtask2);

        Epic updatedEpic = taskManager.getEpicByID(epic.getID());
        assertEquals(Status.IN_PROGRESS, updatedEpic.getStatus(), "Статус эпика должен быть IN_PROGRESS, когда часть подзадач имеет статус NEW, а часть - DONE");
    }

    @Test
    void testSubtasksInProgress() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic.getID());
        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic.getID());
        subtask2.setStatus(Status.NEW);
        taskManager.createSubtask(subtask2);

        Epic updatedEpic = taskManager.getEpicByID(epic.getID());
        assertEquals(Status.IN_PROGRESS, updatedEpic.getStatus(), "Статус эпика должен быть IN_PROGRESS, когда хотя бы одна подзадача имеет статус IN_PROGRESS");
    }
}
