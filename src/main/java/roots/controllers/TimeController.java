package roots.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import roots.dao.TimeManagementsDAO;
import roots.models.TimeManagements;
import roots.models.User;

import java.time.LocalDateTime;

public class TimeController {
    @FXML private TextField task_name;
    @FXML private Label time_label;

    private Timeline timeline;
    private LocalDateTime startTime;
    private long sumSecondsUI;

    private User currentUser;
    public void getInfoUser(User user){
        this.currentUser = user;
    }

    public void updateLabel(){
        long h = sumSecondsUI / 3600;
        long m = (sumSecondsUI % 3600) /60;
        long s = sumSecondsUI % 60;
        time_label.setText(String.format("%02d:%02d:%02d",h,m,s));
    }

    @FXML
    public void startButton(){
        startTime = LocalDateTime.now();
        sumSecondsUI = 0;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1),e ->{
            sumSecondsUI++;
            updateLabel();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    @FXML
    public void stopButton(){
        timeline.stop();
        LocalDateTime endTime = LocalDateTime.now();

        TimeManagements timeManagements = new TimeManagements(currentUser, task_name.getText(),startTime,endTime);
        new TimeManagementsDAO().saveTimeManagements(timeManagements);

        time_label.setText("00:00:00");
    }
}
