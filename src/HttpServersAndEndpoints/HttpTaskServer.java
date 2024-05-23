package HttpServersAndEndpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;

import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.OutputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import managers.Managers;
import managers.interfaces.TaskManager;
import tasks.Task;
import tasks.enums.Status;

import static tasks.enums.Status.NEW;
import static tasks.enums.TaskType.TASK;

public class HttpTaskServer {
    public static void main(String[] args) throws IOException {

        TaskManager taskManager = Managers.getDefaultManager();
        taskManager.createTask(new Task("первая задача"));

        Task task = taskManager.getTaskByID(1);
        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);
      //  taskManager.createTask(new Task("вторая задача"));
       // taskManager.createTask(new Task("третья задача"));

        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
            httpServer.createContext("/tasks", new TaskHandler(taskManager)); // связываем путь и обработчик
//            httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
//            httpServer.createContext("/epics", new EpicHandler(taskManager));
//            httpServer.createContext("/history", new HistoryHandler(taskManager));
//            httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
            httpServer.start();
            System.out.println("Start 8080");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
