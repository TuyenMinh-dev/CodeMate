package roots.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PomodoroView extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/pomodoro.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Pomodoro Timer");
        stage.setScene(scene);
        stage.show();

        scene.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm()
        );
    }

    public static void main(String[] args) {
        launch();
    }
}

