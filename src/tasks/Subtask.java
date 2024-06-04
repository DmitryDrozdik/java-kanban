package tasks;

import tasks.enums.Status;
import tasks.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Integer epicID;

    public Integer getEpicID() {
        return epicID;
    }

    public void setEpic(Integer epicID) {
        this.epicID = epicID;
    }

    public Subtask(String name, String description, int id, Status status, Integer epicID, Duration duration, LocalDateTime localDateTime) {
        this(name, description, id, status, epicID, duration);
        this.startTime = localDateTime;
    }

    public Subtask(String name, String description, int id, Status status, Integer epicID, Duration duration) {
        this(name, description, id, status, epicID);
        this.duration = duration;
        this.startTime = null;
    }

    public Subtask(String name, String description, int id, Status status, Integer epicID, LocalDateTime startTime) {
        this(name, description, id, status, epicID);
        this.duration = null;
        this.startTime = startTime;
    }

    public Subtask(String name, String description, int id, Status status, Integer epicID) {
        super(name, description, id, status);
        this.epicID = epicID;
        this.taskType = TaskType.SUBTASK;
        this.duration = null;
        this.startTime = null;
    }

    public Subtask(String name, String description, Integer epicID) {
        this(name, description, 0, Status.NEW, epicID);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", ID=" + getID() +
                ", status=" + getStatus() +
                ", epicID=" + epicID;
    }
}
