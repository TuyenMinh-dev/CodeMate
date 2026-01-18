package roots.app;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import roots.entity.toDoList;

public class MainApp {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CodeMatePU");

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        toDoList todo = new toDoList();
        todo.setTitle("Hoc Hibernate");
        todo.setCompleted(false);

        em.persist(todo);

        em.getTransaction().commit();

        em.close();
        emf.close();

        System.out.println("Insert done");
    }

    }

