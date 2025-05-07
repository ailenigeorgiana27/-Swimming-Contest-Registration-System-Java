package ro.mpp2024;

import ro.mpp2024.domain.Participant;

public interface IParticipantRepo extends IRepo<Long, Participant> {
    public Long getMaxParticipantId();
}
