package com.example;

import java.util.function.Consumer;

public interface NtfyConnection {

    public void send(String message, String topic);

    public void receive(Consumer<NtfyMessage> message, String topic);

}
