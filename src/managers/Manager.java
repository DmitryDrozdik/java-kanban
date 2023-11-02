package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    private int nextTaskID = 1;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    public void createTask(Task task) {
        int ID = nextTaskID++;
        task.setID(ID);
        tasks.put(ID, task);
    }

    public void createEpic(Epic epic) {
        int ID = nextTaskID++;
        epic.setID(ID);
        epics.put(ID, epic);
    }

    public void createSubtask(Subtask subtask) {
        int ID = nextTaskID++;
        subtask.setID(ID);
        var epic = epics.get(subtask.getEpicID());
        epic.addSubtask(subtask.getID());
        subtasks.put(ID, subtask);
        updateStatuses(epic);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();

        for (var epic : epics.values()) {
            for (var subtaskID : epic.getSubtaskList())
                epic.deleteSubtask(subtaskID);

            updateStatuses(epic);
        }
    }

    public void deleteTaskByID(int ID) {
        tasks.remove(ID);
    }

    public void deleteEpicByID(int ID) {
        var epic = epics.get(ID);
        for (var subtaskID : epic.getSubtaskList()) {
            subtasks.remove(subtaskID);
        }

        epics.remove(ID);
    }

    public void deleteSubtaskByID(int ID) {
        var epic = epics.get(subtasks.get(ID));
        subtasks.remove(ID);

        epic.deleteSubtask(ID);

        updateStatuses(epic);
    }

    public Task getTaskByID(int ID) {
        return tasks.get(ID);
    }

    public Epic getEpicByID(int ID) {
        return epics.get(ID);
    }

    public Subtask getSubtaskByID(int ID) {
        return subtasks.get(ID);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Task> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Task> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public List<Subtask> getEpicTasks(int ID) {
        List<Subtask> list = new ArrayList<Subtask>();

        var epic = epics.get(ID);
        for (var subtaskID : epic.getSubtaskList()) {
            list.add(subtasks.get(subtaskID));
        }

        return list;
    }

    public void updateTask(Task task) {
        if (tasks.get(task.getID()) != null)
            tasks.put(task.getID(), task);
        else
            createTask(task);
    }

    public void updateEpics(Epic epic) {
        if (epics.get(epic.getID()) != null)
            epics.put(epic.getID(), epic);
        else
            createEpic(epic);
    }

    public void updateSubtasks(Subtask subtask) {
        if (subtasks.get(subtask.getID()) != null)
            subtasks.put(subtask.getID(), subtask);
        else
            createSubtask(subtask);
    }

    private void updateStatuses(Epic epic) {
        boolean allNew = true;
        boolean allDone = true;

        for (Integer subtaskID : epic.getSubtaskList()) {
            var subtask = subtasks.get(subtaskID);

            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        }
        else if (allDone) {
            epic.setStatus(Status.DONE);
        }
        else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
