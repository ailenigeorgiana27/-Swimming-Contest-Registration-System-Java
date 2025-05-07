package ro.mpp2024.database;

import ro.mpp2024.IParticipantRepo;
import ro.mpp2024.JdbcUtils;
import ro.mpp2024.domain.Participant;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ParticipantDbRepo implements IParticipantRepo {
    protected JdbcUtils dbUtils;

    // protected static final Logger logger= LogManager.getLogger();

    public ParticipantDbRepo(Properties props) {
        //logger.info("Initializing ParticipantDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);


    }

    @Override
    public Optional<Participant> findOne(Long aLong) {
        Connection con = dbUtils.getConnection();
        try(var statement = con.prepareStatement("select * from Participant where id = ?")) {
            statement.setLong(1, aLong);
            var result = statement.executeQuery();
            if(result.next()) {
                return Optional.of(new Participant(result.getLong("id"), result.getString("name"), result.getInt("age")));
            }
        } catch (Exception e) {
            //logger.error(e);
        }

        return Optional.empty();
    }

    @Override
    public List<Participant> findAll() {
        List<Participant> entities = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try(var statement = con.prepareStatement("select * from Participant")) {
            var result = statement.executeQuery();
            while(result.next()) {
                entities.add(new Participant(result.getLong("id"), result.getString("name"), result.getInt("age")));
            }
        } catch (Exception e) {
            //logger.error(e);
        }

        return entities;
    }

    @Override
    public Optional<Participant> save(Participant entity) {
        Connection con = dbUtils.getConnection();
        try(var statement = con.prepareStatement("insert into Participant (name, age) values (?, ?)")) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getAge());
            statement.executeUpdate();
            return Optional.empty();
        } catch (Exception e) {
            // logger.error(e);
        }

        return Optional.of(entity);
    }

    @Override
    public Optional<Participant> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Participant> update(Participant entity) {
        return Optional.empty();
    }

    @Override
    public Long getMaxParticipantId() {
        Connection con = dbUtils.getConnection();
        Long id = null;
        try(var statement = con.prepareStatement("select max(id) from Participant")){
            var result = statement.executeQuery();
            if(result.next()){
                return result.getLong(1);
            }
        }catch (Exception e) {
            //logger.error(e);
        }
        return id;
    }
}
