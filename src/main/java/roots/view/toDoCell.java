package roots.view;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import roots.entity.toDoList;
import roots.services.toDoService;

public class toDoCell extends ListCell<toDoList> {

    private CheckBox checkBox = new CheckBox();
    private Label lblTitle = new Label();
    private Button btnDelete = new Button("🗑");
    private toDoService todoService;
    private ObservableList<toDoList> todoData;

    public toDoCell(toDoService todoService,
                    ObservableList<toDoList> todoData) {
        this.todoService = todoService;
        this.todoData = todoData;

        container.getChildren().addAll(checkBox, lblTitle, btnDelete);
        HBox.setHgrow(lblTitle, Priority.ALWAYS);

        btnDelete.setStyle("-fx-background-color: transparent;");

        checkBox.setOnAction(e -> {
            toDoList current = getItem();
            if (current == null) return;

            boolean newValue = checkBox.isSelected();

            todoService.setCompleted(current, newValue);
            current.setCompleted(newValue);

            updateItem(current, false);
        });
        btnDelete.setOnAction(e -> {
            toDoList current = getItem();
            if (current == null) return;

            todoService.delete(current);
            getListView().getItems().remove(current);
        });
        container.setStyle("""
                    -fx-padding: 6 10 6 10;
                    -fx-alignment: CENTER_LEFT;
                """);

        lblTitle.setStyle("-fx-font-size: 14px;");


    }


    private HBox container = new HBox(10);


    @Override
    protected void updateItem(toDoList item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        } else {
            lblTitle.setText(item.getTitle());
            checkBox.setSelected(item.isCompleted());

            if (item.isCompleted()) {
                lblTitle.setStyle(
                        "-fx-text-fill: gray; -fx-strikethrough: true;"
                );
            } else {
                lblTitle.setStyle(
                        "-fx-text-fill: black; -fx-strikethrough: false;"
                );
            }

            setGraphic(container);
        }
    }
}
