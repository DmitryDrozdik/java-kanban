package managers.classes;

import managers.Managers;
import managers.interfaces.HistoryManager;
import managers.interfaces.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private int nextTaskID = 1;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
    );

    @Override
    public void createTask(Task task) {
        if (hasIntersections(task)) {
            throw new IllegalArgumentException("Задача пересекается с существующими задачами");
        }

        int ID = nextTaskID++;
        task.setID(ID);
        tasks.put(ID, task);
        if (task.getStartTime() != null)
        {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        int ID = nextTaskID++;
        epic.setID(ID);
        epics.put(ID, epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (hasIntersections(subtask)) {
            throw new IllegalArgumentException("Подзадача пересекается с существующими задачами");
        }
        int ID = nextTaskID++;
        subtask.setID(ID);
        var epic = epics.get(subtask.getEpicID());
        epic.addSubtask(subtask.getID());
        subtasks.put(ID, subtask);
        if (subtask.getStartTime() != null)
        {
            prioritizedTasks.add(subtask);
        }
        updateStatuses(epic);
    }

    @Override
    public void deleteAllTasks() {
        tasks.values().forEach(task -> {
            historyManager.remove(task.getID());
            prioritizedTasks.remove(task);
        });
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.values().forEach(epic -> {
            historyManager.remove(epic.getID());
            epic.getSubtaskList().forEach(subtaskID -> {
                historyManager.remove(subtaskID);
                prioritizedTasks.remove(subtasks.get(subtaskID));
            });
        });
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.values().forEach(subtask -> {
            historyManager.remove(subtask.getID());
            prioritizedTasks.remove(subtask);
            var epic = epics.get(subtask.getEpicID());
            epic.deleteSubtask(subtask.getID());
            updateStatuses(epic);
        });
        subtasks.clear();
    }

    @Override
    public void deleteTaskByID(int ID) {
        var task = tasks.get(ID);
        if (task != null) {
            prioritizedTasks.remove(task);
            tasks.remove(ID);
            historyManager.remove(ID);
        }
    }

    @Override
    public void deleteEpicByID(int ID) {
        var epic = epics.get(ID);

        if (epic != null) {
            epic.getSubtaskList().forEach(subtaskID -> {
                prioritizedTasks.remove(subtasks.get(subtaskID));
                subtasks.remove(subtaskID);
                historyManager.remove(subtaskID);
            });
            historyManager.remove(ID);
            epics.remove(ID);
        }
    }

    @Override
    public void deleteSubtaskByID(int ID) {
        var subtask = subtasks.get(ID);
        if (subtask != null) {
            var epic = epics.get(subtask.getEpicID());
            subtasks.remove(ID);
            epic.deleteSubtask(ID);
            updateStatuses(epic);
            historyManager.remove(ID);
            prioritizedTasks.remove(subtask);
        }
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
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public boolean hasIntersections(Task task) {
        if (task.getStartTime() == null || task.getEndTime() == null)
        {
            return false;
        }
        return prioritizedTasks.stream()
                .anyMatch(t -> t.intersects(task));
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
        var oldTask = tasks.get(task.getID());
        if (oldTask != null && task.getStartTime() != null)
        {
            prioritizedTasks.remove(oldTask);
            tasks.put(task.getID(), task);
            prioritizedTasks.add(task);
        }
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
        var oldSubtask = subtasks.get(subtask.getID());
        if (oldSubtask != null && subtask.getStartTime() != null)
        {
            prioritizedTasks.remove(oldSubtask);
            subtasks.put(subtask.getID(), subtask);
            prioritizedTasks.add(subtask);
            var epic = epics.get(subtask.getEpicID());
            updateStatuses(epic);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateStatuses(Epic epic) {
        var subtaskList = epic.getSubtaskList();
        if (subtaskList.isEmpty())
        {
            epic.setStatus(Status.NEW);
            return;
        }

        var hasNew = false;
        var hasInProgress = false;
        var hasDone = false;

        for (Integer subtaskID : subtaskList) {
            var subtask = subtasks.get(subtaskID);
            if (subtask != null) {
                switch (subtask.getStatus()) {
                    case NEW:
                        hasNew = true;
                        break;
                    case IN_PROGRESS:
                        hasInProgress = true;
                        break;
                    case DONE:
                        hasDone = true;
                        break;
                }
            }
        }

        if (hasInProgress || (hasNew && hasDone))
        {
            epic.setStatus(Status.IN_PROGRESS);
        }
        else if(hasDone)
        {
            epic.setStatus(Status.DONE);
        }
        else
        {
            epic.setStatus(Status.NEW);
        }

        epic.calculateDurationAndStartEndTime(
                subtaskList.stream()
                        .map(subtasks::get)
                        .collect(Collectors.toList())
        );
    }
}
