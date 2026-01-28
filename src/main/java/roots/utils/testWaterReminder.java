package roots.utils;

import roots.controllers.PomodoroController;

public class testWaterReminder {

    public static void main(String[] args) throws Exception {

        PomodoroController controller = new PomodoroController();

        System.out.println("Start WORK 10s");
        controller.startWork(10); // PHẢI > 5 giây

        Thread.sleep(7000); // đợi đủ tick
        System.out.println("Skip break");
        controller.skipBreak();
    }
}
