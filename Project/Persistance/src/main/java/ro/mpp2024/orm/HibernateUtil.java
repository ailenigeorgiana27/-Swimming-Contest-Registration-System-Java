package ro.mpp2024.orm;



import org.hibernate.SessionFactory;

import org.hibernate.cfg.Configuration;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Proba;


public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(){
        if ((sessionFactory==null)||(sessionFactory.isClosed()))
            sessionFactory=createNewSessionFactory();
        return sessionFactory;
    }

    private static  SessionFactory createNewSessionFactory(){
        sessionFactory = new Configuration()
                .addAnnotatedClass(Participant.class)
                .addAnnotatedClass(Proba.class)
                .buildSessionFactory();
        return sessionFactory;
    }

    public static  void closeSessionFactory(){
        if (sessionFactory!=null)
            sessionFactory.close();
    }
}
