package ro.mpp2024.orm;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ro.mpp2024.IPersoanaOficiuRepo;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.utils.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class PersoanaOficiuRepoORM implements IPersoanaOficiuRepo {

    @Override
    public Optional<PersoanaOficiu> findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            PersoanaOficiu user = session
                    .createQuery("from PersoanaOficiu where username = :username", PersoanaOficiu.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            e.printStackTrace(); // poți înlocui cu logare
            return Optional.empty();
        }
    }

    @Override
    public Optional<PersoanaOficiu> findOne(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(PersoanaOficiu.class, id));
        }
    }

    @Override
    public List<PersoanaOficiu> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from PersoanaOficiu", PersoanaOficiu.class).list();
        }
    }

    @Override
    public Optional<PersoanaOficiu> save(PersoanaOficiu entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            return Optional.of(entity);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<PersoanaOficiu> delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            PersoanaOficiu entity = session.get(PersoanaOficiu.class, id);
            if (entity == null) return Optional.empty();

            transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
            return Optional.of(entity);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<PersoanaOficiu> update(PersoanaOficiu entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
            return Optional.of(entity);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
