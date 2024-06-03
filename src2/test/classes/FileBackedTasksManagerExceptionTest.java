package test.classes;

import src.exceptions.ManagerLoadException;
import src.exceptions.ManagerSaveException;
import src.managers.classes.FileBackedTasksManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import src.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerExceptionTest {
    @TempDir
    File tempDir;

    @Test
    void testLoadFromNonExistentFile() {
        File nonExistentFile = new File(tempDir, "nonexistent.csv");
        assertThrows(ManagerLoadException.class, () -> FileBackedTasksManager.load(nonExistentFile),
                "Должно выбрасываться исключение при загрузке несуществующего файла");
    }

    @Test
    void testSaveToInaccessibleFile() throws IOException {
        File inaccessibleFile = tempDir.toPath().resolve("inaccessible.csv").toFile();
        Files.createFile(inaccessibleFile.toPath());
        inaccessibleFile.setWritable(false);

        FileBackedTasksManager taskManager = new FileBackedTasksManager(inaccessibleFile);
        Task task = new Task("Task 1", "Description 1");

        assertThrows(ManagerSaveException.class, () -> {
            taskManager.createTask(task);
        }, "Должно выбрасываться исключение при сохранении в недоступный файл");
    }

    @Test
    void testLoadAndSave() {
        File file = new File(tempDir, "src1.tasks.csv");
        FileBackedTasksManager taskManager = new FileBackedTasksManager(file);
        Task task = new Task("Task 1", "Description 1");
        taskManager.createTask(task);

        assertDoesNotThrow(taskManager::save, "Должна выполняться корректная загрузка и сохранение данных");

        FileBackedTasksManager loadedTaskManager = FileBackedTasksManager.load(file);
        assertEquals(1, loadedTaskManager.getAllTasks().size(), "Загруженный менеджер должен содержать ожидаемое количество задач");
        assertEquals(task, loadedTaskManager.getAllTasks().get(0), "Загруженный менеджер должен содержать ожидаемую задачу");
    }
}
