package roots.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import roots.dao.toDoListDao;
import java.time.LocalDate;

public class MainController {
    @FXML private StackPane rootStack;
    @FXML private VBox welcomeOverlay;
    @FXML private BorderPane mainContainer;
    @FXML private TabPane mainTabPane;

    // Inject các Controller con từ FXML (Lưu ý: fx:id trong FXML phải khớp với tên biến + "Controller")
    @FXML private toDoController todoTabContentController;
    @FXML private PomodoroController pomoTabContentController;

    @FXML
    public void initialize() {
        mainContainer.setVisible(false);
        welcomeOverlay.setVisible(true);

        // QUAN TRỌNG: Kết nối MainController với toDoController để nút Play hoạt động
        if (todoTabContentController != null) {
            todoTabContentController.setMainController(this);
        }
    }

    // Hàm này để fix lỗi "Cannot resolve method" ở toDoController của m
    public PomodoroController getPomoTabContentController() {
        return pomoTabContentController;
    }

    @FXML
    private void handleGlobalStartDay() {
        toDoListDao.carryOverPendingTasks(LocalDate.now());
        welcomeOverlay.setVisible(false);
        mainContainer.setVisible(true);

        if (todoTabContentController != null) {
            todoTabContentController.loadData();
        }
        if (pomoTabContentController != null) {
            pomoTabContentController.refreshTaskList();
        }
    }

    @FXML
    private void handleGlobalEndDay() {
        if (todoTabContentController != null) {
            todoTabContentController.handleEndDay();
        }
    }

    public void backToWelcome() {
        mainContainer.setVisible(false);
        welcomeOverlay.setVisible(true);
    }

    @FXML
    void showTodoTab() {
        mainTabPane.getSelectionModel().select(0);
    }

    @FXML
    void showPomoTab() {
        mainTabPane.getSelectionModel().select(1);
        if (pomoTabContentController != null) {
            pomoTabContentController.refreshTaskList();
        }
    }

    @FXML
    void showStatTab() {
        mainTabPane.getSelectionModel().select(2);
    }
}