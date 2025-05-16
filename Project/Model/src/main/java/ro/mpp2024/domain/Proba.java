package ro.mpp2024.domain;



import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@jakarta.persistence.Entity
@Table(name="Proba")
public class Proba extends Entity<Long> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private int distanta;
    @Column(nullable = false)
    private String stil;


    public Proba() {}
    public Proba(Long id,int distanta, String stil) {
        this.setId(id);
        this.distanta = distanta;
        this.stil = stil;
    }




    public int getDistanta() {
        return distanta;
    }

    public void setDistanta(int distanta) {
        this.distanta = distanta;
    }

    public String getStil() {
        return stil;
    }

    public void setStil(String stil) {
        this.stil = stil;
    }

    @Override
    public String toString() {
        return "Proba{" +
                "distanta=" + distanta +
                ", stil=" + stil +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Proba that = (Proba) o;
        return Objects.equals(distanta, that.distanta) && Objects.equals(stil, that.stil);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distanta, stil);
    }

}