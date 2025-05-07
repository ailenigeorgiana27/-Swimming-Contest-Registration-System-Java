package ro.mpp2024.orm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import ro.mpp2024.IProbaRepo;
import ro.mpp2024.domain.Proba;

import java.util.List;
import java.util.Optional;

public class ProbaRepoORM implements IProbaRepo {
    private static SessionFactory sessionFactory;

    public ProbaRepoORM() {
        initialize();
    }

    static void initialize() {
        try {
            Configuration configuration = new Configuration().configure(); // încarcă configurația din hibernate.cfg.xml
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Eroare la initializarea Hibernate: " + e);
        }
    }


    static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Override
    public Optional<Proba> findOne(Long id) {
        Proba proba = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            proba = session.get(Proba.class, id);
            session.getTransaction().commit();
        }
        return Optional.ofNullable(proba);
    }

    @Override
    public List<Proba> findAll() {
        List<Proba> probe;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            probe = session.createQuery("from Proba", Proba.class).list();
            session.getTransaction().commit();
        }
        return probe;
    }

    @Override
    public Optional<Proba> save(Proba entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Eroare la salvare: " + e);
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Proba> delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Proba proba = session.get(Proba.class, id);
            if (proba != null) {
                session.delete(proba);
                tx.commit();
                return Optional.of(proba);
            }
            tx.commit();
        } catch (Exception e) {
            System.err.println("Eroare la stergere: " + e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Proba> update(Proba entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(entity);
            tx.commit();
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Eroare la update: " + e);
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Proba> findProbaByStyleDistance(String style, int distance) {
        try (Session session = sessionFactory.openSession()) {
            var query = session.createQuery("from Proba where stil = :style and distanta = :distance", Proba.class);
            query.setParameter("style", style);
            query.setParameter("distance", distance);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            System.err.println("Eroare la cautarea probei: " + e);
            return Optional.empty();
        }
    }


}
