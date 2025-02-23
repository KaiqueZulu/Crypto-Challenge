package com.matdev.server;

import com.matdev.security.RSAKeyGenerator;
import com.matdev.server.handlers.DataHandler;
import com.matdev.server.handlers.DeleteHandler;
import com.matdev.server.handlers.PostHandler;
import com.matdev.server.handlers.UpdateHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class MainServer {

    private static final int PORT = 8080;
    private static final String SERVER_RUNNING_MESSAGE = "Server running on port ";

    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    public static void main(String[] args) throws IOException {
        generateRSAKeys();

        HttpServer server = createServer(PORT);
        configureServer(server);
        startServer(server);
    }

    private static void generateRSAKeys() {
        try {
            KeyPair keyPair = RSAKeyGenerator.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            System.out.println("🔐 RSA keys generated successfully!");
        } catch (Exception e) {
            throw new RuntimeException("Error generating RSA keys ", e);
        }
    }

    private static HttpServer createServer(int port) throws IOException {
        return HttpServer.create(new InetSocketAddress(port), 0);
    }

    private static void configureServer(HttpServer server) {
        server.createContext("/create", new PostHandler(publicKey));
        server.createContext("/read", new DataHandler(privateKey));
        server.createContext("/update", new UpdateHandler(publicKey));
        server.createContext("/delete", new DeleteHandler());
        server.setExecutor(null);
    }

    private static void startServer(HttpServer server) {
        server.start();
        System.out.println(SERVER_RUNNING_MESSAGE + PORT + "...");
    }
}
