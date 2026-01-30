package roots.controllers;

import roots.entity.PomodoroState;
import roots.services.PomodoroTimer;

public class PomodoroController {

    private PomodoroState state = PomodoroState.IDLE;
    private final PomodoroTimer timer = new PomodoroTimer();

    private int workMinutes = 30;
    private final int breakMinutes = 5;

    private int continuousWorkSeconds = 0;

    public PomodoroController() {
        timer.onTick(this::onTick);
        timer.onStateChange(this::onStateChange);
        timer.onFinish(this::onFinish);
    }

    // start work
    public void startWork(int minutes) {
        workMinutes = minutes;
        timer.startWork(minutes * 60);
    }

    // tick event
    private void onTick(int secondsLeft) {
        if (state == PomodoroState.WORK) {
            continuousWorkSeconds++;
        }
    }

    private void onStateChange(PomodoroState newState) {
        this.state = newState;
    }

    private void onFinish(PomodoroState finishedState) {
        if (finishedState == PomodoroState.WORK) {
            onWorkFinished();
        } else if (finishedState == PomodoroState.REST) {
            startWork(workMinutes);
        }
    }

    // flow
    private void onWorkFinished() {
        state = PomodoroState.IDLE;
        // UI sẽ gọi acceptBreak() hoặc skipBreak()
    }

    public void acceptBreak() {
        continuousWorkSeconds = 0; // ✅ reset đúng chỗ
        timer.startRest(breakMinutes * 60);
    }

    public void skipBreak() {
        if (continuousWorkSeconds >= 3600) {
            System.out.println("💧 Gợi ý: nên uống nước");
        }
        startWork(workMinutes);
    }

    public PomodoroState getState() {
        return state;
    }
}
