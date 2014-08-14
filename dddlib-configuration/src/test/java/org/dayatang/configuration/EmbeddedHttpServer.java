package org.dayatang.configuration;

import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by yyang on 14-8-14.
 */
public class EmbeddedHttpServer {

    private static final int DEFAULT_PORT = 1528;

    private HttpServer httpServer;

    private int port;

    public EmbeddedHttpServer() throws IOException {
        this(DEFAULT_PORT);
    }

    public EmbeddedHttpServer(int port) throws IOException {
        this.port = port;
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.setExecutor(null);
    }

    public void mapping(final String context, final String data) {
        httpServer.createContext(context, new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                httpExchange.sendResponseHeaders(200, data.length());
                BufferedOutputStream os = new BufferedOutputStream(httpExchange.getResponseBody());
                os.write(data.getBytes());
                os.flush();
                os.close();
            }
        });
    }

    public void mapping(final String context, final File file) {
        httpServer.createContext(context, new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                ByteSource byteSource = Files.asByteSource(file);
                httpExchange.sendResponseHeaders(200, byteSource.size());
                BufferedOutputStream os = new BufferedOutputStream(httpExchange.getResponseBody());
                os.write(byteSource.read());
                os.flush();
                os.close();
            }
        });
    }

    public void start() {
        httpServer.start();
    }

    public void shutdown() {
        httpServer.stop(0);
    }
}
