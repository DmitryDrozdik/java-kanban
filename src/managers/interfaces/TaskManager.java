package managers.interfaces;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    void deleteTaskByID(int ID);

    void deleteEpicByID(int ID);

    void deleteSubtaskByID(int ID);

    Task getTaskByID(int ID);

    Epic getEpicByID(int ID);

    Subtask getSubtaskByID(int ID);

    List<Task> getAllTasks();

    List<Task> getAllEpics();

    List<Task> getAllSubtasks();

    List<Subtask> getEpicTasks(int ID);

    void updateTask(Task task);

    void updateEpics(Epic epic);

    void updateSubtasks(Subtask subtask);

    List<Task> getHistory();
}
