package roots.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import roots.entity.PomodoroState;
import roots.models.User;
import roots.models.UserSession;
import roots.services.PomodoroTimer;
import roots.services.StatService;
import roots.services.WaterService;
import roots.utils.ChangeFXML;

public class PomodoroController {

    @FXML private Label timeLabel;
    @FXML private Label statusLabel;
    @FXML private ChoiceBox<Integer> durationChoice;
    @FXML private Button btnStart, btnBreak, btnSkip;
    @FXML private HBox configBox;
    @FXML private StackPane mainRoot;
    @FXML private Button btnStat;

    private PomodoroState state;
    private final PomodoroTimer timer = PomodoroTimer.getInstance();
    private final StatService statService = new StatService();
    private final WaterService waterService = new WaterService();

    private int workMinutes = 30;
    private int continuousWorkSeconds = 0;
    private final int WATER_REMINDER_THRESHOLD = 3600;

    private User currentUser = UserSession.getCurrentUser();

    public PomodoroController() {}

    @FXML
    public void comeBackPomodoro(MouseEvent event){
        ChangeFXML.changeFXML(event, "/view/home.fxml");
    }

    @FXML
    public void initialize() {
        // Đăng ký các sự kiện từ Timer Singleton
        timer.onTick(this::onTick);
        timer.onStateChange(this::onStateChange);
        timer.onFinish(this::onFinish);

        durationChoice.getItems().addAll(25, 30, 35);
        durationChoice.setValue(30);

        this.state = timer.getCurrentState();
        if (state != PomodoroState.IDLE) {
            updateTimeLabel(timer.getSecondsLeft());
            onStateChange(state);
        } else {
            updateTimeLabel(durationChoice.getValue() * 60);
        }

        durationChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (state == PomodoroState.IDLE && newVal != null) {
                updateTimeLabel(newVal * 60);
            }
        });
    }

    @FXML
    public void handleStart() {
        this.state = timer.getCurrentState();
        if (state == PomodoroState.IDLE) {
            int selectedMinutes = durationChoice.getValue();
            // TEST: Set 10 giây làm việc
            timer.startWork(10);
        } else {
            stopAll();
        }
    }

    @FXML
    public void handleBreak() {
        showWaterPopup();
        continuousWorkSeconds = 0;
        // TEST: Set 5 giây nghỉ
        timer.startRest(5);
    }

    @FXML
    public void handleSkip() {
        if (continuousWorkSeconds >= 15) {
            showWaterPopup();
            continuousWorkSeconds = 0;
        }
        // TEST: Set 10 giây làm việc
        int selectedMinutes = durationChoice.getValue();
        timer.startWork(10);//selectedMinutes*60
    }

    private void onTick(int secondsLeft) {
        Platform.runLater(() -> {
            updateTimeLabel(secondsLeft);
            this.state = timer.getCurrentState();
            if (state == PomodoroState.WORK) {
                continuousWorkSeconds++;
            }
        });
    }

    private void onStateChange(PomodoroState newState) {
        this.state = newState;
        Platform.runLater(() -> {
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
            playAlarm();
            if (finishedState == PomodoroState.WORK) {
                int selectedMinutes = durationChoice.getValue();

                long secondsToSave = (long) selectedMinutes * 60;

                statService.saveSession(secondsToSave);

                statusLabel.setText("Bạn đã làm rất tốt! Nghỉ ngơi chút chứ.");
                btnBreak.setVisible(true);
                btnBreak.setManaged(true);
                btnSkip.setVisible(true);
                btnSkip.setManaged(true);
                setUIState(false);
            } else {
                stopAll();
                statusLabel.setText("Quay lại công việc thôi nào!");
            }
        });
    }

    private void stopAll() {
        timer.stop();
        state = PomodoroState.IDLE;
        btnBreak.setVisible(false);
        btnBreak.setManaged(false);
        btnSkip.setVisible(false);
        btnSkip.setManaged(false);
        updateTimeLabel(durationChoice.getValue() * 60);
        setUIState(false);
    }

    private void setUIState(boolean isRunning) {
        boolean isSelectionMode = btnBreak.isVisible();
        btnStart.setVisible(!isSelectionMode);
        btnStart.setManaged(!isSelectionMode);
        btnStart.setText(isRunning ? "DỪNG" : "BẮT ĐẦU");

        boolean showConfig = !isRunning && !isSelectionMode && state == PomodoroState.IDLE;
        configBox.setVisible(showConfig);
        configBox.setManaged(showConfig);
    }

    private void updateTimeLabel(int totalSeconds) {
        int mins = totalSeconds / 60;
        int secs = totalSeconds % 60;
        timeLabel.setText(String.format("%02d:%02d", mins, secs));
    }

    private void showWaterPopup() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Nhắc nhở uống nước");
        alert.setHeaderText("💧 Đã đến lúc bổ sung nước rồi!");
        alert.setContentText("Bạn có muốn uống một ly nước (250ml) không?");

        ButtonType btnYes = new ButtonType("Đã uống");
        ButtonType btnNo = new ButtonType("Để sau", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnYes, btnNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == btnYes) {
                waterService.addWater(250, currentUser);
                System.out.println("Xác nhận đã uống 250ml nước");
            }
        });
    }

    private void playAlarm() {
        try {
            java.awt.Toolkit.getDefaultToolkit().beep();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void showStatistics() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/view/statistics.fxml"));
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