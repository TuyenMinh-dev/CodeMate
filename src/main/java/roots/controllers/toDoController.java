package roots.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import roots.entity.toDoList;
import roots.services.toDoService;
import roots.view.toDoCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;

import java.net.URL;
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
        allTodos.addAll(todoService.getAllTodos());
        listTodo.setItems(allTodos);


        listTodo.setCellFactory(list ->
                new toDoCell(
                        todoService,
                        () -> currentFilter,
                        this::updateProgress
                )
        );

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
}