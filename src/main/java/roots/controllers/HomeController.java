package roots.controllers;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import roots.models.User;
import roots.models.UserSession;
import roots.services.WaterService;
import roots.utils.ChangeFXML;


public class HomeController {

    @FXML
    public void showPomodoro(Event event) {
        ChangeFXML.changeFXML(event, "/view/pomodoro.fxml");
    }

    @FXML
    public void logout(Event event) {
        ChangeFXML.changeFXML(event, "/view/login.fxml");
    }

    @FXML
    public void showTodoList(Event event) {
        ChangeFXML.changeFXML(event, "/view/todo.fxml");
    }

    @FXML
    public void showStatistics(Event event) {
        ChangeFXML.changeFXML(event, "/view/statistics.fxml");
    }

    private WaterService waterService = new WaterService();
    @FXML
    private Label lblWaterTotal;

    private User currentUser = UserSession.getCurrentUser();
    @FXML
    private void handleDrinkWater() {
        try {
            waterService.addWater(250, currentUser);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("💧 Đã ghi nhận thêm 250ml nước");
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateWaterUI() {
        int total = waterService.getTodayTotal(currentUser);
        Platform.runLater(() -> {
            if(lblWaterTotal != null) lblWaterTotal.setText("Đã uống: " + total + "ml");
        });
    }
    public void showAdvancedReminder() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CodeMate Reminder");
        alert.setHeaderText("💧 Đã đến lúc uống nước rồi!");
        alert.setContentText("Bạn đã uống nước chưa?");

        ButtonType btnDone = new ButtonType("Đã uống 250ml");
        ButtonType btnIgnore = new ButtonType("Để sau", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnDone, btnIgnore);

        alert.showAndWait().ifPresent(response -> {
            if (response == btnDone) {
                waterService.addWater(250, currentUser);
                updateWaterUI();
            }
        });
    }
}