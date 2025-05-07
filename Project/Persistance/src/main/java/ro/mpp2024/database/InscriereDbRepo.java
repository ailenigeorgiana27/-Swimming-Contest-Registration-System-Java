package ro.mpp2024.database;

import ro.mpp2024.IInscriereRepo;
import ro.mpp2024.IParticipantRepo;
import ro.mpp2024.IProbaRepo;
import ro.mpp2024.JdbcUtils;
import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Proba;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class InscriereDbRepo implements IInscriereRepo {
    protected JdbcUtils dbUtils;
    private final IParticipantRepo participantRepository;
    private final IProbaRepo probaRepository;
    //protected static final Logger logger= LogManager.getLogger();

    public InscriereDbRepo(Properties props, IParticipantRepo participantRepository, IProbaRepo probaRepository) {
        // logger.info("Initializing ParticipareDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
        this.participantRepository = participantRepository;
        this.probaRepository = probaRepository;

    }


    @Override
    public int findNoOfParticipanti(Long idProba) {
        Connection con = dbUtils.getConnection();
        int number = 0;
        try (var statement = con.prepareStatement("select count(*) from Inscriere where proba = ?")) {
            statement.setLong(1, idProba);
            var result = statement.executeQuery();
            if (result.next()) {
                number = result.getInt(1);
            }
        } catch (Exception e) {
            // logger.error(e);
        }

        return number;
    }

    @Override
    public int findNoOfProbeDupaParticipanti(Long idParticipant) {
        Connection con = dbUtils.getConnection();
        int number = 0;
        try (var statement = con.prepareStatement("select count(*) from Inscriere where participant = ?")) {
            statement.setLong(1, idParticipant);
            var result = statement.executeQuery();
            if (result.next()) {
                number = result.getInt(1);
            }
        } catch (Exception e) {
            //  logger.error(e);
        }

        return number;
    }


    @Override
    public Optional<Inscriere> findOne(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Inscriere> findAll() {
        List<Inscriere> entities = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try(var statement = con.prepareStatement("select * from Inscriere")){
            var result = statement.executeQuery();
            while(result.next()) {
                Long id = result.getLong("id");
                Long idParticipant = result.getLong("participant");
                Long idProba = result.getLong("proba");
                Participant participant = participantRepository.findOne(idParticipant).get();
                Proba proba = probaRepository.findOne(idProba).get();
                Inscriere participare = new Inscriere(id, participant, proba);
                entities.add(participare);


            }
        } catch (Exception e) {
            //logger.error(e);
        }

        return entities;
    }

    @Override
    public Optional<Inscriere> save(Inscriere entity) {
        Connection con = dbUtils.getConnection();
        try (var statement = con.prepareStatement("insert into Inscriere (proba, participant) values (?, ?)")) {
            statement.setLong(1, entity.getParticipant().getId());
            statement.setLong(2, entity.getProba().getId());
            statement.executeUpdate();
            return Optional.empty();
        } catch (Exception e) {
            // logger.error(e);
        }

        return Optional.of(entity);
    }

    @Override
    public Optional<Inscriere> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Inscriere> update(Inscriere entity) {
        return Optional.empty();
    }

    @Override
    public List<Participant> findParticipantiByProba(Long idProba){
        Connection con = dbUtils.getConnection();
        List<Participant> participants = new ArrayList<>();
        try(var statement = con.prepareStatement("select * from Inscriere where proba = ?"))
        {statement.setLong(1, idProba);
            var result = statement.executeQuery();
            while(result.next()) {
                Long idParticipant = result.getLong("participant");
                Participant participant = participantRepository
                        .findOne(idParticipant).orElseThrow(() -> new RuntimeException("Participant inexistent!"));
                participants.add(participant);
            }

        }catch(Exception e) {
            // logger.error(e);
        }

        return participants;
    }
}

