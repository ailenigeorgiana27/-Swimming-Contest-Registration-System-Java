package ro.mpp2024.SpringData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.utils.JdbcUtils;
import ro.mpp2024.IProbaRepo;

import java.sql.*;
import java.util.*;

@Component
public class ProbaDbRepositoryS implements IProbaRepo {
    private static final Logger logger = LogManager.getLogger();
    private final JdbcUtils dbUtils;

    @Autowired
    public ProbaDbRepositoryS(Properties properties) {
        this.dbUtils = new JdbcUtils(properties);
    }

    @Override
    public List<Proba> findAll() {
        logger.traceEntry("Finding all probe");
        List<Proba> probe = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Proba")) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Proba p = new Proba(rs.getLong("id"), rs.getInt("distance"), rs.getString("stil"));
                probe.add(p);
            }
        } catch (SQLException e) {
            logger.error("Error in findAll", e);
        }
        logger.traceExit(probe);
        return probe;
    }

    @Override
    public Optional<Proba> findOne(Long id) {
        logger.traceEntry("Finding proba by id {}", id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Proba WHERE id=?")) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Proba p = new Proba(rs.getLong("id"), rs.getInt("distance"), rs.getString("stil"));
                logger.traceExit(p);
                return Optional.of(p);
            }
        } catch (SQLException e) {
            logger.error("Error in findOne", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Proba> save(Proba entity) {
        logger.traceEntry("Adding new proba {}", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Proba(distance, stil) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, entity.getDistanta());
            statement.setString(2, entity.getStil());
            int result = statement.executeUpdate();
            if (result > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    entity.setId(id);
                    logger.trace("Generated id {}", id);
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            logger.error("Error in save", e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Proba> delete(Long id) {
        logger.traceEntry("Deleting proba with id {}", id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM Proba WHERE id=?")) {
            statement.setLong(1, id);
            int result = statement.executeUpdate();
            if (result > 0) {
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Error in delete", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Proba> update(Proba entity) {
        logger.traceEntry("Updating proba {}", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("UPDATE Proba SET distance=?, stil=? WHERE id=?")) {
            statement.setInt(1, entity.getDistanta());
            statement.setString(2, entity.getStil());
            statement.setLong(3, entity.getId());
            int result = statement.executeUpdate();
            if (result > 0) {
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Error in update", e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Proba> findProbaByStyleDistance(String style, int distance) {
        logger.traceEntry("Finding proba by distance {} and style {}", distance, style);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Proba WHERE distance=? AND stil=?")) {
            statement.setInt(1, distance);
            statement.setString(2, style);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Proba p = new Proba(rs.getLong("id"), rs.getInt("distance"), rs.getString("stil"));
                logger.traceExit(p);
                return Optional.of(p);
            }
        } catch (SQLException e) {
            logger.error("Error in findProbaByStyleDistance", e);
        }
        return Optional.empty();
    }
}
