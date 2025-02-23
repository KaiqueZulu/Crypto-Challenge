package com.matdev.server.handlers;

import com.matdev.model.User;
import com.matdev.service.UserService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.PublicKey;

public class PostHandler implements HttpHandler {
    private final UserService userService = new UserService();

    private final PublicKey publicKey;

    public PostHandler(PublicKey publicKey) {
        this.publicKey = publicKey;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {

            try (
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
            ) {
                String rawUserInput = bufferedReader.readLine();
                if (rawUserInput == null || rawUserInput.isEmpty()) {
                    sendResponse(exchange, 400, "{\"error\": \"Invalid input\"}");
                    return;
                }
                User user = userService.create(rawUserInput, publicKey);
                String response = convertToJson(user);
                sendResponse(exchange, 200, response);
            } catch (Exception e) {
                sendResponse(exchange, 500, "{\"error\": \"Internal server error\"}");
            }

        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method Not Allowed\"}");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response.getBytes());
        }
    }

    private String convertToJson(User user) {
        return String.format("{\"id\":%d, \"userDocument\":\"%s\", \"creditCardToken\":\"%s\", \"value\":%d}",
                user.getId(), user.getUserDocument(), user.getCreditCardToken(), user.getValue());
    }

}
