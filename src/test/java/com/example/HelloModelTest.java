package com.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloModelTest {
    @Test
    void sendMessageCallsConnectionWithMessageToSend() {
        var spy = new NtfyConnectionSpy();
        var model = new HelloModel(spy);

        model.setMessageToSend("Hello World!");

        model.sendMessage();

        assertThat(spy.message).isEqualTo("Hello World!");

    }

}