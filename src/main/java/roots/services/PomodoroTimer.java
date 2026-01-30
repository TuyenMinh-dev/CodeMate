package roots.services;

import roots.entity.PomodoroState;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class PomodoroTimer {

    private Timer timer;
    private int secondsLeft;

    private Consumer<Integer> onTick;
    private Consumer<PomodoroState> onStateChange;
    private Consumer<PomodoroState> onFinish;

    // callback setter
    public void onTick(Consumer<Integer> callback) {
        this.onTick = callback;
    }

    public void onStateChange(Consumer<PomodoroState> callback) {
        this.onStateChange = callback;
    }

    public void onFinish(Consumer<PomodoroState> callback) {
        this.onFinish = callback;
    }

    // public API
    public void startWork(int seconds) {
        start(seconds, PomodoroState.WORK);
    }

    public void startRest(int seconds) {
        start(seconds, PomodoroState.REST);
    }

    private void start(int seconds, PomodoroState state) {
        stop();

        this.secondsLeft = seconds;

        if (onStateChange != null) {
            onStateChange.accept(state);
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsLeft--;

                if (onTick != null) {
                    onTick.accept(secondsLeft);
                }

                if (secondsLeft <= 0) {
                    stop();
                    if (onFinish != null) {
                        onFinish.accept(state);
                    }
                }
            }
        }, 1000, 1000);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
