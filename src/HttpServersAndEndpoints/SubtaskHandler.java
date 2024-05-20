package HttpServersAndEndpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.interfaces.TaskManager;

import java.io.IOException;
import java.io.OutputStream;

import com.google.gson.Gson;
import managers.interfaces.TaskManager;


public class SubtaskHandler extends BaseHttpHandler  implements HttpHandler {
        protected SubtaskHandler(TaskManager taskManager) {
            super(taskManager);
        }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String[] path = getPath(httpExchange);
        switch (method) {
            case "GET":
                if(path.length < 3) {
                    getSubtaskList(httpExchange);
                } else {
                    getSubtaskById(httpExchange, path[2]);
                }
                break;
            case "POST":
                putSubtask(httpExchange);
                break;
            case "DELETE":
                delSubtaskById(httpExchange, path[2]);
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
    private void getSubtaskList(HttpExchange exchange) {

    }

    private void getSubtaskById(HttpExchange exchange, String id) {

    }

    private void putSubtask(HttpExchange exchange) {
// body
    }

    private void delSubtaskById(HttpExchange exchange, String id) {
// body
    }
}
