package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileInput {
    private final ObservableList<NtfyMessage> messages = FXCollections.observableArrayList();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String homeFolder = System.getProperty("user.home");
    private final File directory = Paths.get(homeFolder, "chattrix").toFile();
    private final File messageFile = Paths.get(String.valueOf(directory), "messages.json").toFile();

    public ObservableList<NtfyMessage> getMessages (){
        return messages;
    }

    public void addMessage(NtfyMessage message){
        this.messages.add(message);
        try {
            saveMessagesJSONFile();
        } catch (IOException e) {
            System.out.println("Failed to save message");
            throw new RuntimeException(e);
        }
    }

    public void loadMessagesFromFile() throws IOException {
        this.messages.clear();
        if(getFile(messageFile).length() > 0){
            List<NtfyMessage> oldMessages = getExistingMessages();
            this.messages.addAll(oldMessages);
        }
    }

    public void saveMessagesJSONFile() throws IOException {
        objectMapper.writeValue(getFile(messageFile), this.messages);
    }

    public List<NtfyMessage> getExistingMessages() throws IOException {
        TypeReference<List<NtfyMessage>> jacksonTypeReference = new TypeReference<>() {};
        return objectMapper.readValue(getFile(messageFile), jacksonTypeReference);
    }

    public File getFile(File file) throws IOException {
        getDirectory();
        if(file.exists())
            return file;
        else
            return createFile(file);
    }

    public File createFile(File file) throws IOException {
        getDirectory();
        if(file.createNewFile())
            return file;
        else
            throw new IOException("Something went wrong when creating the file");
    }

     public File getDirectory(){
        if(doesDirectoryExist())
            return directory;
        else
            return createDirectory();
     }

     public File createDirectory(){
         boolean directoryCreated = directory.mkdir();
         if(directoryCreated || directory.exists()){
             return directory;
         } else {
             throw new RuntimeException("Failed to create directory");
         }
     }

    public boolean doesDirectoryExist(){
        return directory.exists() && directory.isDirectory();
    }

}


