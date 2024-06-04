package tasks;

import tasks.enums.Status;
import tasks.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtaskListID;
    private LocalDateTime endTime;

    public Epic(String name, String description, int id, Status status) {
        super(name, description, id, status);
        this.subtaskListID = new ArrayList<>();
        this.taskType = TaskType.EPIC;
    }

    public void setEndTime(LocalDateTime dt) {
        this.endTime = dt;
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public Epic(String name, String description) {
        this(name, description, 0, Status.NEW);
    }

    public void calculateDurationAndStartEndTime(List<Subtask> subtasks) {
        duration = Duration.ZERO;
        startTime = null;
        endTime = null;

        for (Subtask subtask : subtasks) {
            Duration subtaskDuration = subtask.getDuration();
            if (subtaskDuration != null) {
                duration = duration.plus(subtaskDuration);
            }

            LocalDateTime subtaskStartTime = subtask.getStartTime();
            if (subtaskStartTime != null) {
                if (startTime == null || subtaskStartTime.isBefore(startTime)) {
                    startTime = subtaskStartTime;
                }
            }

            LocalDateTime subtaskEndTime = subtask.getEndTime();
            if (subtaskEndTime != null) {
                if (endTime == null || subtaskEndTime.isAfter(endTime)) {
                    endTime = subtaskEndTime;
                }
            }
        }
    }

    public List<Integer> getSubtaskList() {
        return subtaskListID;
    }

    public void addSubtask(int subtaskID) {
        this.subtaskListID.add(subtaskID);
    }

    public void deleteSubtask(Integer subtaskID) {
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
