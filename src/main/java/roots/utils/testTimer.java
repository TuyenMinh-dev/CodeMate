package roots.utils;

import javafx.application.Platform;
import roots.services.PomodoroTimer;

public class testTimer {

    public static void main(String[] args) {

        // 🔥 KHỞI ĐỘNG JAVAFX RUNTIME
        Platform.startup(() -> {

            PomodoroTimer timer = new PomodoroTimer();

            timer.setOnTick(seconds -> {
                System.out.println("Remaining: " + seconds + "s");
            });

            timer.setOnFinish(() -> {
                System.out.println("=== FINISHED ===");
                Platform.exit(); // thoát JavaFX cho gọn
            });

            timer.startSeconds(5);
        });
    }
}
