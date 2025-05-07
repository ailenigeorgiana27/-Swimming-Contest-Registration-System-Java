package ro.mpp2024.database;

import ro.mpp2024.IProbaRepo;
import ro.mpp2024.JdbcUtils;
import ro.mpp2024.domain.Proba;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ProbaDbRepo implements IProbaRepo {
    protected JdbcUtils dbUtils;

    // protected static final Logger logger= LogManager.getLogger();

    public ProbaDbRepo(Properties props) {
        //logger.info("Initializing ProbaDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);

    }


    @Override
    public Optional<Proba> findOne(Long aLong) {
        Connection con = dbUtils.getConnection();
        try(var statement = con.prepareStatement("select * from Proba where id = ?")){
            statement.setLong(1, aLong);
            var result = statement.executeQuery();
            if(result.next()){
                return Optional.of(new Proba(result.getLong("id"),result.getInt("distance"), result.getString("stil")));
            }

        }catch (Exception e){
            //logger.error(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Proba> findAll() {
        List<Proba> entities = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try(var statement = con.prepareStatement("select * from Proba")){
            var result = statement.executeQuery();
            while(result.next()){
                entities.add(new Proba(result.getLong("id"), result.getInt("distance"), result.getString("stil")));
            }
        }catch (Exception e){
            //logger.error(e);
        }

        return entities;
    }

    @Override
    public Optional<Proba> save(Proba entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Proba> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Proba> update(Proba entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Proba> findProbaByStyleDistance(String style, int distance){
        Connection con = dbUtils.getConnection();
        try(var statement = con.prepareStatement("SELECT * FROM Proba where distance = ? and stil = ?")){
            statement.setInt(1, distance);
            statement.setString(2, style);
            var result = statement.executeQuery();
            if(result.next()){
                return Optional.of(new Proba(
                        result.getLong("id"),
                        result.getInt("distance"),
                        result.getString("stil")));
            }
        }catch( SQLException e){
            //logger.error(e);
        }

        return Optional.empty();
    }
}
