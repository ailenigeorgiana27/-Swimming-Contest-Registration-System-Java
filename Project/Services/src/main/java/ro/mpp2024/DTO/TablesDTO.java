package ro.mpp2024.DTO;

import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Proba;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TablesDTO implements Serializable {
    private HashMap<Proba, Integer> probe;
    private  HashMap<Participant, Integer> participanti;

    @Serial
    private static final long serialVersionUID = 1L;

    public TablesDTO(HashMap<Proba, Integer> probe, HashMap<Participant, Integer> participanti) {
        this.probe = probe;
        this.participanti = participanti;
    }

    public Map<Proba, Integer> getProbe() {
        return probe;
    }


    public Map<Participant, Integer> getParticipanti() {
        return participanti;
    }
}
