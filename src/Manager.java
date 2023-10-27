import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    private int nextTaskID = 1;
    private Map<Integer, Task> allTasks = new HashMap<>();

    public void createTask(Task task) {
        int ID = nextTaskID++;
        task.setID(ID);
        allTasks.put(ID, task);
    }

    public void createSubtask(Task subtask, Task epic) {
        int ID = nextTaskID++;
        subtask.setID(ID);
        allTasks.put(ID, subtask);
        ((Epic)epic).addSubtask((Subtask)subtask);
    }

    public void deleteAllTasks() {
        allTasks.clear();
    }

    public void deleteByID(int ID) {
        allTasks.remove(ID);
    }

    public Task getByID(int ID) {
        return allTasks.get(ID);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(allTasks.values());
    }

    public List<Subtask> getEpicTasks(int ID) {
        Task task = allTasks.get(ID);
        if (task instanceof Epic) {
            return ((Epic) task).getSubtaskList();
        }
        else {
            return  null;
        }
    }

    public List<Subtask> getEpicTasks(Epic epic) {
        return epic.getSubtaskList();
    }

    public void updateTask(Task task) {
        allTasks.put(task.getID(), task);
    }

    public void updateStatuses() {
        for (Task task : allTasks.values()) {
            if (task instanceof Epic) {
                boolean allNew = true;
                boolean allDone = true;

                for (Subtask subtask : ((Epic) task).getSubtaskList()) {
                    if (subtask.getStatus() != Status.NEW) {
                        allNew = false;
                    }
                    if (subtask.getStatus() != Status.DONE) {
                        allDone = false;
                    }
                }

                if (allNew) {
                    task.setStatus(Status.NEW);
                }
                else if (allDone) {
                    task.setStatus(Status.DONE);
                }
                else {
                    task.setStatus(Status.IN_PROGRESS);
                }
            }
        }
    }
}
