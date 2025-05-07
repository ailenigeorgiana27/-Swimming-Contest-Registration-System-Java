package ro.mpp2024;

import ro.mpp2024.database.PersoanaOficiuDbRepo;
import ro.mpp2024.domain.PersoanaOficiu;

import java.util.Optional;

public interface IPersoanaOficiuRepo extends  IRepo<Long, PersoanaOficiu> {
    public Optional<PersoanaOficiu> findByUsername(String username);
}