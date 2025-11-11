package com.example;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.function.Consumer;

public class NtfyConnectionImpl implements NtfyConnection {
    private final  String hostName;
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NtfyConnectionImpl(){
        Dotenv dotenv = Dotenv.load();
        hostName = Objects.requireNonNull(dotenv.get("HOST_NAME"));
    }

    public NtfyConnectionImpl(String hostName){
        this.hostName = hostName;
    }

    @Override
    public boolean send(String message) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .uri(URI.create(hostName + "/anna"))
                .build();

        try {
            var response = http.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return true;
        } catch (IOException e) {
            System.out.println("Error occurred when trying to send message.");
        } catch (InterruptedException e) {
            System.out.println("Process interrupted when sending message.");
        }

        return false;
    }

    @Override
    public void receive(Consumer<NtfyMessage> message) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(hostName + "/anna/json"))
                .build();

        http.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofLines())
                .thenAccept(response -> response.body()
                        .map(s -> objectMapper.readValue(s, NtfyMessage.class))
                        .filter(s -> s.event().equals("message"))
                        .peek(System.out::println)
                        .forEach(message));
    }
}
