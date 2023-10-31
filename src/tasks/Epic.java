package tasks;

import tasks.enums.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtaskListID;

    public Epic(String name, String description, int ID, Status status) {
        super(name, description, ID, status);
        this.subtaskListID = new ArrayList<>();
    }

    public Epic(String name, String description) {
        this(name, description, 0, Status.NEW);
    }

    public List<Integer> getSubtaskList() {
        return subtaskListID;
    }

    public void addSubtask(int subtaskID) {
        this.subtaskListID.add(subtaskID);
    }

    public void deleteSubtask(int subtaskID) {
        this.subtaskListID.remove(subtaskID);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", ID=" + getID() +
                ", status=" + getStatus() +
                ", subtasks ids=[ ");

        for (var subtask : getSubtaskList()) {
            ret.append(subtask).append(" ");
        }

        ret.append("]}");
        return ret.toString();
    }
}
