package tasks;

import com.google.gson.annotations.Expose;
import tasks.enums.Status;
import tasks.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int ID;
    private Status status;
    protected TaskType taskType;

    protected Duration duration;
    protected LocalDateTime startTime;

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null)
        {
            return null;
        }

        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return ID == task.ID &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                status == task.status &&
                taskType == task.taskType &&
                Objects.equals(duration, task.duration) &&
                Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, ID, status, taskType, duration, startTime);
    }

    public Task(String name, String description, int ID, Status status, Duration duration, LocalDateTime localDateTime) {
        this(name, description, ID, status, duration);
        this.startTime = localDateTime;
    }

    public Task(String name, String description, int ID, Status status, Duration duration) {
        this(name, description, ID, status);
        this.duration = duration;
        this.startTime = null;
    }

    public Task(String name, String description, int ID, Status status, LocalDateTime startTime) {
        this(name, description, ID, status);
        this.duration = null;
        this.startTime = startTime;
    }

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

    public boolean intersects(Task other) {
        if (this.getStartTime() == null || this.getEndTime() == null ||
                other.getStartTime() == null || other.getEndTime() == null)
        {
            return false;
        }

        return this.getStartTime().isBefore(other.getEndTime()) && this.getEndTime().isAfter(other.getStartTime());
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