package com.matdev.server.handlers;

import com.matdev.service.UserService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DeleteHandler implements HttpHandler {
    private final UserService userService = new UserService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("DELETE".equals(exchange.getRequestMethod())) {
            String[] pathParts = exchange.getRequestURI().getPath().split("/");

            if (pathParts.length < 3) {
                sendResponse(exchange, 400, "{\"error\": \"Invalid request URL\"}");
                return;
            }

            try {
                Long userId = Long.parseLong(pathParts[2]);
                boolean deleted = userService.delete(userId);

                if (deleted) {
                    sendResponse(exchange, 200, "{\"message\": \"User deleted successfully\"}");
                } else {
                    sendResponse(exchange, 404, "{\"error\": \"User not found\"}");
                }
            } catch (NumberFormatException e) {
                sendResponse(exchange, 400, "{\"error\": \"Invalid user ID format\"}");
            }
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method Not Allowed\"}");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}
