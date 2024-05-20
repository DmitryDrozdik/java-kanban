package HttpServersAndEndpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.interfaces.TaskManager;

import java.io.IOException;

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



//        httpExchange.sendResponseHeaders(200, 0);
//
//        String response = "Ура!";
//
//        try (OutputStream os = httpExchange.getResponseBody()) {
//            os.write(response.getBytes());
//        }
    }

    private void getEpicSubtasksById(HttpExchange httpExchange, String id) {
    }

    private void getEpicList(HttpExchange exchange) {

    }

    private void getEpicById(HttpExchange exchange, String id) {

    }

    private void putEpic(HttpExchange exchange) {
// body
    }

    private void delEpicById(HttpExchange exchange, String id) {
// body
    }

}


