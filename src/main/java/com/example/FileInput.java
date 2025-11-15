package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileInput {
    private final ObservableList<NtfyMessage> messages = FXCollections.observableArrayList();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HelloModel model;
    private final String currentTopic;

    public FileInput(HelloModel model){
        this.model = model;
        currentTopic = model.getTopic();
    }

//    public ObservableList<NtfyMessage> getMessages() {
//
//    }

    public void saveMessagesJSONFile() throws IOException {
        List<NtfyMessage> allMessages = new ArrayList<>();
        File targetFile = getFile();
        if(targetFile.length() > 0){
            TypeReference<List<NtfyMessage>> jacksonTypeReference = new TypeReference<>() {};
            List<NtfyMessage> oldMessages = objectMapper.readValue(targetFile, jacksonTypeReference);
            allMessages.addAll(oldMessages);
        }

        allMessages.addAll(model.getMessages());
        objectMapper.writeValue(targetFile, allMessages);
    }

    public String fileName(){
        return currentTopic + ".json";
    }

    public File getFile() throws IOException {
        File file = new File(getDirectory(), fileName());
        if(file.exists())
            return file;
        else
            return createFile();
    }

    private File createFile() throws IOException {
        File file = new File(getDirectory(), fileName());
        if(file.createNewFile())
            return file;
        else {
            throw new IOException("Something went wrong when creating the file.");
        }
    }

    public File createDirectory(){
        File directory = directoryPath();
        boolean directoryCreated = directory.mkdir();
        if(directoryCreated){
            return directory;
        } else {
            throw new RuntimeException("Failed to create directory. There already exists a file called chattrix.");
        }
    }

    public File getDirectory(){
        if(doesDirectoryExist())
            return directoryPath();
        else
            return createDirectory();
    }

    public boolean doesDirectoryExist(){
        File directory = directoryPath();
        if(directory.exists() && directory.isDirectory())
            return true;
        else
            return false;
    }

    public File directoryPath(){
        String directoryName = "chattrix";
        String homeFolder = System.getProperty("user.home");
        String directoryPath = homeFolder + File.separator + directoryName;

        return new File(directoryPath);
    }
}
