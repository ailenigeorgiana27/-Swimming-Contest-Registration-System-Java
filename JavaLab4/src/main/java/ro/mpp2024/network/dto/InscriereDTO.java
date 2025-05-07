package ro.mpp2024.network.dto;

import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Proba;

public class InscriereDTO extends EntityDTO {
    private final ParticipantDTO participant;
    private final ProbaDTO proba;

    // Constructor
    public InscriereDTO(ParticipantDTO participant, ProbaDTO proba) {
        if (participant == null || proba == null) {
            throw new IllegalArgumentException("Participant and Proba must not be null");
        }
        this.participant = participant;
        this.proba = proba;
    }

    public ParticipantDTO getParticipant() {
        return participant;
    }

    public ProbaDTO getProba() {
        return proba;
    }

    // Convertire din Inscriere la InscriereDTO
    public static InscriereDTO fromInscriere(Inscriere inscriere) {
        if (inscriere == null) {
            throw new IllegalArgumentException("Inscriere must not be null");
        }
        // Convertim participantul și proba în DTO-uri
        ParticipantDTO participantDTO = ParticipantDTO.fromParticipant(inscriere.getParticipant());
        ProbaDTO probaDTO = ProbaDTO.fromProba(inscriere.getProba());

        return new InscriereDTO(participantDTO, probaDTO);
    }

    // Convertire din InscriereDTO la Inscriere
    public Inscriere toInscriere() {
        if (participant == null || proba == null) {
            throw new IllegalStateException("Cannot convert to Inscriere: participant or proba is null");
        }

        Participant participantEntity = participant.toParticipant();
        Proba probaEntity = proba.toProba();

        Inscriere inscriere = new Inscriere(participantEntity, probaEntity);
        inscriere.setId(getId()); // transferăm id-ul
        return inscriere;
    }
}
