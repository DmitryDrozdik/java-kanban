package managers.classes;

import managers.Managers;
import managers.interfaces.HistoryManager;
import managers.interfaces.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int nextTaskID = 1;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    @Override
    public void createTask(Task task) {
        int ID = nextTaskID++;
        task.setID(ID);
        tasks.put(ID, task);
    }

    @Override
    public void createEpic(Epic epic) {
        int ID = nextTaskID++;
        epic.setID(ID);
        epics.put(ID, epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        int ID = nextTaskID++;
        subtask.setID(ID);
        var epic = epics.get(subtask.getEpicID());
        epic.addSubtask(subtask.getID());
        subtasks.put(ID, subtask);
        updateStatuses(epic);
    }

    @Override
    public void deleteAllTasks() {
        tasks.keySet().forEach(historyManager::remove);
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.keySet().forEach(historyManager::remove);
        subtasks.keySet().forEach(historyManager::remove);
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.keySet().forEach(historyManager::remove);
        subtasks.clear();

        for (var epic : epics.values()) {
            for (var subtaskID : epic.getSubtaskList()) {
                epic.deleteSubtask(subtaskID);
            }
            updateStatuses(epic);
        }
    }

    @Override
    public void deleteTaskByID(int ID) {
        tasks.remove(ID);
        historyManager.remove(ID);
    }

    @Override
    public void deleteEpicByID(int ID) {
        var epic = epics.get(ID);
        for (var subtaskID : epic.getSubtaskList()) {
            subtasks.remove(subtaskID);
            historyManager.remove(subtaskID);
        }
        epics.remove(ID);
        historyManager.remove(ID);
    }

    @Override
    public void deleteSubtaskByID(int ID) {
        var epic = epics.get(subtasks.get(ID));
        subtasks.remove(ID);
        epic.deleteSubtask(ID);
        updateStatuses(epic);
        historyManager.remove(ID);
    }

    @Override
    public Task getTaskByID(int ID) {
        Task task = tasks.get(ID);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicByID(int ID) {
        Epic epic = epics.get(ID);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskByID(int ID) {
        Subtask subtask = subtasks.get(ID);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Task> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getEpicTasks(int ID) {
        List<Subtask> list = new ArrayList<>();

        var epic = epics.get(ID);

        for (var subtaskID : epic.getSubtaskList()) {
            list.add(subtasks.get(subtaskID));
        }
        return list;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.get(task.getID()) != null)
            tasks.put(task.getID(), task);
    }

    @Override
    public void updateEpics(Epic epic) {
        if (epics.get(epic.getID()) != null) {
            epics.put(epic.getID(), epic);
            updateStatuses(epic);
        }
    }

    @Override
    public void updateSubtasks(Subtask subtask) {
        if (subtasks.get(subtask.getID()) != null) {
            subtasks.put(subtask.getID(), subtask);
            var epic = epics.get(subtask.getEpicID());
            updateStatuses(epic);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
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
