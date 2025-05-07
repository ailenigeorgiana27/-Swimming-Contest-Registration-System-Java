package ro.mpp2024.repo;
import ro.mpp2024.domain.Entity;
import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.Proba;

import java.util.Optional;

public interface IRepo<ID, E extends Entity<ID>> {
    Optional<Inscriere> add (E e) throws EntityRepoException;
    Optional<Proba> update (E e) throws EntityRepoException;
    void remove (ID id) throws EntityRepoException;
    E getById (ID id) throws EntityRepoException;
    Iterable<E> getAll () throws EntityRepoException;
}
