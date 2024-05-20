package HttpServersAndEndpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;

import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.OutputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import managers.Managers;
import managers.interfaces.TaskManager;

public class HttpTaskServer {
    public static void main(String[] args) throws IOException {

        TaskManager taskManager = Managers.getDefaultManager();


        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager)); // связываем путь и обработчик
        httpServer.start();

        Gson gson = new Gson();
    }
}