package com.example;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel {

    private final String hostName;
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObservableList<NtfyMessage> messages = FXCollections.observableArrayList();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public HelloModel(){
        Dotenv dotenv = Dotenv.load();
        hostName = Objects.requireNonNull(dotenv.get("HOST_NAME"));
        receiveMessage();
    }


    /**
     * Returns a greeting based on the current Java and JavaFX versions.
     */
    public String getGreeting() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        return "Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".";
    }

//    public String inputMessage(){
//        return IO.readln();
//    }

    public void sendMessage() {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("Hello World!"))
                .uri(URI.create(hostName + "/anna"))
                .build();

        try {
            var response = http.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            System.out.println("Error occurred when trying to send message.");
        } catch (InterruptedException e) {
            System.out.println("Process interrupted when sending message.");        }


    }

    public void receiveMessage(){
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(hostName + "/anna/json"))
                .build();

        http.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofLines())
                .thenAccept(response -> response.body()
                        .map(s -> objectMapper.readValue(s, NtfyMessage.class))
                        .filter(s -> s.event().equals("message"))
                        .peek(System.out::println)
                        .forEach(s -> messages.add(s)));
    }
}

