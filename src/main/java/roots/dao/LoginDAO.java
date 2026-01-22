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
    public User getUserByValue(String value, String nameField, String errorMessage){
        EntityManager em = DBConnection.getEntityManager();
        try{
            User user = em.createQuery("select u from User u where u." + nameField + " = :value", User.class)
                    .setParameter("value", value).getSingleResult();
            return user;
        } catch (NoResultException e) {
            System.out.println(errorMessage);
            return  null;
        }finally {
            em.close();
        }
    }
    public User getUserByEmail(String email){
        return getUserByValue("email", email, Error.email);
    }
    public User getUserByUsername(String username){
        return getUserByValue("name", username, Error.username);
    }
}
