package com.matdev.server.handlers;

import com.matdev.model.User;
import com.matdev.security.RSADecrypt;
import com.matdev.service.UserService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.List;

public class DataHandler implements HttpHandler {
    private final UserService userService = new UserService();
    private final PrivateKey privateKey;

    public DataHandler(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

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
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBytes);
        }
    }

    private String convertToJson(List<User> users) throws Exception {

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            String decryptedUserDocument = decryptData(user.getUserDocument());
            String decryptedCreditCardToken = decryptData(user.getCreditCardToken());

            json.append("{")
                    .append("\"id\":").append(user.getId()).append(",")
                    .append("\"userDocument\":").append("\"").append(decryptedUserDocument).append("\",")
                    .append("\"creditCardToken\":").append("\"").append(decryptedCreditCardToken).append("\",")
                    .append("\"value\":").append(user.getValue())
                    .append("}");
            if (i < users.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    private String decryptData(String encryptedData) throws Exception {
        if (encryptedData == null || encryptedData.isEmpty()) {
            return "";
        }

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = RSADecrypt.decrypt(encryptedBytes, privateKey).getBytes();

        return new String(decryptedBytes);
    }
}
