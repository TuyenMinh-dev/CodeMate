package roots.controllers;

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

public class toDoController implements Initializable{

    private toDoService todoService = new toDoService();
    private ObservableList<toDoList> allTodos = FXCollections.observableArrayList();
    private String currentFilter = "ALL";


    @FXML
    private TextField txtTitle;

    @FXML
    private ListView<toDoList> listTodo;

    @FXML
    private void handleAdd() {
        toDoList todo = todoService.addTodo(txtTitle.getText());
        if (todo == null) return;

        allTodos.add(todo);   //  add vào master list
        txtTitle.clear();
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
        listTodo.setItems(allTodos.filtered(todo -> !todo.isCompleted()));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allTodos.addAll(todoService.getAllTodos());

        listTodo.setItems(allTodos); //  gắn trực tiếp
        listTodo.setCellFactory(list ->
                new toDoCell(todoService, () -> currentFilter)
        );


    }



}
