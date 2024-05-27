package HttpServersAndEndpoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import managers.interfaces.TaskManager;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpHandler {
    protected TaskManager taskManager;
    protected Gson gson;

    String[] getPath(HttpExchange exchange) {
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] str = path.split("/");
        return str;
    }

    protected BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .excludeFieldsWithoutExposeAnnotation();
        gson = gsonBuilder.create();
    }

}
