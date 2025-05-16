package ro.mpp2024;

import ro.mpp2024.domain.Participant;

import java.util.Optional;

public interface IParticipantRepo extends IRepo<Long, Participant> {
    public Long getMaxParticipantId();

    Optional<Participant> findByNameAndAge(String name, int age);
}
