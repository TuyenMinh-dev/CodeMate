package roots.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_layout.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

            primaryStage.setTitle("Roots - Hệ thống quản lý năng suất");
            primaryStage.setScene(scene);

            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(650);

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khởi tạo App: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
