package roots.entity;

import java.io.Serializable;

// Serializable giúp Java có thể chuyển đối tượng này thành dữ liệu để ghi vào file
public class DailyStats implements Serializable {
    private String date; // Lưu ngày kiểu "2026-01-31"
    private int totalWorkSeconds; // Tổng số giây làm việc trong ngày đó

    public DailyStats(String date, int totalWorkSeconds) {
        this.date = date;
        this.totalWorkSeconds = totalWorkSeconds;
    }

    // Getters để lấy dữ liệu, Setters để cập nhật dữ liệu
    public String getDate() { return date; }
    public int getTotalWorkSeconds() { return totalWorkSeconds; }
    public void addSeconds(int seconds) { this.totalWorkSeconds += seconds; }
}
