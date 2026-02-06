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
        List<toDoList> tasks = findByDate(date);
        if (tasks.isEmpty()) return 0.0;

        long doneCount = tasks.stream().filter(toDoList::isCompleted).count();
        return (double) doneCount / tasks.size();
    }


}