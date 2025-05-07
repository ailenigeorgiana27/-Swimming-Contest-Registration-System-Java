package ro.mpp2024.domain.Validators;

import ro.mpp2024.domain.Participant;

public class ParticipantValidator implements Validator<Participant> {

    @Override
    public void validate(Participant participant) throws ValidationException {
        String err = "";
        if(participant == null)
            err = "Participantul e null\n";
        if(participant.getName().isEmpty())
            err += "Numele participantului e gol\n";
        if(participant.getAge() < 10)
            err += "Varsta invalida\n";


        if(err.length() > 0)
            throw new ValidationException(err);
    }
}