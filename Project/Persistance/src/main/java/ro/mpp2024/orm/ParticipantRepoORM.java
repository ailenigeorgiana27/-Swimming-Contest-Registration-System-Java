package ro.mpp2024.orm;

import org.hibernate.Session;
import ro.mpp2024.IParticipantRepo;
import ro.mpp2024.domain.Participant;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ParticipantRepoORM implements IParticipantRepo {

    public Optional<Participant> findByNameAndAge(String name, int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Participant where name = :name and age = :age", Participant.class)
                    .setParameter("name", name)
                    .setParameter("age", age)
                    .uniqueResultOptional();
        } catch (Exception e) {
            System.err.println("Eroare la căutarea după nume și vârstă: " + e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Participant> findOne(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(
                    session.createSelectionQuery("from Participant where id = :id", Participant.class)
                            .setParameter("id", id)
                            .getSingleResultOrNull()
            );
        } catch (Exception e) {
            System.err.println("Eroare la căutarea participantului cu ID-ul " + id + ": " + e);
            return Optional.empty();
        }
    }

    @Override
    public List<Participant> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Participant", Participant.class).getResultList();
        } catch (Exception e) {
            System.err.println("Eroare la obținerea participanților: " + e);
            return List.of();
        }
    }

    @Override
    public Optional<Participant> save(Participant entity) {
        try {

            HibernateUtil.getSessionFactory().inTransaction(session -> {
                session.persist(entity);
                session.flush();
            });
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Eroare la salvarea participantului: " + e);
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Participant> delete(Long id) {
        try {
            final Participant[] deleted = new Participant[1];
            HibernateUtil.getSessionFactory().inTransaction(session -> {
                Participant participant = session.find(Participant.class, id);
                if (participant != null) {
                    session.remove(participant);
                    session.flush();
                    deleted[0] = participant;
                }
            });
            return Optional.ofNullable(deleted[0]);
        } catch (Exception e) {
            System.err.println("Eroare la ștergerea participantului: " + e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Participant> update(Participant entity) {
        try {
            final boolean[] updated = {false};
            HibernateUtil.getSessionFactory().inTransaction(session -> {
                Participant existing = session.find(Participant.class, entity.getId());
                if (!Objects.isNull(existing)) {
                    existing.setName(entity.getName());
                    existing.setAge(entity.getAge());
                    session.merge(existing);
                    session.flush();
                    updated[0] = true;
                }
            });
            return updated[0] ? Optional.empty() : Optional.of(entity);
        } catch (Exception e) {
            System.err.println("Eroare la actualizarea participantului: " + e);
            return Optional.of(entity);
        }
    }

    @Override
    public Long getMaxParticipantId() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select max(id) from Participant", Long.class).uniqueResult();
        } catch (Exception e) {
            System.err.println("Eroare la obținerea ID-ului maxim: " + e);
            return null;
        }
    }
}
