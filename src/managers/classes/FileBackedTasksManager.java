package managers.classes;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import managers.interfaces.HistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;
import tasks.enums.TaskType;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;
    private static final String HEADER = "id,type,name,status,description,epic,duration,startTime" + System.lineSeparator();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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

        FileBackedTasksManager loadedManager = FileBackedTasksManager.load(new File("./resources/buffer.csv"));

        System.out.println("Восстановленные задачи: " + loadedManager.getAllTasks());
        System.out.println("Восстановленные эпики: " + loadedManager.getAllEpics());
        System.out.println("Восстановленные подзадачи: " + loadedManager.getAllSubtasks());
        System.out.println("Восстановленная история: " + loadedManager.getHistory());
    }

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public FileBackedTasksManager() {
        this(new File("./buffer.csv"));
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(HEADER);

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
        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        Duration duration = parts[6].equals("null") ? null : Duration.ofMinutes(Long.parseLong(parts[6]));
        LocalDateTime startTime = parts[7].equals("null") ? null : LocalDateTime.parse(parts[7], formatter);

        switch (type) {
            case TASK:
                return new Task(name, description, id, status, duration, startTime);
            case SUBTASK:
                int epicID = Integer.parseInt(parts[5]);
                return new Subtask(name, description, id, status, epicID, duration, startTime);
            case EPIC:
                return new Epic(name, description, id, status);
        }
        return null;
    }

    private String historyToString(HistoryManager historyManager) {
        List<String> ids = new ArrayList<>();
        for (Task task : historyManager.getHistory()) {
            ids.add(String.valueOf(task.getID()));
        }
        return String.join(",", ids);
    }

    private String taskToString(Task task) {
        String epicID = task.isTaskType(TaskType.SUBTASK) ? String.valueOf(((Subtask) task).getEpicID()) : "";
        String duration = task.getDuration() != null ? String.valueOf(task.getDuration().toMinutes()) : "null";
        String startTime = task.getStartTime() != null ? task.getStartTime().format(formatter) : "null";
        return String.join(",",
                String.valueOf(task.getID()),
                task.getTaskType().toString(),
                task.getName(),
                task.getStatus().toString(),
                task.getDescription(),
                epicID,
                duration,
                startTime);
    }

    public static FileBackedTasksManager load(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
            throw new ManagerLoadException("Ошибка загрузки файла", e);
        }
        return fileBackedTasksManager;
    }

    private void addTask(Task task) {
        if (task.isTaskType(TaskType.EPIC)) {
            createEpic((Epic) task);
        } else if (task.isTaskType(TaskType.SUBTASK)) {
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
    public void deleteTaskByID(int id) {
        super.deleteTaskByID(id);
        save();
    }

    @Override
    public void deleteEpicByID(int id) {
        super.deleteEpicByID(id);
        save();
    }

    @Override
    public void deleteSubtaskByID(int id) {
        super.deleteSubtaskByID(id);
        save();
    }

    @Override
    public Task getTaskByID(int id) {
        var res = super.getTaskByID(id);
        save();

        return res;
    }

    @Override
    public Epic getEpicByID(int id) {
        var res = super.getEpicByID(id);
        save();

        return res;
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        var res = super.getSubtaskByID(id);
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
    public List<Subtask> getEpicTasks(int id) {
        var res = super.getEpicTasks(id);
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
}
