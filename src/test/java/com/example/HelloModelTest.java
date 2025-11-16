package com.example;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
class HelloModelTest {
    @Test
    void sendMessageCallsConnectionWithMessageToSend() {
        var spy = new NtfyConnectionSpy();
        var model = new HelloModel(spy);
        var fileInput = new FileInput();

        model.setFileInput(fileInput);
        model.sendMessage("Hello World!");

        assertThat(spy.message).isEqualTo("Hello World!");
    }
    
    @Test
    void sendMessageToFakeServer(WireMockRuntimeInfo wmRuntimeInfo) {
        var con = new NtfyConnectionImpl("http://localhost:" + wmRuntimeInfo.getHttpPort());
        var model = new HelloModel(con);
        var fileInput = new FileInput();

        model.setFileInput(fileInput);
        model.setTopic("anna");

        stubFor(post("/anna").willReturn(ok()));

        model.sendMessage("Hello World!");
        verify(postRequestedFor(urlEqualTo("/anna")).withRequestBody(containing("Hello World!")));
    
    }

    @Test
    void createTestFile() throws IOException {
        var fileInput = new FileInput();
        File testFile = new File("testfile.json");
        File file = fileInput.getFile(testFile);

        assertTrue(file.exists());

        boolean delete = file.delete();
        assertTrue(delete);
        assertFalse(file.exists());
    }

}