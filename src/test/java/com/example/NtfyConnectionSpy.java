package com.example;

import java.util.function.Consumer;

public class NtfyConnectionSpy implements NtfyConnection{
    String message;
    String topic;

    @Override
    public void send(String message, String topic) {
        this.message = message;
        this.topic = topic;
    }

    @Override
    public void receive(Consumer<NtfyMessage> message, String topic) {

    }
}
