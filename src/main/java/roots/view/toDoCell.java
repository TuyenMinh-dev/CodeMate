package roots.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import roots.entity.toDoList;
import roots.services.toDoService;
import roots.controllers.toDoController;
import java.util.function.Supplier;

public class toDoCell extends ListCell<toDoList> {
    private final toDoService todoService;
    private final Supplier<String> currentFilter;
    private final Runnable onDataChanged;
    private final toDoController controller;

    private final CheckBox checkBox = new CheckBox();
    private final Label lblTitle = new Label();
    private final TextField txtEdit = new TextField();
    private final Button btnPlay = createIconButton("M8 5v14l11-7z", "#38bdf8");
    private final Button btnEdit = createIconButton("M3 17.25V21h3.75L17.81 9.94l-3.75-3.75z", "#94a3b8");
    private final Button btnDelete = createIconButton("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z", "#ef4444");
    private final HBox container = new HBox(12);

    public toDoCell(toDoService todoService, Supplier<String> currentFilter, Runnable onDataChanged, toDoController controller) {
        this.todoService = todoService;
        this.currentFilter = currentFilter;
        this.onDataChanged = onDataChanged;
        this.controller = controller;
        initLayout();
        initEvents();
    }

    private void initLayout() { // Hàm fix lỗi Hình 5
        txtEdit.setVisible(false);
        txtEdit.setManaged(false);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox actionBox = new HBox(8, btnPlay, btnEdit, btnDelete);
        container.getChildren().addAll(checkBox, lblTitle, txtEdit, spacer, actionBox);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setStyle("-fx-padding: 8 12; -fx-background-color: #1e293b; -fx-background-radius: 8;");
    }

    private void initEvents() { // Hàm fix lỗi Hình 6
        btnPlay.setOnAction(e -> {
            toDoList item = getItem();
            if (item != null && !item.getSessionDurations().isEmpty()) {
                controller.onStartSession(item, item.getSessionDurations().get(0));
            }
        });
        checkBox.setOnAction(e -> {
            if (getItem() != null) {
                todoService.setCompleted(getItem(), checkBox.isSelected());
                onDataChanged.run();
            }
        });
        btnDelete.setOnAction(e -> {
            if (getItem() != null) {
                todoService.delete(getItem());
                getListView().getItems().remove(getItem());
                onDataChanged.run();
            }
        });
        btnEdit.setOnAction(e -> {
            lblTitle.setVisible(false); lblTitle.setManaged(false);
            txtEdit.setVisible(true); txtEdit.setManaged(true);
            txtEdit.setText(getItem().getTitle());
            txtEdit.requestFocus();
        });
        txtEdit.setOnAction(e -> {
            getItem().setTitle(txtEdit.getText());
            todoService.update(getItem());
            lblTitle.setText(txtEdit.getText());
            txtEdit.setVisible(false); txtEdit.setManaged(false);
            lblTitle.setVisible(true); lblTitle.setManaged(true);
        });
    }

    @Override
    protected void updateItem(toDoList item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) setGraphic(null);
        else {
            lblTitle.setText(item.getTitle());
            checkBox.setSelected(item.isCompleted());
            setGraphic(container);
        }
    }

    private Button createIconButton(String svg, String color) {
        SVGPath path = new SVGPath(); path.setContent(svg); path.setStyle("-fx-fill: " + color + ";");
        Button b = new Button(); b.setGraphic(path);
        b.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        return b;
    }
}