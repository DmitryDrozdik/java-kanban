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
    private Map<Integer, Task> Tasks = new HashMap<>();
    private Map<Integer, Epic> Epics = new HashMap<>();
    private Map<Integer, Subtask> Subtasks = new HashMap<>();

    public void createTask(Task task) {
        int ID = nextTaskID++;
        task.setID(ID);
        Tasks.put(ID, task);
    }

    public void createEpic(Epic epic) {
        int ID = nextTaskID++;
        epic.setID(ID);
        Epics.put(ID, epic);
    }

    public void createSubtask(Subtask subtask) {
        int ID = nextTaskID++;
        subtask.setID(ID);
        updateStatuses();
        Subtasks.put(ID, subtask);
    }

    public void deleteAllTasks() {
        Tasks.clear();
    }

    public void deleteAllEpics() {
        Epics.clear();
    }

    public void deleteAllSubtasks() {
        Subtasks.clear();
    }

    public void deleteAll() {
        deleteAllTasks();
        deleteAllEpics();
        deleteAllSubtasks();
    }

    public void deleteTaskByID(int ID) {
        Tasks.remove(ID);
    }

    public void deleteEpicByID(int ID) {
        for (var subtaskID : Epics.get(ID).getSubtaskList()) {
            Subtasks.remove(subtaskID);
        }

        Epics.remove(ID);
    }

    public void deleteSubtaskByID(int ID) {
        Subtasks.remove(ID);

        for (var epic : Epics.values()) {
            epic.deleteSubtask(ID);
        }

        updateStatuses();
    }

    public Task getTaskByID(int ID) {
        return Tasks.get(ID);
    }

    public Epic getEpicByID(int ID) {
        return Epics.get(ID);
    }

    public Subtask getSubtaskByID(int ID) {
        return Subtasks.get(ID);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(Tasks.values());
    }

    public List<Task> getAllEpics() {
        return new ArrayList<>(Epics.values());
    }

    public List<Task> getAllSubtasks() {
        return new ArrayList<>(Subtasks.values());
    }

    public List<Integer> getEpicTasks(int ID) {
        return Epics.get(ID).getSubtaskList();
    }

    public void updateTask(Task task) {
        Tasks.put(task.getID(), task);
    }

    public void updateEpics(Epic epic) {
        Epics.put(epic.getID(), epic);
    }

    public void updateSubtasks(Subtask subtask) {
        Subtasks.put(subtask.getID(), subtask);
        updateStatuses();
    }

    private void updateStatuses() {
        for (Epic epic : Epics.values()) {
            boolean allNew = true;
            boolean allDone = true;

            for (Integer subtaskID : epic.getSubtaskList()) {
                var subtask = Subtasks.get(subtaskID);

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
}
