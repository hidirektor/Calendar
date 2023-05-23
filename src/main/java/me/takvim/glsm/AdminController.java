package me.takvim.glsm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.io.*;

public class AdminController {
    @FXML
    private ListView<User> userListView;

    @FXML
    private Button deleteButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button goBackButton;

    @FXML
    private Label statusLabel;

    private ObservableList<User> userList;

    public void initialize() {
        userList = FXCollections.observableArrayList();
        userListView.setItems(userList);
        userListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        refreshUserList(); // Kullanıcı listesini başlangıçta yükle

        deleteButton.setOnAction(event -> deleteButtonClicked());
        refreshButton.setOnAction(event -> refreshButtonClicked());
    }

    private void refreshUserList() {
        userList.clear(); // Önceki kullanıcı listesini temizle

        try (BufferedReader reader = new BufferedReader(new FileReader("db.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 9) {
                    String username = userData[0];
                    String firstName = userData[1];
                    String lastName = userData[2];
                    String password = userData[3];
                    String email = userData[4];
                    String tcNo = userData[5];
                    String phone = userData[6];
                    String address = userData[7];
                    UserType userType = UserType.valueOf(userData[8]);

                    User user = new User(username, firstName, lastName, password, email, tcNo, phone, address, userType);
                    userList.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteButtonClicked() {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userList.remove(selectedUser);
            deleteUserFromFile(selectedUser);
            statusLabel.setText("Kullanıcı başarıyla silindi.");
        } else {
            statusLabel.setText("Lütfen bir kullanıcı seçin.");
        }
    }

    private void deleteUserFromFile(User user) {
        // Dosyadan kullanıcıyı silmek için ilgili işlemleri gerçekleştir
        // Örnek olarak, kullanıcıları tuttuğunuz bir "db.txt" dosyasından satır satır okuyup kullanıcıyı silme işlemini yapabilirsiniz.
        // Burada dosya üzerinde işlem yapılacaksa gerekli kontroller ve işlemler yapılmalıdır.
        // Aşağıda, örnek bir kullanıcı silme işlemi verilmiştir.

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("db.txt"))) {
            for (User existingUser : userList) {
                if (!existingUser.equals(user)) {
                    writer.write(existingUser.getUsername() + "," +
                            existingUser.getFirstName() + "," +
                            existingUser.getLastName() + "," +
                            existingUser.getPassword() + "," +
                            existingUser.getEmail() + "," +
                            existingUser.getTcNo() + "," +
                            existingUser.getPhone() + "," +
                            existingUser.getAddress() + "," +
                            existingUser.getUserType().toString());
                    writer.newLine();
                }
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshButtonClicked() {
        refreshUserList();
        statusLabel.setText("Kullanıcı listesi güncellendi.");
    }

    @FXML
    private void backButtonClicked() {
        try {
            // Giriş yapma ekranını yükle
            Parent loginRoot = FXMLLoader.load(Main.class.getResource("Login.fxml"));
            Scene loginScene = new Scene(loginRoot);

            // Geçerli sahneyi al
            Stage currentStage = (Stage) goBackButton.getScene().getWindow();

            // Giriş yapma ekranına yönlendir
            currentStage.setScene(loginScene);
            currentStage.setTitle("Giriş Yap");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
