package roots.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/todo.fxml")
            );

            Scene scene = new Scene(loader.load());
            stage.setTitle("Todo List");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace(); // BẮT BUỘC
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

