package roots.app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import roots.models.User;
import roots.utils.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MainApp {
    public static void main(String[] args) {
        // 1. Khởi tạo EntityManagerFactory (theo tên trong persistence.xml)
        // Đây là đối tượng nặng, chỉ nên tạo 1 lần, EntityManager là công cụ thao tác DB(thêm sử xóa , truy vấn)
        EntityManager entityManager = DBConnection.getEntityManager();

        //mọi thao tác thay đô d liệu DB phải nằm trong transaction
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try{
            //bắt đầu giao dịch
            entityTransaction.begin();

            //giả sử mật khâẩu người dùng là abc123
            String rawPassword = "abc123";
            //mã hóa mật khẩu người dùng
            String hashedPassword = BCrypt.hashpw(rawPassword,BCrypt.gensalt());

            //thêm user mới
            User user1 = new User("NguyenVanA",hashedPassword,"VanA@gmail.com");
            //cho user mới vào danh sách DB
            entityManager.persist(user1);

            //truy vấn(read)
            User foundUser = entityManager.find(User.class, user1.getId());
            System.out.println("tìm thấy: " + foundUser.getUsername());

            //câập nhật(update)
            foundUser.setUsername("NguyenVanB");
            //muốn đổi mật kẩu
            String newRawPassword = "B123";
            String newHashedPassword = BCrypt.hashpw(newRawPassword, BCrypt.gensalt());
            foundUser.setPassword(newHashedPassword);

            //lưu vào DB
            entityTransaction.commit();

        } catch (Exception e) {
            if(entityTransaction.isActive()){//nếu qus trình đang diễn ra(true)
                entityTransaction.rollback();//gọi rollback để xóa dữ lệu hoạt động nửa vời
            }
            e.printStackTrace();
        }finally {
            //đóng EntityManager
            if(entityManager.isOpen()){
                entityManager.close();
            }
            DBConnection.closeEntityManager();
        }

    }
}
