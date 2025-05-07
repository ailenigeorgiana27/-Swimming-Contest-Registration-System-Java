package ro.mpp2024.network.dto;

import ro.mpp2024.domain.Participant;

public class ParticipantDTO extends EntityDTO {
    private final String name;
    private final int age;

    public ParticipantDTO(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    // Convertire din Participant la ParticipantDTO
    public static ParticipantDTO fromParticipant(Participant participant) {
        return new ParticipantDTO(participant.getName(), participant.getAge());
    }

    // Convertire din ParticipantDTO la Participant
    public Participant toParticipant() {
        Participant participant = new Participant(getName(), getAge());
        participant.setId(getId()); // transferăm id-ul
        return participant;
    }
}
