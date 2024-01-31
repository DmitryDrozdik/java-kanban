package tasks;

import tasks.enums.Status;
import tasks.enums.TaskType;

public class Task {
    private String name;
    private String description;
    private int ID;
    private Status status;
    protected TaskType taskType;

    public Task(String name, String description, int ID, Status status) {
        this.name = name;
        this.description = description;
        this.ID = ID;
        this.status = status;
        this.taskType = TaskType.TASK;
    }

    public Task(String name, String description) {
        this(name, description, 0, Status.NEW);
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public Boolean isTaskType(TaskType taskType) {
        return this.taskType == taskType;
    }

    public Task(String name) {
        this(name, "default description", 0, Status.NEW);
    }

    public String getName() {
        return name;
    }

    public void setName (String name){
        this.name = name;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description){
        this.description = description;
    }

    public int getID () {
        return ID;
    }

    public void setID (int ID){
        this.ID = ID;
    }

    public Status getStatus () {
        return status;
    }

    public void setStatus (Status status){
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ID=" + ID +
                ", status=" + status +
                '}';
    }
}