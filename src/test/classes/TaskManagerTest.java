package test.classes;

import managers.interfaces.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @BeforeEach
    abstract void setUp();

    @Test
    void testCreateTask() {
        Task task = new Task("Task1", "Description1");
        taskManager.createTask(task);
        assertEquals(task, taskManager.getTaskByID(task.getID()));
    }

    @Test
    void testCreateEpic() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);
        assertEquals(epic, taskManager.getEpicByID(epic.getID()));
    }

    @Test
    void testCreateSubtask() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 1", epic.getID());
        taskManager.createSubtask(subtask);
        assertEquals(subtask, taskManager.getSubtaskByID(subtask.getID()));
    }

    @Test
    void testDeleteAllTasks() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void testDeleteAllEpics() {
        Epic epic1 = new Epic("Epic 1", "Description 1");
        Epic epic2 = new Epic("Epic 2", "Description 2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test
    void testDeleteAllSubtasks() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic.getID());
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic.getID());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.deleteAllSubtasks();
        assertTrue(taskManager.getAllSubtasks().isEmpty());
    }

    @Test
    void testDeleteTaskByID() {
        Task task = new Task("Task 1", "Description 1");
        taskManager.createTask(task);
        taskManager.deleteTaskByID(task.getID());
        assertNull(taskManager.getTaskByID(task.getID()));
    }

    @Test
    void testDeleteEpicByID() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);
        taskManager.deleteEpicByID(epic.getID());
        assertNull(taskManager.getEpicByID(epic.getID()));
    }

    @Test
    void testDeleteSubtaskByID() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 1", epic.getID());
        taskManager.createSubtask(subtask);
        taskManager.deleteSubtaskByID(subtask.getID());
        assertNull(taskManager.getSubtaskByID(subtask.getID()));
    }

    @Test
    void testGetTaskByID() {
        Task task = new Task("Task 1", "Description 1");
        taskManager.createTask(task);
        assertEquals(task, taskManager.getTaskByID(task.getID()));
    }

    @Test
    void testGetEpicByID() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);
        assertEquals(epic, taskManager.getEpicByID(epic.getID()));
    }

    @Test
    void testGetSubtaskByID() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 1", epic.getID());
        taskManager.createSubtask(subtask);
        assertEquals(subtask, taskManager.getSubtaskByID(subtask.getID()));
    }

    @Test
    void testGetAllTasks() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void testGetAllEpics() {
        Epic epic1 = new Epic("Epic 1", "Description 1");
        Epic epic2 = new Epic("Epic 2", "Description 2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        List<Task> epics = taskManager.getAllEpics();
        assertEquals(2, epics.size());
        assertTrue(epics.contains(epic1));
        assertTrue(epics.contains(epic2));
    }

    @Test
    void testGetAllSubtasks() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic.getID());
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic.getID());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        List<Task> subtasks = taskManager.getAllSubtasks();
        assertEquals(2, subtasks.size());
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask2));
    }

    @Test
    void testGetPrioritizedTasks() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setStartTime(LocalDateTime.of(2023, 6, 10, 10, 0));
        task1.setDuration(Duration.ofHours(2));
        taskManager.createTask(task1);

        Task task2 = new Task("Task 2", "Description 2");
        task2.setStartTime(LocalDateTime.of(2023, 6, 10, 14, 0));
        task2.setDuration(Duration.ofHours(1));
        taskManager.createTask(task2);

        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic.getID());
        subtask1.setStartTime(LocalDateTime.of(2023, 6, 10, 12, 0));
        subtask1.setDuration(Duration.ofHours(1));
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic.getID());
        subtask2.setStartTime(LocalDateTime.of(2023, 6, 10, 13, 0));
        subtask2.setDuration(Duration.ofHours(1));
        taskManager.createSubtask(subtask2);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertEquals(4, prioritizedTasks.size());
        assertEquals(task1, prioritizedTasks.get(0));
        assertEquals(subtask1, prioritizedTasks.get(1));
        assertEquals(subtask2, prioritizedTasks.get(2));
        assertEquals(task2, prioritizedTasks.get(3));
    }

    @Test
    void testHasIntersections() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setStartTime(LocalDateTime.of(2023, 6, 10, 10, 0));
        task1.setDuration(Duration.ofHours(2));

        Task task2 = new Task("Task 2", "Description 2");
        task2.setStartTime(LocalDateTime.of(2023, 6, 10, 11, 0));
        task2.setDuration(Duration.ofHours(1));

        Task task3 = new Task("Task 3", "Description 3");
        task3.setStartTime(LocalDateTime.of(2023, 6, 10, 14, 0));
        task3.setDuration(Duration.ofHours(1));

        taskManager.createTask(task1);

        assertTrue(taskManager.hasIntersections(task2));
        assertFalse(taskManager.hasIntersections(task3));
    }

    @Test
    void testGetEpicTasks() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic.getID());
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic.getID());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        List<Subtask> subtasks = taskManager.getEpicTasks(epic.getID());
        assertEquals(2, subtasks.size());
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask2));
    }

    @Test
    void testUpdateTask() {
        Task task = new Task("Task 1", "Description 1");
        taskManager.createTask(task);
        task.setName("Updated Task");
        task.setDescription("Updated Description");
        taskManager.updateTask(task);
        assertEquals("Updated Task", taskManager.getTaskByID(task.getID()).getName());
        assertEquals("Updated Description", taskManager.getTaskByID(task.getID()).getDescription());
    }

    @Test
    void testUpdateEpic() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);
        epic.setName("Updated Epic");
        epic.setDescription("Updated Description");
        taskManager.updateEpics(epic);
        assertEquals("Updated Epic", taskManager.getEpicByID(epic.getID()).getName());
        assertEquals("Updated Description", taskManager.getEpicByID(epic.getID()).getDescription());
    }

    @Test
    void testUpdateSubtask() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 1", epic.getID());
        taskManager.createSubtask(subtask);
        subtask.setName("Updated Subtask");
        subtask.setDescription("Updated Description");
        taskManager.updateSubtasks(subtask);
        assertEquals("Updated Subtask", taskManager.getSubtaskByID(subtask.getID()).getName());
        assertEquals("Updated Description", taskManager.getSubtaskByID(subtask.getID()).getDescription());
    }

    @Test
    void testGetHistory() {
        Task task = new Task("Task 1", "Description 1");
        taskManager.createTask(task);
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 1", epic.getID());
        taskManager.createSubtask(subtask);

        taskManager.getTaskByID(task.getID());
        taskManager.getEpicByID(epic.getID());
        taskManager.getSubtaskByID(subtask.getID());

        List<Task> history = taskManager.getHistory();

        assertEquals(3, history.size());
        assertEquals(task, history.get(0));
        assertEquals(epic, history.get(1));
        assertEquals(subtask, history.get(2));
    }
}
