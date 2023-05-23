package me.takvim.glsm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NormalUserController {

    @FXML
    private Label usernameLabel;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<Event> eventTable;

    @FXML
    private TableColumn<Event, String> operationTimeColumn;

    @FXML
    private TableColumn<Event, String> startTimeColumn;

    @FXML
    private TableColumn<Event, String> endTimeColumn;

    @FXML
    private TableColumn<Event, String> eventTypeColumn;

    @FXML
    private TableColumn<Event, String> eventDescriptionColumn;

    private String userName;

    public void setUserName(String userName) {
        this.userName = userName;
        usernameLabel.setText("Kullanıcı Adı: " + userName);
    }

    public void loadEvents() {
        List<Event> events = readEventsFromFile(userName);
        populateEventTable(events);
    }

    private List<Event> readEventsFromFile(String userName) {
        List<Event> events = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("events.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6 && parts[0].equals(userName)) {
                    String operationTime = parts[1].trim();
                    String startTime = parts[2].trim();
                    String endTime = parts[3].trim();
                    String eventType = parts[4].trim();
                    String eventDescription = parts[5].trim();

                    Event event = new Event(operationTime, startTime, endTime, eventType, eventDescription, userName);
                    events.add(event);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return events;
    }

    private List<Event> readEventsFromFileWDate(String userName, LocalDate selectedDate) {
        List<Event> events = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("events.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6 && parts[0].equals(userName)) {
                    String operationTime = parts[1].trim();
                    String startTime = parts[2].trim();
                    String endTime = parts[3].trim();
                    String eventType = parts[4].trim();
                    String eventDescription = parts[5].trim();

                    Event event = new Event(operationTime, startTime, endTime, eventType, eventDescription, userName);

                    // Seçilen tarihe ait olayları filtrele
                    LocalDate eventDate = LocalDate.parse(operationTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    if (eventDate.isEqual(selectedDate)) {
                        events.add(event);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return events;
    }

    private void populateEventTable(List<Event> events) {
        operationTimeColumn.setCellValueFactory(data -> data.getValue().operationTimeProperty());
        startTimeColumn.setCellValueFactory(data -> data.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(data -> data.getValue().endTimeProperty());
        eventTypeColumn.setCellValueFactory(data -> data.getValue().eventTypeProperty());
        eventDescriptionColumn.setCellValueFactory(data -> data.getValue().eventDescriptionProperty());

        eventTable.getItems().setAll(events);

        if (events.isEmpty()) {
            showAlert("Bilgi", null, "Seçtiğiniz tarihte bir olay eklenmemiş.");
            events = readEventsFromFile(userName);
            populateEventTable(events);
        }
    }

    public void showEvents() {
        LocalDate selectedDate = datePicker.getValue();

        if (selectedDate != null) {
            List<Event> events = readEventsFromFileWDate(userName, selectedDate);
            populateEventTable(events);
        } else {
            showAlert("Hata", null, "Lütfen bir tarih seçin.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void backButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Mevcut pencereyi kapat
            Stage currentStage = (Stage) usernameLabel.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addEventButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("eventAdd.fxml"));
            Parent root = loader.load();

            EventAddController eventAddController = loader.getController();
            eventAddController.setUserName(userName);
            eventAddController.setEventTable(eventTable);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Olay Oluştur");
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("/icons/ikon.png")));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteEventButtonClicked() {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            eventTable.getItems().remove(selectedEvent);
            deleteEventFromFile(selectedEvent);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText(null);
            alert.setContentText("Lütfen silmek için bir olay seçin.");
            alert.showAndWait();
        }
    }

    private void deleteEventFromFile(Event event) {
        String fileName = "events.txt";
        String tempFileName = "events_temp.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFileName))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String eventUserName = parts[0].trim();
                String operationTime = parts[1].trim();
                String startTime = parts[2].trim();
                String endTime = parts[3].trim();
                String eventType = parts[4].trim();
                String eventDescription = parts[5].trim();

                if (!eventUserName.equals(userName) || !operationTime.equals(event.getOperationTime())
                        || !startTime.equals(event.getStartTime()) || !endTime.equals(event.getEndTime())
                        || !eventType.equals(event.getEventType()) || !eventDescription.equals(event.getEventDescription())) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Dosya adlarını değiştir
        File oldFile = new File(fileName);
        File newFile = new File(tempFileName);
        if (oldFile.delete() && newFile.renameTo(oldFile)) {
            System.out.println("Olay dosyası güncellendi.");
        } else {
            System.out.println("Olay dosyası güncellenirken bir hata oluştu.");
        }
    }

    @FXML
    private void openCalendarView() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("calendarView.fxml"));
            Parent root = loader.load();
            root.setStyle("-fx-background-image: url('/arka.png'); -fx-background-position: center top; -fx-background-size: cover;");

            CalendarViewController calendarViewController = loader.getController();
            calendarViewController.setUserName(userName);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Takvim");
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("/icons/ikon.png")));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}