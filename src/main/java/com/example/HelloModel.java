package com.example;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel {

    private final ObservableList<NtfyMessage> messages = FXCollections.observableArrayList();

    private final NtfyConnection connection;
    private final StringProperty messageToSend = new SimpleStringProperty();

    public HelloModel(NtfyConnection connection){
        receiveMessage();
        this.connection = connection;
    }

    public ObservableList<NtfyMessage> getMessages (){
        return messages;
    }

    public String getMessageToSend() {
        return messageToSend.get();
    }

    public StringProperty messageToSendProperty() {
        return messageToSend;
    }

    public void setMessageToSend(String message){
        messageToSend.set(message);
    }

    /**
     * Returns a greeting based on the current Java and JavaFX versions.
     */
    public String getGreeting() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        return "Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".";
    }

    public void sendMessage() {
        connection.send(messageToSend.get());


    }

    public void receiveMessage(){
        connection.receive(m -> Platform.runLater(() -> messages.add(m)));

    }
}

