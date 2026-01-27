package roots.services;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Consumer;

public class PomodoroTimer {


        private Timeline timeline;
        private int remainingSeconds;

        // callback mỗi giây
        private Consumer<Integer> onTick;

        // callback khi hết giờ
        private Runnable onFinish;

    /* =========================
       PUBLIC API
       ========================= */

        // bắt đầu đếm với số phút
        public void start(int minutes) {
            stop(); // đảm bảo không có timer cũ

            remainingSeconds = minutes * 60;

            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), e -> tick())
            );
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }

        // dừng timer
        public void stop() {
            if (timeline != null) {
                timeline.stop();
                timeline = null;
            }
        }

        // đăng ký callback tick
        public void setOnTick(Consumer<Integer> onTick) {
            this.onTick = onTick;
        }

        // đăng ký callback finish
        public void setOnFinish(Runnable onFinish) {
            this.onFinish = onFinish;
        }

    /* =========================
       INTERNAL LOGIC
       ========================= */

        private void tick() {
            remainingSeconds--;

            if (onTick != null) {
                onTick.accept(remainingSeconds);
            }

            if (remainingSeconds <= 0) {
                stop();
                if (onFinish != null) {
                    onFinish.run();
                }
            }
        }
    public void startSeconds(int seconds) {
        stop();
        remainingSeconds = seconds;

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> tick())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

}


