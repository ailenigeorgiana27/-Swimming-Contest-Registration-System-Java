package ro.mpp2024.domain;

import java.io.Serializable;
import java.util.Objects;

public class Inscriere extends Entity<Long>  {

    private Participant participant;

    private Proba proba;
    public Proba getProba() {
        return proba;
    }
    public void setProba(Proba p) {
        this.proba = p;
    }
    public Inscriere(Long id,Participant participant, Proba proba) {
        super(id);
        this.participant = participant;
        this.proba = proba;
    }

   public Participant getParticipant() {
        return participant;
   }
   public void setParticipant(Participant p) {
        this.participant = p;
   }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Inscriere that = (Inscriere) o;
        return Objects.equals(proba, that.proba) && Objects.equals(participant, that.participant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proba, participant);
    }

}
