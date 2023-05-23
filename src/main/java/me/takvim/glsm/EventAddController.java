package me.takvim.glsm;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;

import javazoom.jl.player.Player;


public class EventAddController {

    @FXML
    private DatePicker operationTimePicker;

    @FXML
    private TextField startTimeField;

    @FXML
    private TextField endTimeField;

    @FXML
    private TextField eventTypeField;

    @FXML
    private TextArea eventDescriptionArea;

    @FXML
    private Button addButton;

    private String userName;
    private TableView<Event> eventTable;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEventTable(TableView<Event> eventTable) {
        this.eventTable = eventTable;
    }

    @FXML
    private void addButtonClicked() {
        String operationTime = operationTimePicker.getValue().toString();
        String startTime = startTimeField.getText();
        String endTime = endTimeField.getText();
        String eventType = eventTypeField.getText();
        String eventDescription = eventDescriptionArea.getText();

        Event newEvent = new Event(operationTime, startTime, endTime, eventType, eventDescription, userName);

        // Yeni olayı events.txt dosyasına kaydetme işlemi
        saveEventToFile(newEvent);

        // Olayı tabloya ekleme işlemi
        eventTable.getItems().add(newEvent);

        // Belirtilen saatte bip sesini çal
        playBipSound(startTime);

        // Olay ekleme ekranını kapat
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelButtonClicked() {
        // Olay ekleme ekranını kapat
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }

    private void saveEventToFile(Event event) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("events.txt", true))) {
            writer.write(event.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playBipSound(String startTime) {
        Thread countdownThread = new Thread(() -> {
            try {
                // Olayın başlama zamanını belirtilen saat formatına çevir
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime eventStartTime = LocalTime.parse(startTime, formatter);

                // Şu anki saat
                LocalTime currentTime = LocalTime.now();

                // Olayın başlama zamanına kadar olan süre
                long delayInMillis = eventStartTime.toSecondOfDay() - currentTime.toSecondOfDay();

                if (delayInMillis > 0) {
                    Thread.sleep(delayInMillis * 1000);
                }

                // Ses dosyasını çal
                playSound("bip.mp3");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        countdownThread.start();
    }

    private void playSound(String soundFile) {
        try {
            URL resourceUrl = Main.class.getResource("/" + soundFile);
            if (resourceUrl == null) {
                System.err.println("Ses dosyası bulunamadı: " + soundFile);
                return;
            }

            InputStream inputStream = resourceUrl.openStream();
            Player player = new Player(inputStream);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}