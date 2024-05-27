package HttpServersAndEndpoints;

import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.interfaces.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static void main(String[] args) throws IOException {

        TaskManager taskManager = Managers.getDefaultManager();

        try {
            HttpServer httpServer = createServer(taskManager);
            httpServer.start();
            System.out.println("Start 8080");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static HttpServer createServer(TaskManager taskManager) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager)); // связываем путь и обработчик
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        return httpServer;
    }
}
