package tasks;

import tasks.enums.Status;
import tasks.enums.TaskType;

public class Subtask extends Task {
    private Integer epicID;

    public Integer getEpicID() {
        return epicID;
    }

    public void setEpic(Integer epicID) {
        this.epicID = epicID;
    }

    public Subtask(String name, String description, int ID, Status status, Integer epicID) {
        super(name, description, ID, status);
        this.epicID = epicID;
        this.taskType = TaskType.SUBTASK;
    }

    public Subtask(String name, String description, Integer epicID) {
        this(name, description, 0, Status.NEW, epicID);
    }

    @Override
    public String toString() {
        return"Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", ID=" + getID() +
                ", status=" + getStatus() +
                ", epicID=" + epicID;
    }
}
