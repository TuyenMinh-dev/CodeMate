package roots.dao;

import org.hibernate.Session;
import roots.models.User;
import roots.utils.HibernateUtils;

public class LoginDAO {
    public User getUserByUsername(String username){
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            return session.createQuery("from User where username = :user", User.class).setParameter("user",username).uniqueResult();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
