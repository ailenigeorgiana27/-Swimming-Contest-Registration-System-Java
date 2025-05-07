package ro.mpp2024.network.dto;


import ro.mpp2024.domain.Proba;

public class ProbaDTO extends EntityDTO {
    private final String distance;
    private final String stil;
    private final Integer nrParticipants;

    public ProbaDTO(String distance, String stil, Integer nrParticipants) {
        this.distance = distance;
        this.stil = stil;
        this.nrParticipants = nrParticipants;
    }

    public String getDistance() {
        return distance;
    }

    public String getStil() {
        return stil;
    }

    public Integer getNrParticipants() {
        return nrParticipants;
    }

    // Convertire din Proba la ProbaDTO
    public static ProbaDTO fromProba(Proba proba) {
        return new ProbaDTO(
                proba.getDistance(),
                proba.getStil(),
                proba.getNrParticipants()
        );
    }

    // Convertire din ProbaDTO la Proba
    public Proba toProba() {
        Proba proba = new Proba(getDistance(), getStil());
        proba.setNrParticipants(getNrParticipants());
        proba.setId(getId()); // transferăm id-ul
        return proba;
    }
}

