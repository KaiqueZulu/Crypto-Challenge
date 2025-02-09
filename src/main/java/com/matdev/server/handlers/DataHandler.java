package com.matdev.server.handlers;

import com.matdev.model.User;
import com.matdev.service.UserService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DataHandler implements HttpHandler {
    private final UserService userService = new UserService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try {
            List<User> users = userService.userList();
            String response = convertToJson(users);
            sendResponse(exchange, 200, response);
        } catch (Exception e) {
            sendResponse(exchange, 500, "Internal server error");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.length());

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    private String convertToJson(List<User> users) {

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            json.append("{")
                    .append("\"id\":").append(user.getId()).append(",")
                    .append("\"userDocument\":").append("\"").append(user.getUserDocument()).append("\",")
                    .append("\"creditCardToken\":").append("\"").append(user.getCreditCardToken()).append("\",")
                    .append("\"value\":").append(user.getValue())
                    .append("}");
            if (i < users.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }
}
