package ro.mpp2024.domain;

import ro.mpp2024.domain.enums.Stil;

public class ProbaCuParticipanti extends Entity<Integer> {
    private float distanta;
    private Stil stil;
    private int nr;

    public ProbaCuParticipanti(float distanta, Stil stil) {
        this.distanta = distanta;
        this.stil = stil;
    }

    public ProbaCuParticipanti() {

    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public ProbaCuParticipanti(float distanta, Stil stil, int nr) {
        this.distanta = distanta;
        this.stil = stil;
        this.nr = nr;
    }

    public float getDistanta() {
        return distanta;
    }

    public void setDistanta(float distanta) {
        this.distanta = distanta;
    }

    public Stil getStil() {
        return stil;
    }

    public void setStil(Stil stil) {
        this.stil = stil;
    }
}
