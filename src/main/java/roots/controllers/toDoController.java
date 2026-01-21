package roots.controllers;

import roots.entity.toDoList;
import roots.services.toDoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class toDoController implements Initializable{
    private ObservableList<toDoList> todoData = FXCollections.observableArrayList();
    private toDoService todoService = new toDoService();


    @FXML
    private TextField txtTitle;

    @FXML
    private ListView<toDoList> listTodo;

    @FXML
    private void handleAdd() {
        toDoList todo = todoService.addTodo(txtTitle.getText());
        if(todo == null){
            return;
        }
        todoData.add(todo);   //add vào danh sách hiển thị
        txtTitle.clear();
    }
    @FXML
    private void handleDelete() {
        toDoList selected = listTodo.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        boolean success =todoService.deleteTodo(selected);
        if(!success){
            return;
        }

        //  xóa trong danh sách hiển thị
        todoData.remove(selected);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //1.lấy dữ liệu từ database
        todoData.addAll(todoService.getAllTodos());
        //2.gắn dữ liệu cho listview
        listTodo.setItems(todoData);
    }

}
