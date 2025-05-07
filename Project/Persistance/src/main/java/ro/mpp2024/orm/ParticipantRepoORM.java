package ro.mpp2024.orm;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ro.mpp2024.IParticipantRepo;
import ro.mpp2024.domain.Participant;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ParticipantRepoORM implements IParticipantRepo {
    private static SessionFactory sessionFactory;

    // Constructorul inițializator al repo-ului
    public ParticipantRepoORM() {
        initialize();
    }

    static void initialize() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // se configurează folosind hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Exceptie: " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Override
    public Optional<Participant> findOne(Long id) {
        Participant participant = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            participant = session.get(Participant.class, id);
            session.getTransaction().commit();
        }
        if (participant != null)
            System.out.println("Găsit: " + participant);
        else
            System.out.println("Participant inexistent cu id " + id);

        return Optional.ofNullable(participant);
    }

    @Override
    public List<Participant> findAll() {
        List<Participant> participants;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Stream<Participant> stream = session.createQuery("from Participant", Participant.class).stream();
            participants = StreamSupport.stream(stream.spliterator(), false).toList();
            session.getTransaction().commit();
        }

        System.out.println("Toți participanții: " + participants);
        participants.forEach(System.out::println);
        return participants;
    }

    @Override
    public Optional<Participant> save(Participant entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Eroare la salvarea participantului: " + e);
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Participant> update(Participant entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Participant participant = session.load(Participant.class, entity.getId());
                participant.setName(entity.getName());
                participant.setAge(entity.getAge());
                session.update(participant);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la update: " + ex);
                if (tx != null) tx.rollback();
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Participant> delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Participant participant = session.load(Participant.class, id);
            if (participant != null) {
                session.delete(participant);
            }
            session.getTransaction().commit();
        }
        return Optional.empty();
    }

    @Override
    public Long getMaxParticipantId() {
        Long id = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            id = (Long) session.createQuery("select max(id) from Participant").uniqueResult();
            session.getTransaction().commit();
        }
        return id;
    }
}

