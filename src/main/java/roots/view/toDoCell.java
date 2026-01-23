package roots.view;

import roots.entity.toDoList;
import roots.services.toDoService;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.function.Supplier;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class toDoCell extends ListCell<toDoList> {

    private final CheckBox checkBox = new CheckBox();
    private final Label lblTitle = new Label();
    private final Button btnDelete = new Button("🗑");
    private final HBox container = new HBox(10);

    private final toDoService todoService;
    private final Supplier<String> currentFilter;

    public toDoCell(toDoService todoService, Supplier<String> currentFilter) {
        this.todoService = todoService;
        this.currentFilter = currentFilter;

        initLayout();
        initEvents();
    }

    private void initLayout() {
        container.getChildren().addAll(checkBox, lblTitle, btnDelete);
        container.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(lblTitle, Priority.ALWAYS);

        container.setStyle(NORMAL_STYLE);
        container.setOnMouseEntered(e -> {
            if (!container.isDisabled())
                container.setStyle(HOVER_STYLE);
        });

        container.setOnMouseExited(e -> {
            if (!container.isDisabled())
                container.setStyle(NORMAL_STYLE);
        });

        lblTitle.setStyle("-fx-font-size: 14px;");

        btnDelete.setPrefSize(36, 36);
        btnDelete.setStyle("""
                    -fx-background-color: transparent;
                    -fx-font-size: 16px;
                    -fx-text-fill: #666;
                    -fx-cursor: hand;
                """);

    }

    private void initEvents() {
        checkBox.setOnAction(e -> {
            toDoList current = getItem();
            if (current == null) return;

            todoService.setCompleted(current, checkBox.isSelected());
            playToggleAnimation();
        });


        btnDelete.setOnAction(e -> {
            toDoList current = getItem();
            if (current == null) return;

            todoService.delete(current);
            getListView().getItems().remove(current);
        });
    }

    @Override
    protected void updateItem(toDoList item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
            return;
        }

        lblTitle.setText(item.getTitle());
        checkBox.setSelected(item.isCompleted());

        // ===== style completed =====
        boolean isAll = "ALL".equals(currentFilter.get());

        if (item.isCompleted() && isAll) {
            // chỉ mờ + gạch khi đang ở ALL
            lblTitle.setStyle("-fx-text-fill: #9e9e9e; -fx-strikethrough: true;");
            container.setOpacity(0.6);
        } else {
            // DONE hoặc UNDONE → hiển thị bình thường
            lblTitle.setStyle("-fx-text-fill: black; -fx-strikethrough: false;");
            container.setOpacity(1.0);
        }


        // ===== FILTER LOGIC (QUAN TRỌNG) =====

        checkBox.setVisible(isAll);
        checkBox.setManaged(isAll);

        btnDelete.setVisible(isAll);
        btnDelete.setManaged(isAll);

        setGraphic(container);
    }

    private void playToggleAnimation() {
        FadeTransition fade = new FadeTransition(Duration.millis(150), container);
        fade.setFromValue(0.7);
        fade.setToValue(1.0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(150), container);
        scale.setFromX(0.97);
        scale.setFromY(0.97);
        scale.setToX(1.0);
        scale.setToY(1.0);

        fade.play();
        scale.play();
    }

    private static final String NORMAL_STYLE = """
                -fx-padding: 8 10;
                -fx-background-color: #f9f9f9;
                -fx-background-radius: 8;
            """;

    private static final String HOVER_STYLE = """
                -fx-padding: 8 10;
                -fx-background-color: #eef4ff;
                -fx-background-radius: 8;
            """;


}
