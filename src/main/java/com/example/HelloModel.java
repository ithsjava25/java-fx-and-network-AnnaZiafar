package com.example;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.util.JSONPObject;

import javax.xml.xpath.XPath;
import java.io.File;
import java.io.IOException;


/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel {

    private final ObservableList<NtfyMessage> messages = FXCollections.observableArrayList();
    private final StringProperty messageToSend = new SimpleStringProperty();

    private final NtfyConnection connection;
    private String topic;


    public HelloModel(NtfyConnection connection){
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

    public void setTopic(String topic){
        this.topic = topic;
    }

    public String getTopic(){
        return topic;
    }

    /**
     * Returns a greeting based on the current Java and JavaFX versions.
     */
    public String getGreeting() {
        return "Welcome to Chattrix!";
    }

    public void sendMessage() {
        connection.send(messageToSend.get(), getTopic());
    }

    public void receiveMessage(){
        connection.receive(m -> Platform.runLater(() -> messages.add(m)), getTopic());
    }


}

