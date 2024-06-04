package classes;

import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;
import httpserversandendpoints.DurationAdapter;
import httpserversandendpoints.HttpTaskServer;
import httpserversandendpoints.LocalDateTimeAdapter;
import managers.Managers;
import managers.interfaces.TaskManager;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static tasks.enums.Status.NEW;

public class HttpServerTests {
    HttpServer httpServer;
    TaskManager taskManager;
    static Gson gson;

    @BeforeAll
    public static void allInit() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    @BeforeEach
    public void init() throws IOException {
        taskManager = Managers.getDefaultManager();
        httpServer = HttpTaskServer.createServer(taskManager);
        httpServer.start();
    }

    @AfterEach
    public void end() {
        httpServer.stop(0);
    }

    @Test
    public void getTasksTest() throws IOException, InterruptedException {
        Task task = new Task("task1");
        Task task2 = new Task("task2");
        taskManager.createTask(task);
        taskManager.createTask(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        Assertions.assertEquals(2, jsonArray.size());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void putEpicTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "description", 1, NEW);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(url)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        Assertions.assertEquals(epic, taskManager.getEpicByID(1));
        Assertions.assertEquals(201, response.statusCode());
    }

    @Test
    public void deleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "description", 1, NEW);
        Subtask subtask = new Subtask("subtask1", "description", 1);
        Subtask subtask1 = new Subtask("subtask2", "description", 1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask1);
        epic.addSubtask(2);
        epic.addSubtask(3);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);

        Assertions.assertEquals(0, taskManager.getAllEpics().size());
        Assertions.assertEquals(0, taskManager.getAllSubtasks().size());
        Assertions.assertEquals(200, response.statusCode());

    }

    @Test
    public void getSubtaskByID() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "description", 1, NEW);
        Subtask subtask = new Subtask("subtask1", "description", 1);
        Subtask subtask1 = new Subtask("subtask2", "description", 1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask1);
        epic.addSubtask(2);
        epic.addSubtask(3);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);

        Assertions.assertEquals(200, response.statusCode());

    }

    @Test
    public void getSubtaskByIDError() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "description", 1, NEW);
        Subtask subtask = new Subtask("subtask1", "description", 1);
        Subtask subtask1 = new Subtask("subtask2", "description", 1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask1);
        epic.addSubtask(2);
        epic.addSubtask(3);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/4");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);

        Assertions.assertEquals(404, response.statusCode());

    }

    @Test
    public void createTaskError() throws IOException, InterruptedException {

        Task task = new Task("task1");
        Task task2 = new Task("task2");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofHours(1));
        task2.setStartTime(LocalDateTime.now().plusMinutes(15));
        task2.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2)))
                .uri(url)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);

        Assertions.assertEquals(406, response.statusCode());
    }
}