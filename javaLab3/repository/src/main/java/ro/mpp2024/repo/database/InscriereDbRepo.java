package ro.mpp2024.repo.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.repo.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class InscriereDbRepo extends DatabaseRepoUtils<Integer, Inscriere> implements IInscriereRepo {
    private static final Logger logger = LogManager.getLogger(InscriereDbRepo.class);

    IParticipantRepo participantRepo;
    IProbaRepo probaRepo;

    public InscriereDbRepo(Properties properties, IParticipantRepo participantRepo, IProbaRepo probaRepo) {
        super(properties);
        this.participantRepo = participantRepo;
        this.probaRepo = probaRepo;
    }

    @Override
    public Inscriere decodeResult(ResultSet result) throws SQLException, EntityRepoException {
        var id = result.getInt("id");
        var participantId = result.getInt("participant");
        var probaId = result.getInt("proba");
        var participant = participantRepo.getById(participantId);
        var proba = probaRepo.getById(probaId);
        var inscriere = new Inscriere(participant, proba);
        inscriere.setId(id);
        return inscriere;
    }


    @Override
    public Optional<Inscriere> add(Inscriere inscriere) throws EntityRepoException{
        logger.trace("Inserting {} ", inscriere);

        executeNonQuery("insert into \"Inscriere\" (\"participant\", \"proba\") values (?, ?)",
                inscriere.getParticipant().getId(),
                inscriere.getProba().getId());
        logger.info("Inserted successfully");
        logger.traceExit();
        return null;
    }

    @Override
    public Optional<Proba> update(Inscriere inscriere) throws EntityRepoException{
        logger.trace("Updating inscriere {} ", inscriere);
        executeNonQuery("update \"Inscriere\" set " +
                "\"participant\"=?," +
                "\"proba\" = ? where \"id\"= ?",
                inscriere.getParticipant().getId(),
                inscriere.getProba().getId(),
                inscriere.getId());
        logger.info("Updated inscriere {}",inscriere);
        logger.traceExit();
        return null;
    }

    @Override
    public void remove(Integer id) throws EntityRepoException{
        logger.trace("Removing inscriere id= {} ", id);
        executeNonQuery("delete from \"Inscriere\" where \"id\" = ?", id);
        logger.info("Removed inscriere id={}",id);
        logger.traceExit();
    }

    @Override
    public Inscriere getById(Integer id) throws EntityRepoException{
        return selectFirst("select * from \"Inscriere\" where \"id\" = ?", id);
    }

    @Override
    public Iterable<Inscriere> getAll() throws EntityRepoException{
        return select("select * from \"Inscriere\"");
    }
}
