package roots.services;

import jakarta.persistence.EntityManager;
import roots.utils.DBConnection;
import roots.entity.WaterLog;
import java.util.Map;
import java.util.LinkedHashMap;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class WaterService {
    public void addWater(int amount) {
        EntityManager em = DBConnection.getEntityManager();
        try {
            em.getTransaction().begin();
            LocalDate today = LocalDate.now();

            WaterLog log = em.createQuery("SELECT w FROM WaterLog w WHERE w.date = :d", WaterLog.class)
                    .setParameter("d", today)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (log == null) {
                log = new WaterLog();
                log.setDate(today);
                log.setTotalAmount(amount);
                em.persist(log);
            } else {
                log.setTotalAmount(log.getTotalAmount() + amount);
                em.merge(log);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public int getTodayTotal() {
        EntityManager em = DBConnection.getEntityManager();
        try {
            Long total = em.createQuery("SELECT SUM(w.totalAmount) FROM WaterLog w WHERE w.date = :today", Long.class)
                    .setParameter("today", LocalDate.now())
                    .getSingleResult();
            return total != null ? total.intValue() : 0;
        } finally {
            em.close();
        }
    }
    public Map<String, Integer> getWeeklyWaterData() {
        EntityManager em = DBConnection.getEntityManager();
        Map<String, Integer> data = new LinkedHashMap<>();
        try {
            for (int i = 6; i >= 0; i--) {
                LocalDate date = LocalDate.now().minusDays(i);
                Long total = em.createQuery("SELECT SUM(w.totalAmount) FROM WaterLog w WHERE w.date = :d", Long.class)
                        .setParameter("d", date)
                        .getSingleResult();

                String label = date.format(DateTimeFormatter.ofPattern("dd/MM"));
                data.put(label, total != null ? total.intValue() : 0);
            }
        } finally {
            em.close();
        }
        return data;
    }
}
