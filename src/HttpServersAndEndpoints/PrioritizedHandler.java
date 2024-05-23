package HttpServersAndEndpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.interfaces.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

      protected PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        if(method.equals("GET")) {
            getPrioritized(httpExchange);
        }
    }

    private void getPrioritized(HttpExchange exchange) throws IOException{

          // List<Task> getPrioritizedTasks();

        List<Task> prioritized = taskManager.getPrioritizedTasks();
//        список задач в формате Json
        String prioritizedJson = gson.toJson(prioritized);
        exchange.sendResponseHeaders(200, 0);
        sendResponse(exchange, prioritizedJson);

    }


    void sendResponse(HttpExchange exchange, String text) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(text.getBytes());
        }
    }

}
