package roots.dao;

import org.hibernate.Transaction;
import org.hibernate.Session;

import roots.models.TimeManagements;

import java.util.List;

public class TimeManagementsDAO {

    public void saveTimeManagements(TimeManagements time){
        Transaction transaction = null;
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.persist(time);
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null){
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }
    public List<TimeManagements> getTimeHistoryById(Long user_id){
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            return session.createQuery("from TimeManagements where user.id = :uid order by start_time desc ", TimeManagements.class).setParameter("uid",user_id).list();
        }
    }
}

