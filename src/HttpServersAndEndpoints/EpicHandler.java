package HttpServersAndEndpoints;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.interfaces.TaskManager;
import tasks.Epic;
import tasks.Task;
import tasks.enums.Methods;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static tasks.enums.Methods.*;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    protected EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String[] path = getPath(httpExchange);

        if (Methods.valueOf(method) == GET) {
            if (path.length < 3) {
                getEpicList(httpExchange);
            } else if (path.length == 3) {
                getEpicById(httpExchange, path[2]);
            } else {
                getEpicSubtasksById(httpExchange, path[3]);
            }
        } else if (Methods.valueOf(method) == POST) {
            putEpic(httpExchange);
        } else if (Methods.valueOf(method) == DELETE) {
            delEpicById(httpExchange, path[2]);
        }

    }

    private void getEpicList(HttpExchange exchange) throws IOException {

        List<Task> listEpic = taskManager.getAllEpics();
//        список задач в формате Json
        String listEpicJson = gson.toJson(listEpic);
        exchange.sendResponseHeaders(200, 0);

        sendResponse(exchange, listEpicJson);

    }


    private void getEpicSubtasksById(HttpExchange exchange, String id) throws IOException {

        int ide = -1;
        try {
            ide = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            exchange.sendResponseHeaders(400, 0);
            sendResponse(exchange, "Неверный формат. Id должен быть числом.");
            return;
        }

        Task epicId = taskManager.getEpicByID(ide);
        if (epicId == null) {
            exchange.sendResponseHeaders(404, 0);
            sendResponse(exchange, "Эпик не найден.");
            return;
        }

        String epicIdJson = gson.toJson(epicId);
        exchange.sendResponseHeaders(200, 0);
        sendResponse(exchange, epicIdJson);

    }


    private void getEpicById(HttpExchange exchange, String id) throws IOException {

        int ide = Integer.parseInt(id);

        Task epicId = taskManager.getEpicByID(ide);

        String epicIdJson = gson.toJson(epicId);
        exchange.sendResponseHeaders(200, 0);
        sendResponse(exchange, epicIdJson);

    }

    private void putEpic(HttpExchange exchange) throws IOException {

        String request = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (request.isBlank()) {
            exchange.sendResponseHeaders(400, 0);
            sendResponse(exchange, "Пустые входные данные");
            return;
        }

//        получаем объект задачи из Json - десериализуем
        Epic epic;
        try {
            epic = gson.fromJson(request, Epic.class);
        } catch (JsonSyntaxException e) {
            exchange.sendResponseHeaders(400, 0);
            sendResponse(exchange, "Неверный формат входных данных");
            return;
        }
        taskManager.createEpic(epic);
        exchange.sendResponseHeaders(201, 0);
        sendResponse(exchange, String.format("Задача создана"));
    }

    private void delEpicById(HttpExchange exchange, String id) throws IOException {

        int ide = Integer.parseInt(id);

        taskManager.deleteEpicByID(ide);
        exchange.sendResponseHeaders(200, 0);
        sendResponse(exchange, String.format("Эпик удален"));

    }

    void sendResponse(HttpExchange exchange, String text) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(text.getBytes());
        }
    }

}