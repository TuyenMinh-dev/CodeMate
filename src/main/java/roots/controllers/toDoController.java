package roots.controllers;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import roots.dao.toDoListDao;
import roots.entity.toDoList;
import roots.services.toDoService;
import roots.view.toDoCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class toDoController implements Initializable {

    private final toDoService todoService = new toDoService();
    private final ObservableList<toDoList> allTodos = FXCollections.observableArrayList();
    private String currentFilter = "ALL";
    private MainController mainController;
    @FXML private ListView<toDoList> todoListView;


    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label lblProgress;

    @FXML
    private TextField txtTitle;

    @FXML
    private ListView<toDoList> listTodo;

    @FXML
    private VBox startDayBox;
    @FXML
    private VBox mainTodoBox;

    @FXML
    private VBox vboxCelebration;
    @FXML
    private Label lblFinishedMessage;

    @FXML
    private void handleAdd() {
        String title = txtTitle.getText();
        if (title.isEmpty()) return;

        List<Integer> durations = new ArrayList<>();
        if ("Khác...".equals(cbPomoEstimate.getValue())) {
            try {
                durations = Arrays.stream(txtCustomPomo.getText().split(","))
                        .map(s -> Integer.parseInt(s.trim()))
                        .collect(java.util.stream.Collectors.toList());
            } catch (Exception e) {
                durations = List.of(25);
            }
        } else {
            int count = Integer.parseInt(cbPomoEstimate.getValue());
            for (int i = 0; i < count; i++) durations.add(30);
        }

        // Gửi List durations đi
        toDoList todo = todoService.addTodo(title, durations);

        if (todo != null) {
            allTodos.add(todo);
            txtTitle.clear();
            txtCustomPomo.clear();
            updateProgress();
        }
    }

    @FXML
    private void filterAll() {
        currentFilter = "ALL";
        listTodo.setItems(allTodos);
    }

    @FXML
    private void filterDone() {
        currentFilter = "DONE";
        listTodo.setItems(allTodos.filtered(toDoList::isCompleted));
    }

    @FXML
    private void filterUndone() {
        currentFilter = "UNDONE";
        listTodo.setItems(allTodos.filtered(t -> !t.isCompleted()));
    }

    @FXML
    private ComboBox<String> cbPomoEstimate;
    @FXML
    private TextField txtCustomPomo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        todoListView.setCellFactory(lv -> new toDoCell(todoService, () -> currentFilter, this::updateProgress, this));
        cbPomoEstimate.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "Khác..."));
        cbPomoEstimate.getSelectionModel().selectFirst();

        cbPomoEstimate.setOnAction(e -> {
            boolean isCustom = "Khác...".equals(cbPomoEstimate.getValue());
            txtCustomPomo.setVisible(isCustom);
            txtCustomPomo.setManaged(isCustom);
        });
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    public void loadData() {
        allTodos.clear();
        allTodos.addAll(toDoListDao.findByDate(LocalDate.now()));
        listTodo.setItems(allTodos);
        updateProgress();
    }

    // ================= PROGRESS =================

    private void updateProgress() {
        if (allTodos.isEmpty()) {
            progressBar.setProgress(0);
            lblProgress.setText("0% (0/0)");
            // Hiện thông báo nếu danh sách trống hoàn toàn
            showCelebration(false);
            return;
        }

        long doneCount = allTodos.stream().filter(toDoList::isCompleted).count();
        double progress = (double) doneCount / allTodos.size();

        progressBar.setProgress(progress);
        lblProgress.setText((int) (progress * 100) + "% (" + doneCount + "/" + allTodos.size() + ")");

    }

    private void showCelebration(boolean isFinished) {
        if (isFinished) {
            listTodo.setVisible(false); // Ẩn danh sách việc đi
            vboxCelebration.setVisible(true); // Hiện màn hình pháo hoa
            lblFinishedMessage.setText("🌟 Thật là một ngày làm việc năng suất !");
        } else {
            listTodo.setVisible(true);
            vboxCelebration.setVisible(false);
        }
    }

    @FXML
    public void handleEndDay() {
        long totalTasks = allTodos.size();
        if (totalTasks == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Hôm nay m chưa thêm việc gì mà đã đòi kết thúc rồi à? Chiến đi chứ!");
            alert.showAndWait();
            return;
        }

        List<toDoList> pendingTasks = allTodos.stream()
                .filter(t -> !t.isCompleted())
                .toList();
        long completedCount = totalTasks - pendingTasks.size();
        int percent = (int) ((double) completedCount / totalTasks * 100);

        // TRƯỜNG HỢP 1: HOÀN THÀNH 100%
        if (pendingTasks.isEmpty()) {
            showCelebration(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION); // Dùng INFORMATION để chỉ có nút OK
            alert.setTitle("Hoàn thành xuất sắc!");
            alert.setHeaderText("Hôm nay m đã hoàn thành 100% công việc!");
            alert.setContentText("Tuyệt vời! Một ngày làm việc cực kỳ năng suất. Nghỉ ngơi thôi Tuyên ơi!");

            alert.showAndWait();


        }
        // TRƯỜNG HỢP 2: VẪN CÒN VIỆC (DƯỚI 100%)
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Tổng kết ngày làm việc");
            alert.setHeaderText("Hôm nay bạn mới hoàn thành " + percent + "% công việc.");
            alert.setContentText("Bạn vẫn còn " + pendingTasks.size() + " việc chưa xong. M tính sao?");

            ButtonType btnTomorrow = new ButtonType("Chuyển sang mai");
            ButtonType btnKeep = new ButtonType("Để lại hôm nay");
            ButtonType btnCancel = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(btnTomorrow, btnKeep, btnCancel);

            alert.showAndWait().ifPresent(response -> {
                if (response == btnTomorrow) {
                    for (toDoList t : pendingTasks) {
                        t.setCreatedAt(LocalDate.now().plusDays(1));
                        todoService.update(t);
                    }
                    allTodos.removeAll(pendingTasks);
                    updateProgress();
                    showMotivationAlert();
                }
            });
        }
    }

    private void showMotivationAlert() {
        Alert motivation = new Alert(Alert.AlertType.INFORMATION);
        motivation.setTitle("CodeMate Coach");
        motivation.setHeaderText("🚀 Lời nhắn nhủ");
        motivation.setContentText("Hệ thống ghi nhận bạn đã rất nỗ lực. Hãy nghỉ ngơi và sẵn sàng cho ngày mai nhé!");
        motivation.show();
    }

    public void onStartSession(toDoList task, Integer duration) {
        if (this.mainController != null) {
            this.mainController.showPomoTab();
            if (this.mainController.getPomoTabContentController() != null) {
                this.mainController.getPomoTabContentController().setTimer(task, duration);
            }
        }
    }
}