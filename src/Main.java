import managers.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;

public class Main {
    public static void main(String[] args) {
        var manager = Managers.getDefaultManager();

        // Create Tasks
        Task task1 = new Task("Task1", "This is task 1");
        Task task2 = new Task("Task2", "This is task 2");
        Task task3 = new Task("Task3", "This is task 3");
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);

        // Create Epic and Subtasks
        Epic epic = new Epic("Epic1", "This is epic 1");
        manager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask1", "This is subtask 1", epic.getID());
        Subtask subtask2 = new Subtask("Subtask2", "This is subtask 2", epic.getID());
        Subtask subtask3 = new Subtask("Subtask3", "This is subtask 3", epic.getID());
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        // Link subtasks to the epic
        epic.addSubtask(subtask1.getID());
        epic.addSubtask(subtask2.getID());
        manager.updateEpics(epic);

        // Verify initial conditions
        System.out.println("Initial Tasks: " + manager.getAllTasks());
        System.out.println("Initial Epics: " + manager.getAllEpics());
        System.out.println("Initial Subtasks: " + manager.getAllSubtasks());

        // Update task
        task1.setName("UpdatedTask1");
        task1.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task1);

        System.out.println("Check history:");
        manager.getTaskByID(task1.getID());
        System.out.println(manager.getHistory());
        manager.getEpicByID(epic.getID());
        System.out.println(manager.getHistory());
        manager.getSubtaskByID(subtask3.getID());
        System.out.println(manager.getHistory());


        // Delete task by ID
        manager.deleteTaskByID(task2.getID());

        // Delete epic by ID (Should also delete subtasks)
        manager.deleteEpicByID(epic.getID());

        // Verify final conditions
        System.out.println("Final Tasks: " + manager.getAllTasks());
        System.out.println("Final Epics: " + manager.getAllEpics());
        System.out.println("Final Subtasks: " + manager.getAllSubtasks());
    }
}