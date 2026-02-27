package roots.dao;
import java.time.LocalDate;
import java.util.List;
import roots.entity.ToDoList;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
public class ToDoListDAO {
    private static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("CodeMatePU");

    public static void save(ToDoList todo) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(todo);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    public static void delete(ToDoList todo) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // bắt buộc phải attach object vào persistence context
            ToDoList managedTodo = em.find(ToDoList.class, todo.getId());

            if (managedTodo != null) {
                em.remove(managedTodo);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    public static void update(ToDoList todo) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(todo);   // UPDATE
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }


    public static List<ToDoList> findAll() {
        EntityManager em = emf.createEntityManager();
        List<ToDoList> list = em
                .createQuery("FROM ToDoList", ToDoList.class)
                .getResultList();
        em.close();
        return list;
    }
    public static List<ToDoList> findByDate(LocalDate date) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM ToDoList WHERE createdAt = :date", ToDoList.class)
                    .setParameter("date", date)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public static double getCompletionRate(LocalDate date) {
        EntityManager em = emf.createEntityManager();
        try {
            List<ToDoList> tasks = em.createQuery("FROM ToDoList WHERE createdAt = :date", ToDoList.class)
                    .setParameter("date", date)
                    .getResultList();
            if (tasks.isEmpty()) return 0.0;
            return (double) tasks.stream().filter(ToDoList::isCompleted).count() / tasks.size();
        } finally {
            em.close();
        }
    }
    public static void carryOverPendingTasks(LocalDate today) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Tìm những task chưa xong của những ngày trước
            List<ToDoList> pendingTasks = em.createQuery(
                            "FROM ToDoList WHERE completed = false AND createdAt < :today", ToDoList.class)
                    .setParameter("today", today)
                    .getResultList();

            // Đổi ngày của chúng thành hôm nay
            for (ToDoList t : pendingTasks) {
                t.setCreatedAt(today);
                em.merge(t); // Cập nhật vào DB
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }


}