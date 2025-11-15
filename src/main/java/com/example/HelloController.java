package com.example;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller layer: mediates between the view (FXML) and the model.
 */
public class HelloController {

    private final HelloModel model = new HelloModel(new NtfyConnectionImpl());
    private final FileInput fileInput = new FileInput(model);

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
        messageView.setItems(model.getMessages());
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

            topicBox.setVisible(false);
            topicBox.setManaged(false);
            topicField.setText("");

            currentTopicBox.setVisible(true);
            currentTopicBox.setManaged(true);

            messageBox.setManaged(true);
            messageBox.setVisible(true);

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
            model.setMessageToSend(message);

        model.sendMessage();

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
    }
}
