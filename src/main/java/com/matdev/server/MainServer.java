package com.matdev.server;

import com.matdev.server.handlers.DataHandler;
import com.matdev.server.handlers.DeleteHandler;
import com.matdev.server.handlers.PostHandler;
import com.matdev.server.handlers.UpdateHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MainServer {

    private static final int PORT = 8080;
    private static final String SERVER_RUNNING_MESSAGE = "Server running on port ";


    public static void main(String[] args) throws IOException {

        HttpServer server = createServer(PORT);
        configureServer(server);
        startServer(server);
    }

    private static HttpServer createServer(int port) throws IOException {
        return HttpServer.create(new InetSocketAddress(port), 0);
    }

    private static void configureServer(HttpServer server) {
        server.createContext("/create", new PostHandler());
        server.createContext("/read", new DataHandler());
        server.createContext("/update", new UpdateHandler());
        server.createContext("/delete", new DeleteHandler());
        server.setExecutor(null);
    }

    private static void startServer(HttpServer server) {
        server.start();
        System.out.println(SERVER_RUNNING_MESSAGE + PORT + "...");
    }
}
