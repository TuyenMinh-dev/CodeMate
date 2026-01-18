package roots.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class testConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/test1";
        String user = "root";
        String password = ""; // nếu MySQL có mật khẩu thì điền vào
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối database THÀNH CÔNG!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


