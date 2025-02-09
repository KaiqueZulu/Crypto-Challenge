package com.matdev.server.handlers;

import com.matdev.model.User;
import com.matdev.service.UserService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

public class UpdateHandler implements HttpHandler {
    private final UserService userService = new UserService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"PUT".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "{\"error\": \"Method Not Allowed\"}");
            return;
        }

        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        if (pathParts.length < 3) {
            sendResponse(exchange, 400, "{\"error\": \"Invalid request URL\"}");
            return;
        }

        try {
            Long id = Long.parseLong(pathParts[2]);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
                String rawUserInput = reader.readLine();

                if (rawUserInput == null || rawUserInput.isEmpty()) {
                    sendResponse(exchange, 400, "{\"error\": \"Empty request body\"}");
                    return;
                }

                User updatedUser = userService.update(id, rawUserInput);
                String response = "{\"message\": \"User updated successfully\", \"user\": " + convertToJson(updatedUser) + "}";
                sendResponse(exchange, 200, response);

            } catch (NoSuchElementException e) {
                sendResponse(exchange, 404, "{\"error\": \"User not found\"}");
            } catch (Exception e) {
                sendResponse(exchange, 500, "{\"error\": \"Internal server error\"}");
            }
        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "{\"error\": \"Invalid user ID format\"}");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    private String convertToJson(User user) {
        return String.format("{\"id\":%d, \"userDocument\":\"%s\", \"creditCardToken\":\"%s\", \"value\":%d}",
                user.getId(), user.getUserDocument(), user.getCreditCardToken(), user.getValue());
    }
}
