package roots.dao;
import java.time.LocalDate;
import java.util.List;
import roots.entity.toDoList;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
public class toDoListDao {
    private static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("CodeMatePU");

    public static void save(toDoList todo) {
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
    public static void delete(toDoList todo) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // bắt buộc phải attach object vào persistence context
            toDoList managedTodo = em.find(toDoList.class, todo.getId());

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
    public static void update(toDoList todo) {
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


    public static List<toDoList> findAll() {
        EntityManager em = emf.createEntityManager();
        List<toDoList> list = em
                .createQuery("FROM toDoList", toDoList.class)
                .getResultList();
        em.close();
        return list;
    }
    public static List<toDoList> findByDate(LocalDate date) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM toDoList WHERE createdAt = :date", toDoList.class)
                    .setParameter("date", date)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public static double getCompletionRate(LocalDate date) {
        EntityManager em = emf.createEntityManager();
        try {
            List<toDoList> tasks = em.createQuery("FROM toDoList WHERE createdAt = :date", toDoList.class)
                    .setParameter("date", date)
                    .getResultList();
            if (tasks.isEmpty()) return 0.0;
            return (double) tasks.stream().filter(toDoList::isCompleted).count() / tasks.size();
        } finally {
            em.close();
        }
    }
    public static void carryOverPendingTasks(LocalDate today) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Tìm những task chưa xong của những ngày trước
            List<toDoList> pendingTasks = em.createQuery(
                            "FROM toDoList WHERE completed = false AND createdAt < :today", toDoList.class)
                    .setParameter("today", today)
                    .getResultList();

            // Đổi ngày của chúng thành hôm nay
            for (toDoList t : pendingTasks) {
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