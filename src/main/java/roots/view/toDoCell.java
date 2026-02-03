package roots.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import roots.entity.toDoList;
import roots.services.toDoService;

import java.util.function.Supplier;

public class toDoCell extends ListCell<toDoList> {

    private final CheckBox checkBox = new CheckBox();
    private final Label lblTitle = new Label();
    private final TextField txtEdit = new TextField();

    private final Button btnEdit = createIconButton(editIcon());
    private final Button btnDelete = createIconButton(deleteIcon());
    private final Button btnMore = createIconButton(moreIcon());

    private final HBox actionBox = new HBox(2);
    private final HBox container = new HBox(8);

    private final toDoService todoService;
    private final Supplier<String> currentFilter;
    private final Runnable onDataChanged; // 🔥 CALLBACK

    public toDoCell(
            toDoService todoService,
            Supplier<String> currentFilter,
            Runnable onDataChanged
    ) {
        this.todoService = todoService;
        this.currentFilter = currentFilter;
        this.onDataChanged = onDataChanged;

        initLayout();
        initEvents();
    }

    /* ================= LAYOUT ================= */

    private void initLayout() {
        lblTitle.setStyle("-fx-font-size: 14px;");
        txtEdit.setVisible(false);
        txtEdit.setManaged(false);

        actionBox.getChildren().addAll(btnEdit, btnDelete, btnMore);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        container.getChildren().addAll(
                checkBox,
                lblTitle,
                txtEdit,
                spacer,
                actionBox
        );

        container.setAlignment(Pos.CENTER_LEFT);
        container.setStyle("""
            -fx-padding: 6 10;
            -fx-background-color: #f9f9f9;
            -fx-background-radius: 10;
        """);
    }

    /* ================= EVENTS ================= */

    private void initEvents() {

        checkBox.setOnAction(e -> {
            toDoList item = getItem();
            if (item != null) {
                todoService.setCompleted(item, checkBox.isSelected());
                onDataChanged.run(); // 🔥 UPDATE %
            }
        });

        btnEdit.setOnAction(e -> startTitleEdit());

        btnDelete.setOnAction(e -> {
            toDoList item = getItem();
            if (item != null) {
                todoService.delete(item);
                getListView().getItems().remove(item);
                onDataChanged.run(); // 🔥 UPDATE %
            }
        });

        txtEdit.focusedProperty().addListener((obs, o, n) -> {
            if (!n) saveEdit();
        });

        txtEdit.setOnAction(e -> saveEdit());
    }

    /* ================= EDIT ================= */

    private void startTitleEdit() {
        txtEdit.setText(lblTitle.getText());

        lblTitle.setVisible(false);
        lblTitle.setManaged(false);

        actionBox.setVisible(false);
        actionBox.setManaged(false);

        checkBox.setVisible(false);
        checkBox.setManaged(false);

        txtEdit.setVisible(true);
        txtEdit.setManaged(true);
        txtEdit.requestFocus();
        txtEdit.selectAll();
    }

    private void saveEdit() {
        toDoList item = getItem();
        if (item == null) return;

        String newTitle = txtEdit.getText().trim();
        if (!newTitle.isEmpty()) {
            item.setTitle(newTitle);
            todoService.update(item);
            lblTitle.setText(newTitle);
        }

        txtEdit.setVisible(false);
        txtEdit.setManaged(false);

        lblTitle.setVisible(true);
        lblTitle.setManaged(true);

        boolean isAll = "ALL".equals(currentFilter.get());

        actionBox.setVisible(isAll);
        actionBox.setManaged(isAll);

        checkBox.setVisible(isAll);
        checkBox.setManaged(isAll);

        onDataChanged.run(); // 🔥 UPDATE %
    }

    /* ================= UPDATE ================= */

    @Override
    protected void updateItem(toDoList item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
            return;
        }

        txtEdit.setVisible(false);
        txtEdit.setManaged(false);
        lblTitle.setVisible(true);
        lblTitle.setManaged(true);

        lblTitle.setText(item.getTitle());
        checkBox.setSelected(item.isCompleted());

        boolean isAll = "ALL".equals(currentFilter.get());

        checkBox.setVisible(isAll);
        checkBox.setManaged(isAll);
        actionBox.setVisible(isAll);
        actionBox.setManaged(isAll);

        if (item.isCompleted() && isAll) {
            lblTitle.setStyle("-fx-text-fill: #9e9e9e; -fx-strikethrough: true;");
        } else {
            lblTitle.setStyle("-fx-text-fill: black; -fx-strikethrough: false;");
        }

        setGraphic(container);
    }

    /* ================= ICONS ================= */

    private Button createIconButton(SVGPath icon) {
        icon.setScaleX(0.7);
        icon.setScaleY(0.7);
        icon.setStyle("-fx-fill: #666;");

        Button btn = new Button();
        btn.setGraphic(icon);
        btn.setPrefSize(20, 20);
        btn.setMinSize(20, 20);
        btn.setMaxSize(20, 20);
        btn.setStyle("""
            -fx-background-color: transparent;
            -fx-background-radius: 6;
            -fx-cursor: hand;
        """);
        return btn;
    }

    private SVGPath editIcon() {
        SVGPath p = new SVGPath();
        p.setContent("M3 17.25V21h3.75L17.81 9.94l-3.75-3.75z");
        return p;
    }

    private SVGPath deleteIcon() {
        SVGPath p = new SVGPath();
        p.setContent("M6 7h12v2H6z M8 9h8v10H8z M9 4h6l1 1h3v2H5V5h3z");
        return p;
    }

    private SVGPath moreIcon() {
        SVGPath p = new SVGPath();
        p.setContent(
                "M12 8a2 2 0 1 0 0-4 " +
                        "2 2 0 0 0 0 4zm0 6a2 2 0 1 0 0-4 " +
                        "2 2 0 0 0 0 4zm0 6a2 2 0 1 0 0-4 " +
                        "2 2 0 0 0 0 4z"
        );
        return p;
    }
}