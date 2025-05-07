package ro.mpp2024.domain;

public class Proba extends Entity<Integer>{
    private String distance;
    private String stil;
    private Integer nrParticipants;

    public Proba(String distance, String stil) {
        this.distance = distance;
        this.stil = stil;

    }
    public String getDistance() {
        return distance;
    }
    public void setDistance(String distance) {
        this.distance = distance;
    }
    public String getStil() {
        return stil;
    }
    public void setStil(String stil) {
        this.stil = stil;
    }
    public Integer getNrParticipants() {
        return nrParticipants;
    }
    public void setNrParticipants(Integer nrParticipants) {
        this.nrParticipants = nrParticipants;
    }

}
