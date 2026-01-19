package roots.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import roots.constrant.Error;
import roots.models.User;
import roots.utils.HibernateUtils;

public class RegisterDAO {
    private LoginDAO loginDAO = new LoginDAO();
    public boolean registerNewUser(User user){
        if(loginDAO.getUserByUsername(user.getUsername()) != null){
            System.out.println(Error.username);
            return false;
        }
        Transaction transaction = null;
        boolean success = false;
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            success = true;
        } catch (Exception e) {
            if(transaction != null){
                transaction.rollback();
                e.printStackTrace();
                success = false;
            }
        }
        return success;
    }

}
