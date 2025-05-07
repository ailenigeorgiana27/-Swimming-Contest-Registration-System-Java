package ro.mpp2024.repo.database;

import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.Participant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.repo.DatabaseRepoUtils;
import ro.mpp2024.repo.EntityRepoException;
import ro.mpp2024.repo.IParticipantRepo;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class ParticipantDbRepo extends DatabaseRepoUtils<Integer, Participant> implements IParticipantRepo {
    private static final Logger logger = LogManager.getLogger(ParticipantDbRepo.class);

    public ParticipantDbRepo(Properties properties) {
        super(properties);
        logger.info("Initializing ParticipantDbRepo with properties {} ", properties);
    }

    public Participant decodeResult(ResultSet result) throws SQLException {
        var name = result.getString("name");
        var age = result.getInt("age");
        var id = result.getInt("id");
        var participant = new Participant(name, age);
        participant.setId(id);
        return participant;
    }

    @Override
    public Optional<Inscriere> add(Participant participant) throws EntityRepoException {
        logger.trace("Inserting {}", participant);

        executeNonQuery("insert into \"Participant\" (\"name\", \"age\") values (?,?,)", participant.getName(), participant.getAge());
        logger.info("Inserted succesfully");
        logger.traceExit();
        return null;
    }

    @Override
    public Optional<Proba> update(Participant participant) throws EntityRepoException{
        throw new EntityRepoException("Participant update is not allowed");
    }

    @Override
    public void remove(Integer integer) throws EntityRepoException{
        throw new EntityRepoException("Participant removal is not allowed");
    }

    @Override
    public Participant getById(Integer id) throws EntityRepoException{
        return selectFirst("select * from \"Participant\" where \"id\" = ?", id);
    }

    @Override
    public Iterable<Participant> getAll() throws EntityRepoException{
        return select("select * from \"Participant\" ");
    }

    @Override
    public Iterable<Participant> filterByName(String name) throws EntityRepoException{
        return select("select * from \"Participant\" where \"name\" = ? ",
                name);
    }

    public Iterable<Participant> filterByAge(int age) throws EntityRepoException{
        return select("select * from \"Participant\" where \"age\" = ?",
                age);
    }
}
