package roots.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
    private static SessionFactory sessionFactory = null;
    static{
        try{
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable e){
            System.out.println(e);
            throw new ExceptionInInitializerError(e);
        }
    }
    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
