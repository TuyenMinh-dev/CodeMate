package roots.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import roots.constrant.Error;
import roots.models.User;
import roots.utils.DBConnection;

public class LoginDAO {
    public User checkAccountUser(String username, String password){
        EntityManager em = DBConnection.getEntityManager();
        try{
            User user = em.createQuery("select u from User u where u.username = :user and u.password = :pass", User.class)
                    .setParameter("user", username)
                    .setParameter("pass", password)
                    .getSingleResult();
            return user ;
        } catch (NoResultException e) {
            System.out.println(Error.failLogin);
            e.printStackTrace();
            return null;
        }finally {
            em.close();
        }
    }
    public User getUserByUsername(String username){
        EntityManager em = DBConnection.getEntityManager();
        try{
            User user = em.createQuery("select u from User u where u.username = :name", User.class)
                    .setParameter("name", username).getSingleResult();
            return user;
        } catch (NoResultException e) {
            System.out.println(Error.username);
            return  null;
        }finally {
            em.close();
        }
    }
}
