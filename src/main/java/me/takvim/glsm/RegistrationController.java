package me.takvim.glsm;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RegistrationController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField tcNoField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField addressField;

    @FXML
    private ComboBox<UserType> userTypeComboBox;

    @FXML
    private Label statusLabel;

    @FXML
    private Button goBackButton;

    public void initialize() {
        // ComboBox içeriğini ayarlayalım
        userTypeComboBox.setItems(FXCollections.observableArrayList(UserType.values()));
    }

    public void registerButtonClicked() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String tcNo = tcNoField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        UserType userType = userTypeComboBox.getValue();

        // Burada kayıt işlemlerini gerçekleştir ve verileri dosyaya yaz

        // Örnek olarak, verileri "db.txt" dosyasına satır satır yazalım.
        String data = username + "," + firstName + "," + lastName + "," + password + "," + email + "," + tcNo + "," + phone + "," + address + "," + userType;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("db.txt", true))) {
            writer.write(data);
            writer.newLine();
            writer.flush();

            statusLabel.setText("Kayıt başarılı!");
            clearFields(); // Alanları temizle

            // Kayıt başarılı olduktan sonra Giriş yapma ekranına yönlendir
            redirectToLoginScreen();
        } catch (IOException e) {
            statusLabel.setText("Kayıt sırasında bir hata oluştu.");
            e.printStackTrace();
        }
    }

    @FXML
    private void goBackButtonClicked() {
        try {
            // Giriş yapma ekranını yükle
            Parent loginRoot = FXMLLoader.load(getClass().getResource("Login.fxml"));
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

    private void redirectToLoginScreen() {
        try {
            // Giriş yapma ekranını yükle
            Parent loginRoot = FXMLLoader.load(getClass().getResource("Login.fxml"));
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

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        usernameField.clear();
        passwordField.clear();
        tcNoField.clear();
        phoneField.clear();
        emailField.clear();
        addressField.clear();
        userTypeComboBox.getSelectionModel().clearSelection();
    }
}