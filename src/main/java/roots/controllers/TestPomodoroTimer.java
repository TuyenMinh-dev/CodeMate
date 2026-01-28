package roots.controllers;

public class TestPomodoroTimer {
    public static void main(String[] args) throws Exception {
        PomodoroController controller = new PomodoroController();

        System.out.println("Start WORK 0.1 min (~6s)");
        controller.startWork(0);

        Thread.sleep(7000);

        System.out.println("Accept break");
        controller.acceptBreak();

        Thread.sleep(6000);
    }
}
