package roots.services;

import roots.entity.DailyStats;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class StatService {

    // Hàm này dùng để lưu 1 phiên làm việc vừa xong vào lịch sử
    public void saveSession(int seconds) {
        Map<String, DailyStats> allStats = loadAllStats(); // 1. Đọc dữ liệu cũ lên trước
        String today = LocalDate.now().toString(); // Lấy ngày hôm nay

        if (allStats.containsKey(today)) {
            // Nếu hôm nay đã có trong danh sách rồi -> Cộng thêm giây vào
            allStats.get(today).addSeconds(seconds);
        } else {
            // Nếu hôm nay là ngày mới -> Tạo mới dòng dữ liệu cho ngày hôm nay
            allStats.put(today, new DailyStats(today, seconds));
        }
        saveToFile(allStats); // 2. Ghi đè lại file đã cập nhật
    }

    // Hàm đọc toàn bộ lịch sử từ file lên bộ nhớ (RAM)
    public Map<String, DailyStats> loadAllStats() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new HashMap<>(); // Nếu chưa có file thì trả về danh sách rỗng

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, DailyStats>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private void saveToFile(Map<String, DailyStats> stats) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(stats);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
