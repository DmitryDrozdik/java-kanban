public class Subtask extends Task {
    private Epic epic;

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    public Subtask(String name, String description, int ID, Status status, Epic epic) {
        super(name, description, ID, status);
        this.epic = epic;
    }

    public Subtask(String name, String description, Epic epic) {
        this(name, description, 0, Status.NEW, epic);
    }

    @Override
    public String toString() {
        return"Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", ID=" + getID() +
                ", status=" + getStatus() +
                ", epic=" + getEpic().getName();
    }
}
