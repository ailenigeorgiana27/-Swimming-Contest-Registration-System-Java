package ro.mpp2024.repo;

import ro.mpp2024.domain.Entity;
import ro.mpp2024.domain.Participant;

public interface IParticipantRepo extends IRepo<Integer, Participant> {
        Iterable<Participant> filterByName(String name) throws EntityRepoException;

        Iterable<Participant> filterByAge(int age) throws EntityRepoException;
}
