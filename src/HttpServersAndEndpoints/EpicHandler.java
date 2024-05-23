package HttpServersAndEndpoints;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.interfaces.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler  implements HttpHandler {
    protected EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String[] path = getPath(httpExchange);
        switch (method) {
            case "GET":
                if(path.length < 3) {
                    getEpicList(httpExchange);
                } else if (path.length < 3) {
                    getEpicById(httpExchange, path[2]);
                } else {
                    getEpicSubtasksById(httpExchange, path[3]);
                }
                break;
            case "POST":
                putEpic(httpExchange);
                break;
            case "DELETE":
                delEpicById(httpExchange, path[2]);
                break;
        }

    }

    private void getEpicList(HttpExchange exchange) throws IOException {
//        List<Task> getAllEpics();

        List<Task> listEpic = taskManager.getAllEpics();
//        список задач в формате Json
        String listEpicJson = gson.toJson(listEpic);
        exchange.sendResponseHeaders(200, 0);

        sendResponse(exchange, listEpicJson);

    }


    private void getEpicSubtasksById(HttpExchange exchange, String id) throws IOException {

        // Subtask getSubtaskByID(int ID);

        String[] path = getPath(exchange);
        int ide = Integer.parseInt(id);

        Task subtaskId = taskManager.getSubtaskByID(ide);

        String subtaskIdJson = gson.toJson(subtaskId);
        exchange.sendResponseHeaders(200, 0);
        sendResponse(exchange, subtaskIdJson);

    }


    private void getEpicById(HttpExchange exchange, String id) throws IOException {

        // Epic getEpicByID(int ID);

        String[] path = getPath(exchange);
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


    private void delEpicById(HttpExchange exchange, String id) {
// body
    }

    void sendResponse(HttpExchange exchange, String text) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(text.getBytes());
        }
    }

}


