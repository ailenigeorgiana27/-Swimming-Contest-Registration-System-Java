package ro.mpp2024.DTO;

import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Proba;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class ParticipareDTO implements Serializable {
    Participant participant;
    ArrayList<Proba> probe;

    @Serial
    private static final long serialVersionUID = 1L;

    public ParticipareDTO(Participant part, ArrayList<Proba> probe) {
        this.participant = part;
        this.probe = probe;
    }

    public Participant getParticipant(){
        return participant;
    }

    public ArrayList<Proba> getProbe() {
        return probe;
    }


}
