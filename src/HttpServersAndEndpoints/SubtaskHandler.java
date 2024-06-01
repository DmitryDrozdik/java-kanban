package HttpServersAndEndpoints;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.interfaces.TaskManager;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Methods;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static tasks.enums.Methods.*;


public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    protected SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String[] path = getPath(httpExchange);

        if (Methods.valueOf(method) == GET) {
            if (path.length < 3) {
                getSubtaskList(httpExchange);
            } else {
                getSubtaskById(httpExchange, path[2]);
            }
        } else if (Methods.valueOf(method) == POST) {
            putSubtask(httpExchange);
        } else if (Methods.valueOf(method) == DELETE) {
            delSubtaskById(httpExchange, path[2]);
        }
    }

    private void getSubtaskList(HttpExchange exchange) throws IOException {

        List<Task> listSubtask = taskManager.getAllSubtasks();
//        список задач в формате Json
        String listSubtaskJson = gson.toJson(listSubtask);
        exchange.sendResponseHeaders(200, 0);

        sendResponse(exchange, listSubtaskJson);

    }

    private void getSubtaskById(HttpExchange exchange, String id) throws IOException {
        int ide = -1;
        try {
            ide = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            exchange.sendResponseHeaders(400, 0);
            sendResponse(exchange, "Неверный формат. Id должен быть числом.");
            return;
        }

        Task subtaskId = taskManager.getSubtaskByID(ide);
        subtaskId = taskManager.getSubtaskByID(ide);
        if (subtaskId == null) {
            exchange.sendResponseHeaders(404, 0);
            sendResponse(exchange, "Подзадача не найдена.");
            return;
        }

        String subtaskIdJson = gson.toJson(subtaskId);
        exchange.sendResponseHeaders(200, 0);
        sendResponse(exchange, subtaskIdJson);
    }

    private void putSubtask(HttpExchange exchange) throws IOException {

        String request = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (request.isBlank()) {
            exchange.sendResponseHeaders(400, 0);
            sendResponse(exchange, "Пустые входные данные");
            return;
        }

//        получаем объект задачи из Json - десериализуем
        Subtask subtask;
        try {
            subtask = gson.fromJson(request, Subtask.class);
        } catch (JsonSyntaxException e) {
            exchange.sendResponseHeaders(400, 0);
            sendResponse(exchange, "Неверный формат входных данных");
            return;
        }
        taskManager.createSubtask(subtask);
        exchange.sendResponseHeaders(201, 0);
        sendResponse(exchange, String.format("Задача создана"));
    }

    private void delSubtaskById(HttpExchange exchange, String id) throws IOException {

        int ide = Integer.parseInt(id);

        taskManager.deleteSubtaskByID(ide);
        exchange.sendResponseHeaders(200, 0);
        sendResponse(exchange, String.format("Подзадача удалена"));

    }

    void sendResponse(HttpExchange exchange, String text) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(text.getBytes());
        }
    }

}