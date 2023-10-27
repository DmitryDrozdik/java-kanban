import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtaskList;
    public Epic(String name, String description, int ID, Status status) {
        super(name, description, ID, status);
        this.subtaskList = new ArrayList<>();
    }

    public Epic(String name, String description) {
        this(name, description, 0, Status.NEW);
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void addSubtask(Subtask subtask) {
        this.subtaskList.add(subtask);
        subtask.setEpic(this);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", ID=" + getID() +
                ", status=" + getStatus() +
                ", subtasks names=[ ");

        for (var subtask : getSubtaskList()) {
            ret.append(subtask.getName() + ' ');
        }
        ret.append("]}");
        return ret.toString();
    }
}
