
public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("Task1", "Description1");
        Task task2 = new Task("Task2", "Description2");

        Epic epic1 = new Epic("Epic1", "Epic description1");
        Subtask subtask1 = new Subtask("Subtask1", "Description subtask1", epic1);
        Subtask subtask2 = new Subtask("Subtask2", "Description subtask2", epic1);
        epic1.addSubtask(subtask1);
        epic1.addSubtask(subtask2);

        Epic epic2 = new Epic("Epic2", "Epic description2");
        Subtask subtask3 = new Subtask("Subtask3", "Description subtask3", epic2);
        epic2.addSubtask(subtask3);

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(epic1);
        manager.createTask(epic2);
        manager.createTask(subtask1);
        manager.createTask(subtask2);
        manager.createTask(subtask3);

        System.out.println("Default values:");
        System.out.println("All tasks: " + manager.getAllTasks());
        System.out.println("Subtasks for Epic 1: " + manager.getEpicTasks(epic1));
        System.out.println("Subtasks for Epic 2: " + manager.getEpicTasks(epic2));

        task1.setStatus(Status.DONE);
        subtask2.setStatus(Status.IN_PROGRESS);

        System.out.println("Updated values:");
        System.out.println("All tasks: " + manager.getAllTasks());
        System.out.println("Subtasks for Epic 1: " + manager.getEpicTasks(epic1));
        System.out.println("Subtasks for Epic 2: " + manager.getEpicTasks(epic2));

        manager.deleteByID(task2.getID());
        manager.deleteByID(epic1.getID());

        System.out.println("Deleted values:");
        System.out.println("All tasks: " + manager.getAllTasks());
        System.out.println("Subtasks for Epic 1: " + manager.getEpicTasks(epic1));
        System.out.println("Subtasks for Epic 2: " + manager.getEpicTasks(epic2));
    }
}