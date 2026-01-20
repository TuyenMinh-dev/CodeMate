package roots.dao;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import roots.constrant.Error;
import roots.models.User;
import roots.utils.DBConnection;

public class RegisterDAO {
    public boolean registerNewUser(User  user){
        EntityManager  em = DBConnection.getEntityManager();
        boolean check = false;
        try{
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            check = true;
        }catch (Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
                check = false;
            }
            e.printStackTrace();
        }finally {
            em.close();
        }
        return check;
    }

}
