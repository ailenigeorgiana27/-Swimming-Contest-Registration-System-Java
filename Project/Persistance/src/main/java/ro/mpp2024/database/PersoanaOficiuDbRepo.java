package ro.mpp2024.database;

import ro.mpp2024.IPersoanaOficiuRepo;
import ro.mpp2024.JdbcUtils;
import ro.mpp2024.domain.PersoanaOficiu;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class PersoanaOficiuDbRepo implements IPersoanaOficiuRepo {
    protected JdbcUtils dbUtils;

    //protected static final Logger logger= LogManager.getLogger();

    public PersoanaOficiuDbRepo(Properties props) {
        // logger.info("Initializing PersoanaOficiuDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);


    }

    @Override
    public Optional<PersoanaOficiu> findByUsername(String username) {
        Connection con = dbUtils.getConnection();
        try (var statement = con.prepareStatement("select * from PersoanaOficiu where username = ?")) {
            statement.setString(1, username);
            var result = statement.executeQuery();
            if (result.next()) {
                return Optional.of(new PersoanaOficiu(result.getLong("id"), result.getString("username"), result.getString("password")));
            }
        } catch (Exception e) {
            // logger.error(e);
        }
        return Optional.empty();

    }

    @Override
    public Optional<PersoanaOficiu> findOne(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<PersoanaOficiu> findAll() {
        Connection con  = dbUtils.getConnection();
        List<PersoanaOficiu> entities = new ArrayList<>();

        try(var statement = con.prepareStatement("select * from PersoanaOficiu")) {
            var result = statement.executeQuery();
            while(result.next()) {
                entities.add(new PersoanaOficiu(result.getLong("id") ,result.getString("username") , result.getString("password")));
            }
        } catch (Exception e) {
            //logger.error(e);
        }

        return entities;
    }

    @Override
    public Optional<PersoanaOficiu> save(PersoanaOficiu entity) {
        return Optional.empty();
    }

    @Override
    public Optional<PersoanaOficiu> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<PersoanaOficiu> update(PersoanaOficiu entity) {
        return Optional.empty();
    }
}

