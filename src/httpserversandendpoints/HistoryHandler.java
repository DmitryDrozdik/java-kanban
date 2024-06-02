package httpserversandendpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.interfaces.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    protected HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        if (method.equals("GET")) {
            getHistory(httpExchange);
        }
    }

    private void getHistory(HttpExchange exchange) throws IOException {

        List<Task> history = taskManager.getHistory();
//        список задач в формате Json
        String historyJson = gson.toJson(history);
        exchange.sendResponseHeaders(200, 0);
        sendResponse(exchange, historyJson);

    }

    void sendResponse(HttpExchange exchange, String text) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(text.getBytes());
        }
    }

}
