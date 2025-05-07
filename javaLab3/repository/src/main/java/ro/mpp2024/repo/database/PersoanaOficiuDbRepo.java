package ro.mpp2024.repo.database;

import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.PersoanaOficiu;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.repo.DatabaseRepoUtils;
import ro.mpp2024.repo.EntityRepoException;
import ro.mpp2024.repo.IPersoanaOficiuRepo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class PersoanaOficiuDbRepo extends DatabaseRepoUtils<Integer, PersoanaOficiu> implements IPersoanaOficiuRepo {
    private static final Logger logger = LogManager.getLogger(PersoanaOficiuDbRepo.class);

    public PersoanaOficiuDbRepo(Properties properties) {
        super(properties);
        logger.info("Initializing PersoanaOficiuDbRepo with properties: {} " , properties);
    }

    @Override
    public PersoanaOficiu decodeResult(ResultSet resultSet) throws SQLException {
        var id = resultSet.getInt("id");
        var username = resultSet.getString("username");
        var password = resultSet.getString("password");
        var persoanaOficiu = new PersoanaOficiu(username, password);
        persoanaOficiu.setId(id);
        return persoanaOficiu;
    }

    @Override
    public PersoanaOficiu findbyCredentials( String username, String password) throws EntityRepoException {
        return selectFirst("select * from \"PersoanaOficiu\" where " + " \"username\"= ? and \"password\" = ?", username, password);
    }

    @Override
    public Optional<Inscriere> add(PersoanaOficiu persoanaOficiu) throws EntityRepoException{
        logger.trace("Inserting {}", persoanaOficiu);

        executeNonQuery("insert into \"PersoanaOficiu\" ( \"username\", \"password\") values (?,?)", persoanaOficiu.getUsername(), persoanaOficiu.getPassword());
        logger.info("Inserted succesfully");
        logger.traceExit();
        return null;
    }

    @Override
    public Optional<Proba> update(PersoanaOficiu persoanaOficiu) throws EntityRepoException{
        throw new EntityRepoException("PersoanaOficiu update is not allowed");
    }

    @Override
    public void remove(Integer integer) throws EntityRepoException{
        throw new EntityRepoException("PersoanaOficiu removal is not allowed");
    }

    @Override
    public PersoanaOficiu getById(Integer id) throws EntityRepoException{
        return selectFirst("select * from \"PersoanaOficiu\" where \"id\" = ?", id);
    }

    @Override
    public Iterable<PersoanaOficiu> getAll() throws EntityRepoException{
        return select("select * from \"PersoanaOficiu\" ");
    }
}
