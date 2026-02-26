package roots.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import roots.entity.DailyStats;
import roots.models.UserSession;
import roots.services.StatService;
import roots.utils.ChangeFXML;

import java.util.Map;

public class StatController {
    // Biến này phải nằm ở đây thì mới không bị lỗi đỏ
    @FXML private BarChart<String, Number> barChart;
    private final StatService statService = new StatService();

    @FXML
    public void initialize() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        //xem người dùng nào đang đăng nhập
        if(UserSession.getCurrentUser() != null){
            long currentUser = UserSession.getCurrentUser().getId();
            Map<String, Long> dataUser = statService.getStatByUserId(currentUser);
            //dữ liệu cho vào biểu đồ
            dataUser.entrySet().stream().sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        double minutes = entry.getValue() / 60.0;
                        series.getData().add(new XYChart.Data<>(entry.getKey(), minutes));

                    });
            barChart.getData().add(series);
        }

    }

    @FXML
    public void comeBackProduct(MouseEvent event){
        ChangeFXML.changeFXML(event, "/view/home.fxml");
    }

    @FXML
    private void handleClose() {
        ((Stage) barChart.getScene().getWindow()).close();
    }
}