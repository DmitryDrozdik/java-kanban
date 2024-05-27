package HttpServersAndEndpoints;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.interfaces.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    protected TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String[] path = getPath(httpExchange);
        switch (method) {
            case "GET":
                if (path.length < 3) {
                    getTaskList(httpExchange);
                } else {
                    getTaskById(httpExchange, path[2]);
                }
                break;
            case "POST":
                putTask(httpExchange);
                break;
            case "DELETE":
                delTaskById(httpExchange, path[2]);
                break;
        }

    }

    private void getTaskList(HttpExchange exchange) throws IOException {

        List<Task> listTask = taskManager.getAllTasks();
//        список задач в формате Json
        String listTaskJson = gson.toJson(listTask);
        exchange.sendResponseHeaders(200, 0);

        sendResponse(exchange, listTaskJson);

    }

    private void getTaskById(HttpExchange exchange, String id) throws IOException {

        int ide = Integer.parseInt(id);

        Task taskId = taskManager.getTaskByID(ide);

        String taskIdJson = gson.toJson(taskId);
        exchange.sendResponseHeaders(200, 0);
        sendResponse(exchange, taskIdJson);

    }

    private void putTask(HttpExchange exchange) throws IOException {

        String request = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (request.isBlank()) {
            exchange.sendResponseHeaders(400, 0);
            sendResponse(exchange, "Пустые входные данные.");
            return;
        }

//        получаем объект задачи из Json - десериализуем
        Task task;
        try {
            task = gson.fromJson(request, Task.class);
        } catch (JsonSyntaxException e) {
            exchange.sendResponseHeaders(400, 0);
            sendResponse(exchange, "Неверный формат входных данных.");
            return;
        }

        if (taskManager.hasIntersections(task)) {
            exchange.sendResponseHeaders(406, 0);
            sendResponse(exchange, "Задача пересекается с существующими.");
        } else if (task.getID() != 0) {
            taskManager.updateTask(task);
            exchange.sendResponseHeaders(201, 0);
            sendResponse(exchange, "Задача обновлена.");
        } else {
            taskManager.createTask(task);
            exchange.sendResponseHeaders(201, 0);
            sendResponse(exchange, String.format("Задача создана."));
        }
    }

    private void delTaskById(HttpExchange exchange, String id) throws IOException {

        int ide;

        try {
            ide = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            exchange.sendResponseHeaders(400, 0);
            sendResponse(exchange, "Неверный формат. Id должен быть числом.");
            return;
        }

        taskManager.deleteTaskByID(ide);
        exchange.sendResponseHeaders(200, 0);
        sendResponse(exchange, String.format("Задача удалена"));

    }

    void sendResponse(HttpExchange exchange, String text) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(text.getBytes());
        }
    }

}