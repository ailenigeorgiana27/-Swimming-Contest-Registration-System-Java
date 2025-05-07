package ro.mpp2024.repo;

import ro.mpp2024.domain.PersoanaOficiu;

public interface IPersoanaOficiuRepo extends IRepo<Integer, PersoanaOficiu> {
    PersoanaOficiu findbyCredentials(String username, String password) throws EntityRepoException;
}
