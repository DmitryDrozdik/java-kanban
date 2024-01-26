package managers.classes;

import managers.interfaces.HistoryManager;
import managers.interfaces.TaskManager;

import exceptions.ManagerSaveException;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;
import tasks.enums.TaskType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    public static void main(String[] args) {
        FileBackedTasksManager manager = new FileBackedTasksManager();
        Task task1 = new Task("Task1", "Desc Task1", 1, Status.NEW);
        Epic epic1 = new Epic("Epic1", "Desc Epic1", 2, Status.NEW);
        Subtask sub1 = new Subtask("Subtask1", "Desc Sub1", 3, Status.NEW, epic1.getID());
        Subtask sub2 = new Subtask("Subtask2", "Desc Sub2", 4, Status.NEW, epic1.getID());

        manager.createTask(task1);
        manager.createEpic(epic1);
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);

        manager.getTaskByID(task1.getID());
        manager.getEpicByID(epic1.getID());
        manager.getSubtaskByID(sub2.getID());

        FileBackedTasksManager loadedManager = FileBackedTasksManager.load();

        System.out.println("Восстановленные задачи: " + loadedManager.getAllTasks());
        System.out.println("Восстановленные эпики: " + loadedManager.getAllEpics());
        System.out.println("Восстановленные подзадачи: " + loadedManager.getAllSubtasks());
        System.out.println("Восстановленная история: " + loadedManager.getHistory());
    }
    private static final String filePath = "./buffer.csv";

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteTaskByID(int ID) {
        super.deleteTaskByID(ID);
        save();
    }

    @Override
    public void deleteEpicByID(int ID) {
        super.deleteEpicByID(ID);
        save();
    }

    @Override
    public void deleteSubtaskByID(int ID) {
        super.deleteSubtaskByID(ID);
        save();
    }

    @Override
    public Task getTaskByID(int ID) {
        var res = super.getEpicByID(ID);
        save();

        return res;
    }

    @Override
    public Epic getEpicByID(int ID) {
        var res = super.getEpicByID(ID);
        save();

        return res;
    }

    @Override
    public Subtask getSubtaskByID(int ID) {
        var res = super.getSubtaskByID(ID);
        save();

        return res;
    }

    @Override
    public List<Task> getAllTasks() {
        var res = super.getAllTasks();
        save();

        return res;
    }

    @Override
    public List<Task> getAllEpics() {
        var res = super.getAllEpics();
        save();

        return res;
    }

    @Override
    public List<Task> getAllSubtasks() {
        var res = super.getAllSubtasks();
        save();

        return res;
    }

    @Override
    public List<Subtask> getEpicTasks(int ID) {
        var res = super.getEpicTasks(ID);
        save();

        return res;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpics(Epic epic) {
        super.updateEpics(epic);
        save();
    }

    @Override
    public void updateSubtasks(Subtask subtask) {
        super.updateSubtasks(subtask);
        save();
    }

    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id,type,name,status,description,epic" + "\n");

            for (Task task : super.getAllTasks()) {
                writer.write(taskToString(task));
                writer.newLine();
            }
            for (Task epic : super.getAllEpics()) {
                writer.write(taskToString(epic));
                writer.newLine();
            }
            for (Task subtask : super.getAllSubtasks()) {
                writer.write(taskToString(subtask));
                writer.newLine();
            }

            writer.newLine();

            writer.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла", e);
        }
    }

    private static Task taskFromString(String value) {
        String[] parts = value.split(",");
        int ID = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        Task task = null;

        switch (type) {
            case TASK:
                task = new Task(name, description, ID, status);
                break;
            case SUBTASK:
                int epicID = Integer.parseInt(parts[5]);
                task = new Subtask(name, description, ID, status, epicID);
                break;
            case EPIC:
                task = new Epic(name, description, ID, status);
                break;
        }
        return task;
    }

    private String historyToString(HistoryManager historyManager) {
        List<String> ids = new ArrayList<>();
        for (Task task : historyManager.getHistory()) {
            ids.add(String.valueOf(task.getID()));
        }
        return String.join(",", ids);
    }

    private String taskToString(Task task) {
        TaskType type = task instanceof Epic ? TaskType.EPIC :
                task instanceof Subtask ? TaskType.SUBTASK : TaskType.TASK;
        String epicID = task instanceof Subtask ? String.valueOf(((Subtask) task).getEpicID()) : "";
        return String.join(",",
                String.valueOf(task.getID()),
                type.toString(),
                task.getName(),
                task.getStatus().toString(),
                task.getDescription(),
                epicID);
    }

    public static FileBackedTasksManager load() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                Task task = taskFromString(line);
                fileBackedTasksManager.addTask(task);
            }

            String historyLine = reader.readLine();
            if (historyLine != null && !historyLine.isEmpty()) {
                List<Integer> historyIds = historyFromString(historyLine);
                fileBackedTasksManager.restoreHistory(historyIds);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки файла", e);
        }
        return fileBackedTasksManager;
    }

    private void addTask(Task task) {
        if (task instanceof Epic) {
            createEpic((Epic) task);
        } else if (task instanceof  Subtask) {
            createSubtask((Subtask) task);
        } else {
            createTask(task);
        }
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> ids = new ArrayList<>();
        for (String id : value.split(",")) {
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }

    private void restoreHistory(List<Integer> historyIds) {
        for (Integer id : historyIds) {
            Task task = getAnyTask(id);
            if (task != null) {
                historyManager.add(task);
            }
        }
    }

    private Task getAnyTask(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        }
        return null;
    }

}
