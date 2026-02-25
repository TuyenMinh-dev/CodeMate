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
import java.util.List;
import java.util.ResourceBundle;

public class toDoController implements Initializable {

    private final toDoService todoService = new toDoService();
    private final ObservableList<toDoList> allTodos = FXCollections.observableArrayList();
    private String currentFilter = "ALL";

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
    private void handleAdd() {
        toDoList todo = todoService.addTodo(txtTitle.getText());
        if (todo == null) return;

        allTodos.add(todo);
        txtTitle.clear();
        updateProgress();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listTodo.setCellFactory(list -> new toDoCell(todoService, () -> currentFilter, this::updateProgress));

        // Đảm bảo lúc mới vào chỉ hiện startDayBox
        mainTodoBox.setVisible(false);
        mainTodoBox.setManaged(false);
        startDayBox.setVisible(true);
        startDayBox.setManaged(true);
    }

    @FXML
    private void handleStartDay() {
        // 1. Quét toàn bộ task chưa xong từ quá khứ và đẩy về hôm nay
        toDoListDao.carryOverPendingTasks(LocalDate.now());

        // 2. Chuyển đổi giao diện (Như cũ)
        startDayBox.setVisible(false);
        startDayBox.setManaged(false);
        mainTodoBox.setVisible(true);
        mainTodoBox.setManaged(true);

        // 3. Load dữ liệu (Lúc này allTodos sẽ bao gồm cả việc mới của hôm nay + việc cũ vừa được đẩy sang)
        allTodos.clear();
        allTodos.addAll(toDoListDao.findByDate(LocalDate.now()));

        // 4. Cập nhật giao diện (Như cũ)
        listTodo.setItems(allTodos);
        updateProgress();
    }

    // ================= PROGRESS =================

    private void updateProgress() {
        if (allTodos.isEmpty()) {
            progressBar.setProgress(0);
            lblProgress.setText("0%");
            return;
        }

        long doneCount = allTodos.stream()
                .filter(toDoList::isCompleted)
                .count();

        double progress = (double) doneCount / allTodos.size();
        progressBar.setProgress(progress);

        int percent = (int) (progress * 100);
        lblProgress.setText(percent + "% (" + doneCount + "/" + allTodos.size() + ")");
    }
    @FXML
    private void handleEndDay() {
        long totalTasks = allTodos.size();
        List<toDoList> pendingTasks = allTodos.stream()
                .filter(t -> !t.isCompleted())
                .toList();
        long completedCount = totalTasks - pendingTasks.size();

        double completionRate = (totalTasks == 0) ? 0 : (double) completedCount / totalTasks;
        int percent = (int) (completionRate * 100);


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Tổng kết ngày làm việc");


        alert.setHeaderText("Hôm nay bạn đã hoàn thành " + percent + "% công việc.");

        if (pendingTasks.isEmpty() && totalTasks > 0) {
            alert.setContentText("Tuyệt vời! Bạn đã dọn dẹp sạch sẽ danh sách việc cần làm.");
        } else if (totalTasks == 0) {
            alert.setContentText("Hôm nay bạn chưa có công việc nào để tổng kết.");
        } else {
            alert.setContentText("Bạn vẫn còn " + pendingTasks.size() + " việc chưa xong. Bạn muốn làm gì?");
        }

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

    // Hàm hiện thông báo khích lệ so sánh với hôm qua
    private void showMotivationAlert() {
        Alert motivation = new Alert(Alert.AlertType.INFORMATION);
        motivation.setTitle("CodeMate Coach");
        motivation.setHeaderText("🚀 Lời nhắn nhủ");
        motivation.setContentText("Hệ thống ghi nhận bạn đã rất nỗ lực. Hãy nghỉ ngơi và sẵn sàng cho ngày mai nhé!");
        motivation.show();
    }
}