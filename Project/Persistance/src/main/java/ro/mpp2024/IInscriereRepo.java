package ro.mpp2024;

import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.Participant;

import java.util.List;

public interface IInscriereRepo extends  IRepo<Long, Inscriere>{
    int findNoOfParticipanti(Long idProba);

    int findNoOfProbeDupaParticipanti(Long idParticipant);

    List<Participant> findParticipantiByProba(Long idProba);

}