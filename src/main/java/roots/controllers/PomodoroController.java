package roots.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import roots.entity.PomodoroState;
import roots.services.PomodoroTimer;
import roots.services.StatService;

public class PomodoroController {

    @FXML private Label timeLabel;
    @FXML private Label statusLabel;
    @FXML private ChoiceBox<Integer> durationChoice;
    @FXML private Button btnStart, btnBreak, btnSkip;
    @FXML private HBox configBox;
    @FXML private StackPane mainRoot;
    @FXML private Button btnStat;

    private PomodoroState state = PomodoroState.IDLE;
    private final PomodoroTimer timer = new PomodoroTimer();
    private final StatService statService = new StatService();

    private int workMinutes = 30;
    private final int breakMinutes = 5;

    // Biến đếm thời gian làm việc liên tục (giây)
    private int continuousWorkSeconds = 0;
    private final int WATER_REMINDER_THRESHOLD = 3600; // 1 giờ = 3600 giây

    public PomodoroController() {
        timer.onTick(this::onTick);
        timer.onStateChange(this::onStateChange);
        timer.onFinish(this::onFinish);
    }

    @FXML
    public void initialize() {
        durationChoice.getItems().addAll(25, 30, 35);
        durationChoice.setValue(30);
        updateTimeLabel(durationChoice.getValue() * 60);

        // Lắng nghe thay đổi thời gian khi user chọn
        durationChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (state == PomodoroState.IDLE && newVal != null) {
                updateTimeLabel(newVal * 60);
            }
        });
    }

    @FXML
    public void handleStart() {
        if (state == PomodoroState.IDLE) {
            workMinutes = durationChoice.getValue();
            timer.startWork(10);
        } else {
            // Nếu đang chạy mà bấm nút này (lúc này là nút Dừng)
            stopAll();
        }
    }

    @FXML
    public void handleBreak() {
        showWaterPopup(); // Thông báo uống nước trước khi nghỉ
        continuousWorkSeconds = 0; // Reset thời gian làm liên tục
        timer.startRest(5);
    }

    @FXML
    public void handleSkip() {
        // Nếu đã làm quá 1 tiếng mà vẫn định Skip
        if (continuousWorkSeconds >= 15) {
            showWaterPopup();
            // Tùy bạn: Cho làm tiếp luôn hoặc ép nghỉ. Ở đây mình nhắc xong cho làm tiếp:
            continuousWorkSeconds = 0;
        }
        timer.startWork(10);
    }

    private void onTick(int secondsLeft) {
        Platform.runLater(() -> {
            updateTimeLabel(secondsLeft);
            if (state == PomodoroState.WORK) {
                continuousWorkSeconds++;
                // Kiểm tra nếu đang làm mà chạm mốc 1 tiếng
                if (continuousWorkSeconds == WATER_REMINDER_THRESHOLD) {
                    statusLabel.setText("⚠️ Bạn đã làm 1 giờ rồi! Hãy uống nước.");
                }
            }
        });
    }

    private void onStateChange(PomodoroState newState) {
        this.state = newState;
        Platform.runLater(() -> {
            // Xóa class cũ để tránh bị chồng chéo màu
            mainRoot.getStyleClass().removeAll("work-mode", "rest-mode");

            switch (newState) {
                case WORK:
                    mainRoot.getStyleClass().add("work-mode");
                    statusLabel.setText("🚀 Đang tập trung làm việc...");
                    setUIState(true);
                    break;
                case REST:
                    mainRoot.getStyleClass().add("rest-mode");
                    statusLabel.setText("☕ Nghỉ ngơi một chút nào!");
                    setUIState(true);
                    break;
                case IDLE:
                    statusLabel.setText("Sẵn sàng tập trung?");
                    setUIState(false);
                    break;
            }
        });
    }

    private void onFinish(PomodoroState finishedState) {
        Platform.runLater(() -> {
            java.awt.Toolkit.getDefaultToolkit().beep();
            if (finishedState == PomodoroState.WORK) {
                statService.saveSession(workMinutes * 60);
                statusLabel.setText("Hết giờ làm! Hãy uống nước."); // Bỏ icon lỗi

                btnBreak.setVisible(true);
                btnBreak.setManaged(true);
                btnSkip.setVisible(true);
                btnSkip.setManaged(true);

                setUIState(false);
            } else {
                // Khi nghỉ xong, gọi stopAll để đưa App về trạng thái sẵn sàng làm việc tiếp
                stopAll();
                statusLabel.setText("Nghỉ xong rồi! Bắt đầu phiên mới nhé ?");
            }
        });
    }

    private void stopAll() {
        timer.stop();
        state = PomodoroState.IDLE;

        // Đảm bảo ẩn các nút phụ đi
        btnBreak.setVisible(false);
        btnBreak.setManaged(false);
        btnSkip.setVisible(false);
        btnSkip.setManaged(false);

        // Reset thời gian hiển thị về mức user chọn
        updateTimeLabel(durationChoice.getValue() * 60);

        // Gọi setUIState(false) để hiện lại nút BẮT ĐẦU và bảng chọn thời gian
        setUIState(false);
    }

    // Hàm thay đổi trạng thái giao diện
    private void setUIState(boolean isRunning) {
        // 1. Kiểm tra xem có đang trong trạng thái chờ người dùng chọn (Nghỉ/Bỏ qua) không
        boolean isSelectionMode = btnBreak.isVisible();

        // 2. Điều khiển nút Start/Stop
        btnStart.setVisible(!isSelectionMode);
        btnStart.setManaged(!isSelectionMode);
        btnStart.setText(isRunning ? "DỪNG" : "BẮT ĐẦU");

        // 3. Điều khiển bảng chọn thời gian (Config)
        boolean showConfig = !isRunning && !isSelectionMode && state == PomodoroState.IDLE;
        configBox.setVisible(showConfig);
        configBox.setManaged(showConfig);

        // 4. ĐIỀU KHIỂN NÚT LỊCH SỬ (btnStat)
        // Chỉ hiện nút lịch sử khi KHÔNG chạy và KHÔNG trong chế độ chọn nghỉ/bỏ qua
        if (btnStat != null) {
            boolean showStat = !isRunning && !isSelectionMode && state == PomodoroState.IDLE;
            btnStat.setVisible(showStat);
            btnStat.setManaged(showStat);
        }

        // 5. Nếu quay về trạng thái rảnh (IDLE), đảm bảo ẩn các nút phụ
        if (state == PomodoroState.IDLE) {
            btnBreak.setVisible(false);
            btnBreak.setManaged(false);
            btnSkip.setVisible(false);
            btnSkip.setManaged(false);
        }
    }

    private void updateTimeLabel(int totalSeconds) {
        int mins = totalSeconds / 60;
        int secs = totalSeconds % 60;
        timeLabel.setText(String.format("%02d:%02d", mins, secs));
    }

    private void showWaterPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nhắc nhở");
        alert.setHeaderText(null);
        alert.setContentText("💧 Đã đến lúc bổ sung nước cho cơ thể bạn ơi!");
        alert.showAndWait();
    }

    private void playAlarm() {
        try {

            String path = getClass().getResource("/alarm.mp3").toExternalForm();
            AudioClip alert = new AudioClip(path);
            alert.play();
        } catch (Exception e) {
            System.out.println("Không tìm thấy file chuông, dùng Beep mặc định.");
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }
    @FXML
    public void showStatistics() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/statistics.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Thống kê");
            stage.setScene(new javafx.scene.Scene(root, 750, 550));
            stage.show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}