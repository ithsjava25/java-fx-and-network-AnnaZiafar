package com.example;

import javafx.application.Platform;

import java.io.IOException;


/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel {
    private final NtfyConnection connection;
    private FileInput fileInput;
    private String topic;

    public void setTopic(String topic){
        this.topic = topic;
        clearMessagesWhenChangingTopic();

        try{
            fileInput.loadMessagesFromFile();
        } catch (IOException e) {
            System.out.println("Could not load old messages.");
            e.printStackTrace();
        }
    }

    public String getTopic(){
        return topic;
    }

    public void clearMessagesWhenChangingTopic(){
        fileInput.getMessages().clear();
    }


    public HelloModel(NtfyConnection connection){
        this.connection = connection;
    }

    public void setFileInput(FileInput fileInput){
        this.fileInput = fileInput;
    }

    /**
     * Returns a greeting based on the current Java and JavaFX versions.
     */
    public String getGreeting() {
        return "Welcome to Chattrix!";
    }

    public void sendMessage(String message) {
        connection.send(message, this.topic);
    }

    public void receiveMessage(){
        connection.receive(m -> Platform.runLater(() -> fileInput.addMessage(m)), this.topic);
    }
}

