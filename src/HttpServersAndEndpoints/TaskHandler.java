package HttpServersAndEndpoints;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.interfaces.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler  implements HttpHandler {
    protected TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String[] path = getPath(httpExchange);
        switch (method) {
            case "GET":
                if(path.length < 3) {
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



//        httpExchange.sendResponseHeaders(200, 0);
//
//        String response = "Ура!";
//
//        try (OutputStream os = httpExchange.getResponseBody()) {
//            os.write(response.getBytes());
//        }
    }
    private void getTaskList(HttpExchange exchange) throws IOException {
//        List<Task> getAllTasks();

        List<Task> listTask = taskManager.getAllTasks();
//        список задач в формате Json
        String listTaskJson = gson.toJson(listTask);
        exchange.sendResponseHeaders(200, 0);

        sendResponse(exchange, listTaskJson);

    }

    private void getTaskById(HttpExchange exchange, String id) throws IOException {

        // Task getTaskByID(int ID);

        String[] path = getPath(exchange);
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
            sendResponse(exchange, "Пустые входные данные");
            return;
        }

//        получаем объект задачи из Json - десериализуем
        Task task;
        try {
            task = gson.fromJson(request, Task.class);
        } catch (JsonSyntaxException e) {
            exchange.sendResponseHeaders(400, 0);
            sendResponse(exchange, "Неверный формат входных данных");
            return;
        }
        taskManager.createTask(task);
        exchange.sendResponseHeaders(201, 0);
        sendResponse(exchange, String.format("Задача создана"));
        }

    private void delTaskById(HttpExchange exchange, String id) throws IOException {
        //exchange.sendResponseHeaders(203, 0);

        //void deleteTaskByID(int ID);

        String[] path = getPath(exchange);
        int ide = Integer.parseInt(id);

        Task task;
//        try {
//            task = gson.fromJson(request, Task.class);
//        } catch (JsonSyntaxException e) {
//            exchange.sendResponseHeaders(400, 0);
//            sendResponse(exchange, "Неверный формат входных данных");
//            return;
//        }
        taskManager.deleteTaskByID(ide);
        exchange.sendResponseHeaders(200, 0);
        sendResponse(exchange, String.format("Задача удалена"));



//        Task deletedTask = taskManager.deleteTaskByID(ide);
//
//        String deletedTaskJson = gson.toJson(deletedTask);
//        exchange.sendResponseHeaders(200, 0);
//        sendResponse(exchange, deletedTaskJson);




    }

    void sendResponse(HttpExchange exchange, String text) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(text.getBytes());
        }
    }

    //        System.out.println("tasks");
//        exchange.sendResponseHeaders(200, 0);
//        String response = "Ура!";
//
//        try (OutputStream os = exchange.getResponseBody()) {
//            os.write(response.getBytes());

}