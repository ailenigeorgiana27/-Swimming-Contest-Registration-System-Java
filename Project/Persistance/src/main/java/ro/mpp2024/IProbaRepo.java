package ro.mpp2024;

import ro.mpp2024.domain.Proba;

import java.util.Optional;

public interface IProbaRepo extends IRepo<Long, Proba>{
    public Optional<Proba> findProbaByStyleDistance(String style, int distance);

}
