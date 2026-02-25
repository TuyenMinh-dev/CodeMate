package roots.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import roots.utils.ChangeFXML;


public class HomeController {

    @FXML public void showPomodoro(Event event) {
        ChangeFXML.changeFXML(event, "/view/pomodoro.fxml");
    }

    @FXML
    public void showTodoList(Event event) {
        ChangeFXML.changeFXML(event, "/view/todo.fxml");
    }

    @FXML public void showStatistics(Event event) {
        ChangeFXML.changeFXML(event, "/view/statistics.fxml");
    }
}