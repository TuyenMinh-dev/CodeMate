package roots.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MainController {
    @FXML private StackPane contentArea;

    // Hàm dùng chung để thay đổi màn hình
    private void setPage(String fxmlPath) {
        try {
            // Load file FXML mới
            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
            // Xóa màn hình cũ, thêm màn hình mới vào
            contentArea.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void showPomodoro() { setPage("/pomodoro.fxml"); }

    @FXML public void showTodoList() { setPage("/todo.fxml"); }

    @FXML public void showStatistics() {
        // Có thể mở cửa sổ mới như cũ hoặc nhúng vào Dashboard tùy bạn
        setPage("/statistics.fxml");
    }
}