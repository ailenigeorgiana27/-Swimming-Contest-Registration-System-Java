package ro.mpp2024.orm;

import org.hibernate.Session;
import ro.mpp2024.IPersoanaOficiuRepo;
import ro.mpp2024.domain.PersoanaOficiu;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PersoanaOficiuRepoORM implements IPersoanaOficiuRepo {

    @Override
    public Optional<PersoanaOficiu> findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from PersoanaOficiu where username = :username", PersoanaOficiu.class)
                    .setParameter("username", username)
                    .uniqueResultOptional();
        } catch (Exception e) {
            System.err.println("Eroare la căutarea după username: " + e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<PersoanaOficiu> findOne(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createSelectionQuery("from PersoanaOficiu where id = :id", PersoanaOficiu.class)
                    .setParameter("id", id)
                    .getSingleResultOrNull());
        } catch (Exception e) {
            System.err.println("Eroare la găsirea persoanei cu ID-ul " + id + ": " + e);
            return Optional.empty();
        }
    }

    @Override
    public List<PersoanaOficiu> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from PersoanaOficiu", PersoanaOficiu.class).getResultList();
        } catch (Exception e) {
            System.err.println("Eroare la obținerea tuturor persoanelor: " + e);
            return List.of();
        }
    }

    @Override
    public Optional<PersoanaOficiu> save(PersoanaOficiu entity) {
        try {
            HibernateUtil.getSessionFactory().inTransaction(session -> {
                session.persist(entity);
                session.flush();
            });
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Eroare la salvare: " + e);
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<PersoanaOficiu> delete(Long id) {
        try {
            final PersoanaOficiu[] deleted = new PersoanaOficiu[1];
            HibernateUtil.getSessionFactory().inTransaction(session -> {
                PersoanaOficiu persoana = session.find(PersoanaOficiu.class, id);
                if (persoana != null) {
                    session.remove(persoana);
                    session.flush();
                    deleted[0] = persoana;
                }
            });
            return Optional.ofNullable(deleted[0]);
        } catch (Exception e) {
            System.err.println("Eroare la ștergere: " + e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<PersoanaOficiu> update(PersoanaOficiu entity) {
        try {
            final boolean[] updated = {false};
            HibernateUtil.getSessionFactory().inTransaction(session -> {
                if (!Objects.isNull(session.find(PersoanaOficiu.class, entity.getId()))) {
                    session.merge(entity);
                    session.flush();
                    updated[0] = true;
                }
            });
            return updated[0] ? Optional.empty() : Optional.of(entity);
        } catch (Exception e) {
            System.err.println("Eroare la update: " + e);
            return Optional.of(entity);
        }
    }
}
