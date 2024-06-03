package src.managers.interfaces;

import src.tasks.Epic;
import src.tasks.Subtask;
import src.tasks.Task;

import java.util.List;

public interface TaskManager {

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    void deleteTaskByID(int id);

    void deleteEpicByID(int id);

    void deleteSubtaskByID(int id);

    Task getTaskByID(int id);

    Epic getEpicByID(int id);

    Subtask getSubtaskByID(int id);

    List<Task> getAllTasks();

    List<Task> getAllEpics();

    List<Task> getAllSubtasks();

    List<Task> getPrioritizedTasks();

    boolean hasIntersections(Task task);

    List<Subtask> getEpicTasks(int id);

    void updateTask(Task task);

    void updateEpics(Epic epic);

    void updateSubtasks(Subtask subtask);

    List<Task> getHistory();
}
