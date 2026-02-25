package roots.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import roots.entity.DailyStats;
import roots.services.StatService;
import java.util.Map;

public class StatController {
    // Biến này phải nằm ở đây thì mới không bị lỗi đỏ
    @FXML private BarChart<String, Number> barChart;
    private final StatService statService = new StatService();

    @FXML
    public void initialize() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Chỉ lấy dữ liệu thật từ file .dat của bạn
        Map<String, DailyStats> allData = statService.loadAllStats();

        // Sắp xếp theo ngày để biểu đồ hiện từ trái qua phải cho đúng thứ tự thời gian
        allData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    double minutes = entry.getValue().getTotalWorkSeconds() / 60.0;
                    series.getData().add(new XYChart.Data<>(entry.getKey(), minutes));
                });

        barChart.getData().add(series);
    }

    @FXML
    private void handleClose() {
        ((Stage) barChart.getScene().getWindow()).close();
    }
}