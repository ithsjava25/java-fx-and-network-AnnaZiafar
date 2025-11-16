package com.example;

import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Controller layer: mediates between the view (FXML) and the model.
 */
public class HelloController {

    private final HelloModel model = new HelloModel(new NtfyConnectionImpl());
    private final FileInput fileInput = new FileInput();
    private final FilteredList<NtfyMessage> filteredMessages = new FilteredList<>(fileInput.getMessages());

    @FXML
    public ListView<NtfyMessage> messageView;
    @FXML
    private Label messageLabel;
    @FXML
    private HBox topicBox;
    @FXML
    private TextField topicField;
    @FXML
    private HBox messageBox;
    @FXML
    private TextField messageField;
    @FXML
    private Button changeTopicButton;
    @FXML
    public VBox currentTopicBox;
    @FXML
    public Label currentTopicLabel;

    @FXML
    private void initialize() {
        if (messageLabel != null) {
            messageLabel.setText(model.getGreeting());
        }

        model.setFileInput(fileInput);
        messageView.setItems(filteredMessages);
        messageView.setCellFactory(list -> createCell());
    }

    /**
     *
     * @return a list of messages with timestamps. Excludes id, event and topic.
     */
    private ListCell<NtfyMessage> createCell(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return new ListCell<>(){
            @Override
            public void updateItem(NtfyMessage item, boolean empty){
                super.updateItem(item, empty);
                if(empty || item == null){
                    setText("");
                }
                else{
                    //Konvertera epoch-tid till en tidstämpel
                    String time = Instant.ofEpochSecond(item.time())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime()
                            .format(formatter);

                    setText(time + "   " + item.message());
                }
            }
        };
    }

    @FXML
    public void handleTopic(ActionEvent event){
        String topic = topicField.getText();
        if(topic.isBlank())
            System.out.println("You must choose a topic. Please try again.");
        else {
            model.setTopic(topic);
            model.receiveMessage();
            filteredMessages.setPredicate(message -> message.topic().equals(topic));

            topicBox.setVisible(false);
            topicBox.setManaged(false);
            topicField.setText("");

            currentTopicLabel.setText("Topic: " + topic);
            currentTopicBox.setVisible(true);
            currentTopicBox.setManaged(true);

            messageBox.setManaged(true);
            messageBox.setVisible(true);
            messageView.setVisible(true);

            changeTopicButton.setManaged(true);
            changeTopicButton.setVisible(true);
        }
    }

    @FXML
    public void sendMessage(ActionEvent actionEvent) {
        String message = messageField.getText();

        if(message.isBlank())
            System.out.println("You can not send an empty message. Please try again.");
        else
            model.sendMessage(message);

        try{
            fileInput.saveMessagesJSONFile();
        } catch (IOException e) {
            System.out.println("Could not save recieved messages to file");
            throw new RuntimeException(e);
        }
        messageField.setText("");
    }

    @FXML
    private void changeTopic(ActionEvent event){
        topicBox.setVisible(true);
        topicBox.setManaged(true);

        messageBox.setVisible(false);
        messageBox.setManaged(false);

        changeTopicButton.setVisible(false);
        changeTopicButton.setManaged(false);
        currentTopicBox.setVisible(false);

        messageView.setVisible(false);
    }
}
