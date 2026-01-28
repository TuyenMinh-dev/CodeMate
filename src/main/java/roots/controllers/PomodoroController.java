package roots.controllers;

import roots.entity.PomodoroState;
import roots.services.PomodoroTimer;

public class PomodoroController {

    private PomodoroState state = PomodoroState.IDLE;
    private final PomodoroTimer timer = new PomodoroTimer();

    private int workMinutes = 30;
    private final int breakMinutes = 5;

    private  int continuousWorkSeconds = 0;

    public PomodoroController() {
        timer.onTick(this::onTick);
        timer.onStateChange(this::onStateChange);
        timer.onFinish(this::onFinish);
    }

    //start

    public void startWork(int minutes) {
        workMinutes = minutes;
        continuousWorkSeconds = 0;
        timer.startWork(minutes * 60);
    }

    //các sự kiện diễn ra trong quá tình work

    private void onTick(int secondsLeft) {
        if (state == PomodoroState.WORK) {
            continuousWorkSeconds++;
        }
    }

    private void onStateChange(PomodoroState newState) {
        this.state = newState;
    }

    private void onFinish() {
        if (state == PomodoroState.WORK) {
            onWorkFinished();
        } else if (state == PomodoroState.REST) {
            startWork(workMinutes);
        }
    }

    //flow

    private void onWorkFinished() {
        state = PomodoroState.IDLE;
        // accept phiên nghỉ or skip phiên nghỉ
    }

    public void acceptBreak() {
        continuousWorkSeconds = 0;
        timer.startRest(breakMinutes * 60);
    }

    public void skipBreak() {
        if (continuousWorkSeconds >= 3600) {//continousWorkSeconds là kiểu đếm số thời gian làm việc liên tục
                                         // (bản chất là số lần gọi onTick trong state Work)
            System.out.println("💧 Gợi ý: nên uống nước");
        }
        startWork(workMinutes);
    }

    public PomodoroState getState() {
        return state;
    }
}
