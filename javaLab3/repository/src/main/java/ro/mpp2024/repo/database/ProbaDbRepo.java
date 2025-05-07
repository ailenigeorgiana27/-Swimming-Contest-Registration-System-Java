package ro.mpp2024.repo.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.repo.DatabaseRepoUtils;
import ro.mpp2024.repo.EntityRepoException;
import ro.mpp2024.repo.IProbaRepo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class ProbaDbRepo extends DatabaseRepoUtils<Integer, Proba> implements IProbaRepo {
    private static Logger logger = LogManager.getLogger(ProbaDbRepo.class);

    public ProbaDbRepo(Properties properties) {
        super(properties);
        logger.info("Initializing ProbaDbRepo with properties {} ", properties);
    }

    @Override
    public Proba decodeResult(ResultSet result) throws SQLException {
        var distance = result.getString("distance");
        var stil = result.getString("stil");
        var id = result.getInt("id");
        var proba = new Proba(distance, stil);
        proba.setId(id);
        return proba;
    }

    @Override
    public Optional<Inscriere> add(Proba proba) throws EntityRepoException {
        logger.trace("Inserting {} ", proba);
        executeNonQuery("insert into \"Proba\" " + "(\"distance\", \"stil\" ) " +
                        "values (?,?)",
                proba.getDistance(),
                proba.getStil());
        logger.info("Inserted successfully");
        logger.traceExit();
        return null;
    }

    @Override
    public Optional<Proba> update(Proba proba) throws EntityRepoException{
        var sql = "update \"Proba\" set" +
                "distance = ?, " +
                "stil = ?, " +
                "where id = ?";
        logger.trace("Updating proba {} ", proba);
        executeNonQuery(sql,
                proba.getDistance(),
                proba.getStil(),
                proba.getId());
        logger.info("Updated proba {} ", proba);
        logger.traceExit();
        return null;
    }

    @Override
    public void remove(Integer integer) throws EntityRepoException{
        throw new EntityRepoException("Proba removal is not allowed");
    }

    @Override
    public Proba getById(Integer id) throws EntityRepoException{
        return selectFirst("select * from \"Proba\" where id = ?", id);
    }

    @Override
    public Iterable<Proba> getAll() throws EntityRepoException{
        return select("select * from \"Proba\" ");
    }

    @Override
    public Iterable<Proba> filterByStil( String stil) throws EntityRepoException{
        return select("select * from \"Proba\" where stil = ?", stil);
    }
}
