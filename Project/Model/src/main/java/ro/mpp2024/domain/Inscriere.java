package ro.mpp2024.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@jakarta.persistence.Entity
@Table(name="Inscriere")
public class Inscriere extends Entity<Long>  {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="participant", nullable=false)
    private Participant participant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="proba", nullable=false)
    private Proba proba;
    public Proba getProba() {
        return proba;
    }
    public void setProba(Proba p) {
        this.proba = p;
    }
    public Inscriere(){}
    public Inscriere(Long id,Participant participant, Proba proba) {
        this.setId(id);
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