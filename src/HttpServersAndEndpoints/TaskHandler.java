package HttpServersAndEndpoints;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.interfaces.TaskManager;

import java.io.IOException;
import java.io.OutputStream;

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
    private void getTaskList(HttpExchange exchange) {

    }

    private void getTaskById(HttpExchange exchange, String id) {

    }

    private void putTask(HttpExchange exchange) {
// body
    }

    private void delTaskById(HttpExchange exchange, String id) {
// body
    }
}