package roots.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import roots.services.StatService;
import roots.services.WaterService;
import roots.utils.ChangeFXML;
import java.util.Map;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.paint.Color;
public class StatController {

    @FXML private BarChart<String, Number> barChart;
    @FXML private BarChart<String, Number> waterChart;

    private final StatService statService = new StatService();
    private final WaterService waterService = new WaterService();
    @FXML private Button btnPomoTab;
    @FXML private Button btnWaterTab;



    @FXML
    public void initialize() {
        // Sử dụng Platform.runLater để đảm bảo UI đã load xong mới chỉnh style
        Platform.runLater(() -> {
            loadPomoData();
            loadWaterData();
        });
    }

    private void loadPomoData() {
        if (barChart == null) return;
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Phút tập trung");

        Map<String, Integer> data = statService.getWeeklyPomoData();
        data.forEach((date, minutes) -> {
            series.getData().add(new XYChart.Data<>(date, minutes));
        });

        barChart.getData().add(series);

        // Lấy trục ra để định dạng - Đã fix lỗi ép kiểu
        CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
        NumberAxis yAxis = (NumberAxis) barChart.getYAxis();

        // 1. Ép màu trắng sáng cho tiêu đề (SỐ PHÚT / NGÀY)
        String axisStyle = "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;";
        xAxis.setLabel("NGÀY");
        yAxis.setLabel("SỐ PHÚT");

        // Đợi UI render xong mới lookup để tránh bị null
        Platform.runLater(() -> {
            if (xAxis.lookup(".axis-label") != null) xAxis.lookup(".axis-label").setStyle(axisStyle);
            if (yAxis.lookup(".axis-label") != null) yAxis.lookup(".axis-label").setStyle(axisStyle);
        });

        // 2. Chỉnh ngày tháng: NẰM NGANG, TRẮNG, TÁCH RỜI
        xAxis.setTickLabelFill(Color.WHITE);
        xAxis.setTickLabelRotation(0);
        xAxis.setTickLabelFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 10));

        // Tăng khoảng cách cực đại để các ngày dạt ra xa nhau
        barChart.setCategoryGap(60);
        barChart.setBarGap(5);

        yAxis.setTickLabelFill(Color.WHITE);

        // 3. Nhuộm màu cam
        for (XYChart.Data<String, Number> d : series.getData()) {
            if (d.getNode() != null) d.getNode().setStyle("-fx-bar-fill: #e67e22;");
        }
    }

    private void loadWaterData() {
        if (waterChart == null) return;
        waterChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Lượng nước");

        Map<String, Integer> data = waterService.getWeeklyWaterData();
        data.forEach((date, amount) -> {
            series.getData().add(new XYChart.Data<>(date, amount));
        });

        waterChart.getData().add(series);

        javafx.scene.chart.CategoryAxis xAxis = (javafx.scene.chart.CategoryAxis) waterChart.getXAxis();
        javafx.scene.chart.NumberAxis yAxis = (javafx.scene.chart.NumberAxis) waterChart.getYAxis();

        xAxis.setLabel("NGÀY");
        yAxis.setLabel("SỐ ML");

        // Ép màu trắng sáng cho dễ nhìn
        String labelStyle = "-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;";
        xAxis.lookup(".axis-label").setStyle(labelStyle);
        yAxis.lookup(".axis-label").setStyle(labelStyle);

        xAxis.setTickLabelFill(javafx.scene.paint.Color.WHITE);
        yAxis.setTickLabelFill(javafx.scene.paint.Color.WHITE);

        // Nhuộm màu xanh cho cột nước
        for (XYChart.Data<String, Number> d : series.getData()) {
            if (d.getNode() != null) d.getNode().setStyle("-fx-bar-fill: #3b82f6;");
        }
    }

    @FXML
    public void comeBackProduct(MouseEvent event) {
        ChangeFXML.changeFXML(event, "/view/home.fxml");
    }

    @FXML
    public void handlePomoTabClick(MouseEvent event) {
        updateTabUI(true);
    }

    @FXML
    public void handleWaterTabClick(MouseEvent event) {
        updateTabUI(false);
    }
    private void updateTabUI(boolean isPomo) {
        // 1. Ẩn hiện biểu đồ
        barChart.setVisible(isPomo);
        waterChart.setVisible(!isPomo);

        // 2. Định nghĩa style
        String activePomo = "-fx-background-color: #e67e22; -fx-text-fill: white; -fx-background-radius: 25; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(230,126,34,0.6), 15, 0, 0, 0);";
        String activeWater = "-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-background-radius: 25; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(59,130,246,0.6), 15, 0, 0, 0);";
        String inactive = "-fx-background-color: transparent; -fx-text-fill: #888888; -fx-background-radius: 25; -fx-font-weight: bold;";

        if (isPomo) {
            btnPomoTab.setStyle(activePomo);
            btnWaterTab.setStyle(inactive);
        } else {
            btnPomoTab.setStyle(inactive);
            btnWaterTab.setStyle(activeWater);
        }
    }

}