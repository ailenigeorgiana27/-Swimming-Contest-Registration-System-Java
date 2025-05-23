package ro.mpp2024.SpringData;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ro.mpp2024.IPersoanaOficiuRepo;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.utils.JdbcUtils;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

@Component
public class PersoanaOficiuDBRepoS implements IPersoanaOficiuRepo {
    private static final Logger logger = LogManager.getLogger();
    private JdbcUtils dbUtils;

    @Autowired
    public PersoanaOficiuDBRepoS(Properties properties) {
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public Optional<PersoanaOficiu> findByUsername(String username) {
        logger.traceEntry("Autentificare persoanaOficiu {} / {}", username);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM PersoanaOficiu WHERE username = ?")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new PersoanaOficiu(rs.getLong("id"), rs.getString("username"), rs.getString("password")));
                }
            }
        } catch (SQLException e) {
            logger.error("Eroare la autentificare:", e);
        }
        logger.traceExit("PersoanaOficiu nu a fost găsită");
        return Optional.empty();
    }

    @Override
    public Optional<PersoanaOficiu> findOne(Long id) {
        logger.traceEntry("findOne persoanaOficiu {}", id);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM PersoanaOficiu WHERE id = ?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    return Optional.of(new PersoanaOficiu(id, username, password));
                }
            }
        } catch (SQLException e) {
            logger.error("Eroare la findOne:", e);
        }
        return Optional.empty();
    }

    @Override
    public List<PersoanaOficiu> findAll() {
        logger.traceEntry("findAll persoane oficiu");
        List<PersoanaOficiu> list = new ArrayList<>();
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM PersoanaOficiu");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                list.add(new PersoanaOficiu(id, username, password));
            }
        } catch (SQLException e) {
            logger.error("Eroare la findAll:", e);
        }
        return list;
    }

    @Override
    public Optional<PersoanaOficiu> save(PersoanaOficiu entity) {
        logger.traceEntry("Saving persoanaOficiu {}", entity);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO PersoanaOficiu(username, password) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getUsername());
            stmt.setString(2, entity.getPassword());
            int result = stmt.executeUpdate();
            if (result > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getLong(1));
                    }
                }
                return Optional.empty(); // înseamnă că a fost salvat cu succes
            }
        } catch (SQLException e) {
            logger.error("Eroare la salvare:", e);
        }
        return Optional.of(entity); // returnează entitatea dacă NU s-a salvat
    }

    @Override
    public Optional<PersoanaOficiu> delete(Long id) {
        logger.traceEntry("Deleting persoanaOficiu {}", id);
        Optional<PersoanaOficiu> found = findOne(id);
        if (found.isEmpty())
            return Optional.empty();
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM PersoanaOficiu WHERE id = ?")) {
            stmt.setLong(1, id);
            int result = stmt.executeUpdate();
            if (result > 0)
                return found;
        } catch (SQLException e) {
            logger.error("Eroare la ștergere:", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<PersoanaOficiu> update(PersoanaOficiu entity) {
        logger.traceEntry("Updating persoanaOficiu {}", entity);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(
                "UPDATE PersoanaOficiu SET username = ?, password = ? WHERE id = ?")) {
            stmt.setString(1, entity.getUsername());
            stmt.setString(2, entity.getPassword());
            stmt.setLong(3, entity.getId());
            int result = stmt.executeUpdate();
            if (result > 0)
                return Optional.empty(); // actualizare cu succes
        } catch (SQLException e) {
            logger.error("Eroare la actualizare:", e);
        }
        return Optional.of(entity); // nu s-a făcut actualizarea
    }

}
