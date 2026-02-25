//package roots.app;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.EntityTransaction;
//import jakarta.persistence.Persistence;
//import org.mindrot.jbcrypt.BCrypt;
//import roots.entity.toDoList;
//import roots.models.User;
//import roots.utils.DBConnection;
//
//public class MainApp {
//    public static void main(String[] args) {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CodeMatePU");
//
//        EntityManager em = emf.createEntityManager();
//
//        em.getTransaction().begin();
//
//        toDoList todo = new toDoList();
//        todo.setTitle("Hoc Hibernate");
//        todo.setCompleted(false);
//
//        em.persist(todo);
//
//        em.getTransaction().commit();
//
//        em.close();
//        emf.close();
//        System.out.println("Insert done");
//
//
//        EntityManager entityManager = DBConnection.getEntityManager();
//
//        //mọi thao tác thay đô d liệu DB phải nằm trong transaction
//        EntityTransaction entityTransaction = entityManager.getTransaction();
//
//        try{
//            //bắt đầu giao dịch
//            entityTransaction.begin();
//
//            //giả sử mật khâẩu người dùng là abc123
//            String rawPassword = "abc123";
//            //mã hóa mật khẩu người dùng
//            String hashedPassword = BCrypt.hashpw(rawPassword,BCrypt.gensalt());
//
//            //thêm user mới
//            User user1 = new User("Nguyễn Văn Anh","NguyenVanAnh",hashedPassword,"VanA@gmail.com");
//            //cho user mới vào danh sách DB
//            entityManager.persist(user1);
//
//            //truy vấn(read)
//            User foundUser = entityManager.find(User.class, user1.getId());
//            System.out.println("tìm thấy: " + foundUser.getUsername());
//
//            //câập nhật(update)
//            foundUser.setUsername("NguyenVanB");
//            //muốn đổi mật kẩu
//            String newRawPassword = "B123";
//            String newHashedPassword = BCrypt.hashpw(newRawPassword, BCrypt.gensalt());
//            foundUser.setPassword(newHashedPassword);
//
//            //lưu vào DB
//            entityTransaction.commit();
//
//        } catch (Exception e) {
//            if(entityTransaction.isActive()){//nếu qus trình đang diễn ra(true)
//                entityTransaction.rollback();//gọi rollback để xóa dữ lệu hoạt động nửa vời
//            }
//            e.printStackTrace();
//        }finally {
//            //đóng EntityManager
//            if(entityManager.isOpen()){
//                entityManager.close();
//            }
//            DBConnection.closeEntityManager();
//        }
//    }
//
//
//
//}
//
