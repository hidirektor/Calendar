package me.takvim.glsm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    public void loginButtonClicked() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Burada giriş işlemlerini gerçekleştir ve kullanıcıyı yönlendir
        // Eğer giriş başarılı ise, kullanıcının UserType'ını kontrol ederek ilgili ekrana yönlendir.

        // Örnek bir kontrol
        if (username.equals("admin") && password.equals("admin123")) {
            redirectToAdminScreen();
        } else {
            redirectToNormalUserScreen();
        }
    }

    public void registerButtonClicked() {
        try {
            // Kayıt ol ekranını yükle
            Parent registrationRoot = FXMLLoader.load(Main.class.getResource("Registration.fxml"));
            Scene registrationScene = new Scene(registrationRoot);

            // Geçerli sahneyi al
            Stage currentStage = (Stage) registerButton.getScene().getWindow();

            // Kayıt ol ekranına yönlendir
            currentStage.setScene(registrationScene);
            currentStage.setTitle("Kayıt Ol");
        } catch (IOException e) {
            statusLabel.setText("Kayıt olma ekranı yüklenirken bir hata oluştu.");
            e.printStackTrace();
        }
    }

    private void redirectToAdminScreen() {
        try {
            // Admin ekranını yükle
            Parent adminRoot = FXMLLoader.load(Main.class.getResource("admin.fxml"));
            Scene adminScene = new Scene(adminRoot);

            // Geçerli sahneyi al
            Stage currentStage = (Stage) registerButton.getScene().getWindow();

            // Admin ekranına yönlendir
            currentStage.setScene(adminScene);
            currentStage.setTitle("Admin Ekranı");
        } catch (IOException e) {
            statusLabel.setText("Admin ekranı yüklenirken bir hata oluştu.");
            e.printStackTrace();
        }
    }

    private void redirectToNormalUserScreen() {
        try {
            // Normal kullanıcı ekranını yükle
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("normalUser.fxml"));
            Parent userRoot = loader.load();
            Scene userScene = new Scene(userRoot);

            // Geçerli sahneyi al
            Stage currentStage = (Stage) registerButton.getScene().getWindow();

            // Normal kullanıcı ekranına yönlendir
            currentStage.setScene(userScene);
            currentStage.setTitle("Normal Kullanıcı Ekranı");

            // Normal kullanıcı ekranının kontrolcüsünü al
            NormalUserController userController = loader.getController();
            userController.setUserName(usernameField.getText());
            userController.loadEvents();
        } catch (IOException e) {
            statusLabel.setText("Normal kullanıcı ekranı yüklenirken bir hata oluştu.");
            e.printStackTrace();
        }
    }
}
