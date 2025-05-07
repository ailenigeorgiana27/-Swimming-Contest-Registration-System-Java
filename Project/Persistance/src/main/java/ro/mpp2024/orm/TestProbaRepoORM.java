package ro.mpp2024.orm;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ro.mpp2024.domain.Proba;

import java.util.List;

public class TestProbaRepoORM {
    private static SessionFactory sessionFactory;

    public static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public static void main(String... args) {
        // Inițializare Hibernate
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml") // asigură-te că e în resources
                .build();

        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException("Eroare la inițializarea SessionFactory", e);
        }

        // Adăugare proba de test
        Proba proba = new Proba(0L,100, "Liber");
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(proba);
            session.getTransaction().commit();
        }

        // Listare probe din DB
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Proba> probe = session.createQuery("from Proba", Proba.class).list();
            if (probe.isEmpty()) {
                System.out.println("Nu există probe în baza de date.");
            } else {
                for (Proba p : probe) {
                    System.out.println("Proba: " + p.getId() + ", Distanta: " + p.getDistanta() + ", Stil: " + p.getStil());
                }
            }
            session.getTransaction().commit();
        }

        close();
    }
}

