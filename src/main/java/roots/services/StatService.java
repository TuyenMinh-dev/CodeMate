package roots.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import roots.entity.DailyStats;
import roots.entity.PomodoroState;
import roots.entity.TimeManagements;
import roots.models.UserSession;
import roots.utils.DBConnection;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class StatService {

    // Hàm này dùng để lưu 1 phiên làm việc vừa xong vào lịch sử
    public void saveSession(long seconds) {
        EntityManager em = DBConnection.getEntityManager();
        EntityTransaction entityTransaction = em.getTransaction();

        try{
            entityTransaction.begin();
            TimeManagements time = new TimeManagements();
            time.setUser(UserSession.getCurrentUser());
            time.setStart_time(LocalDateTime.now());
            time.setDuration_seconds(seconds);
            time.setState(PomodoroState.WORK);

            //lưu các giá trị na vào database trong sql
            em.persist(time);
            entityTransaction.commit();
        } catch (Exception e) {
            if(entityTransaction.isActive()){
                entityTransaction.rollback();
            }
            e.printStackTrace();
        }finally {
            em.close();
        }
    }
    // Hàm đọc toàn bộ lịch sử từ sql
    public Map<String, Long> getStatByUserId(long userId){
        EntityManager em = DBConnection.getEntityManager();
        Map<String,Long> map = new HashMap<>();

        try{
            String jpql = "select function('date',t.start_time),sum(t.duration_seconds) "
                    + "from TimeManagements t " +
                    "where t.user.id = :uid " +
                    "group by function('date', t.start_time)";
            List<Object[]> results = em.createQuery(jpql)
                                    .setParameter("uid", userId)
                                    .getResultList();

            for(Object[] re : results){
                map.put(re[0].toString(), ((Number)re[1]).longValue());

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            em.close();
        }
        return map;
    }

}
