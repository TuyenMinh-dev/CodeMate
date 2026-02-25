package roots.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import roots.dao.toDoListDao;
import roots.entity.PomodoroState;
import roots.entity.toDoList;
import roots.services.PomodoroTimer;
import roots.services.StatService;
import roots.services.toDoService;

import java.time.LocalDate;

public class PomodoroController {

    @FXML
    private Label timeLabel, statusLabel, currentTaskLabel;
    @FXML
    private ChoiceBox<Integer> durationChoice;
    @FXML
    private Button btnStart, btnBreak, btnSkip, btnStat;
    @FXML
    private HBox configBox;
    @FXML
    private StackPane mainRoot;
    @FXML
    private ListView<toDoList> pomoTaskList;

    private PomodoroState state = PomodoroState.IDLE;
    private final PomodoroTimer timer = new PomodoroTimer();
    private final StatService statService = new StatService();
    private final toDoService todoService = new toDoService();
    private final ObservableList<toDoList> dailyTasks = FXCollections.observableArrayList();
    private toDoList selectedTask;

    private int workMinutes = 30;
    private int continuousWorkSeconds = 0;
    private final int WATER_REMINDER_THRESHOLD = 3600;

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

        durationChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (state == PomodoroState.IDLE && newVal != null) updateTimeLabel(newVal * 60);
        });

        // Load Task lên Sidebar
        refreshTaskList();

        // Bắt sự kiện chọn Task
        pomoTaskList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedTask = newVal;
                currentTaskLabel.setText("🎯 Đang làm: " + newVal.getTitle());
            }
        });
    }


    public void refreshTaskList() {
        dailyTasks.clear();
        dailyTasks.addAll(toDoListDao.findByDate(LocalDate.now()));
        pomoTaskList.setItems(dailyTasks);
    }

    @FXML
    public void handleStart() {
        if (state == PomodoroState.IDLE) {
            if (selectedTask == null) {
                workMinutes = durationChoice.getValue();
            }
            timer.startWork(workMinutes * 60);
        } else {
            stopAll();
        }
    }

    @FXML
    public void handleBreak() {
        showWaterPopup();
        continuousWorkSeconds = 0;
        timer.startRest(5 * 60);
    }

    @FXML
    public void handleSkip() {
        if (continuousWorkSeconds >= 3600) showWaterPopup();
        timer.startWork(workMinutes * 60);
    }

    private void onTick(int secondsLeft) {
        Platform.runLater(() -> {
            updateTimeLabel(secondsLeft);
            if (state == PomodoroState.WORK) continuousWorkSeconds++;
        });
    }

    private void onStateChange(PomodoroState newState) {
        this.state = newState;
        Platform.runLater(() -> {
            mainRoot.getStyleClass().removeAll("work-mode", "rest-mode");
            switch (newState) {
                case WORK:
                    mainRoot.getStyleClass().add("work-mode");
                    statusLabel.setText("🚀 Tập trung cao độ!");
                    setUIState(true);
                    break;
                case REST:
                    mainRoot.getStyleClass().add("rest-mode");
                    statusLabel.setText("☕ Nghỉ ngơi tí nào.");
                    setUIState(true);
                    break;
                case IDLE:
                    statusLabel.setText("Sẵn sàng chưa?");
                    setUIState(false);
                    break;
            }
        });
    }

    private void onFinish(PomodoroState finishedState) {
        Platform.runLater(() -> {
            if (finishedState == PomodoroState.WORK) {
                // CỘNG PHIÊN CHO TASK
                if (selectedTask != null) {
                    todoService.incrementActualPomo(selectedTask);
                    refreshTaskList();
                }
                statService.saveSession(workMinutes * 60);
                btnBreak.setVisible(true);
                btnBreak.setManaged(true);
                btnSkip.setVisible(true);
                btnSkip.setManaged(true);
                setUIState(false);
            } else {
                stopAll();
            }
            playAlarm();
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
        configBox.setVisible(!isRunning && !isSelectionMode);
        configBox.setManaged(!isRunning && !isSelectionMode);
    }

    private void updateTimeLabel(int totalSeconds) {
        timeLabel.setText(String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60));
    }

    private void showWaterPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nhắc nhở");
        alert.setContentText("💧 Uống nước đi Tuyên ơi!");
        alert.showAndWait();
    }

    private void playAlarm() {
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    public void setTimer(toDoList task, int duration) {
        this.selectedTask = task;
        this.workMinutes = duration;

        Platform.runLater(() -> {
            currentTaskLabel.setText("🎯 Đang làm: " + task.getTitle());
            updateTimeLabel(duration * 60);
            statusLabel.setText("Sẵn sàng cho phiên " + duration + " phút?");

            // Tự động chuyển trạng thái UI
            stopAll();
        });
    }
}