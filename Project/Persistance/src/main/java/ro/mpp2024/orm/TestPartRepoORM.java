package ro.mpp2024.orm;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ro.mpp2024.domain.Participant;

import java.util.List;

public class TestPartRepoORM {
    private static SessionFactory sessionFactory;

    public static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public static void main(String... args) {
        // Inițializare Hibernate
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml") // asigură-te că ai acest fișier în `resources`
                .build();

        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException("Eroare la inițializarea SessionFactory", e);
        }

        // Salvare participant test
        Participant participant = new Participant(0L, "Popescu", 20); // Constructor: nume + vârstă (presupus)
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(participant);
            session.getTransaction().commit();
        }

        // Listare participanți
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Participant> participants = session.createQuery("from Participant", Participant.class).list();
            if (participants.isEmpty()) {
                System.out.println("Nu există participanți în baza de date.");
            } else {
                for (Participant p : participants) {
                    System.out.println("Participant: " + p.getId() + ", Nume: " + p.getName() + ", Vârstă: " + p.getAge());
                }
            }
            session.getTransaction().commit();
        }

        close();
    }
}

