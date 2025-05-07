package ro.mpp2024.service;

import ro.mpp2024.DTO.TablesDTO;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.domain.Proba;

import java.util.ArrayList;
import java.util.HashMap;

public interface IService {

    void login(String username, String password, IObserver client) throws Exception;

    void logout(PersoanaOficiu oficiu, IObserver client) throws Exception;

    // Optional<Participant> addParticipant (String name, int age) throws ValidationException;

    void inscriereParticipant(Participant participant, ArrayList<Proba> probe) throws Exception;

    HashMap<Participant, Integer> getParticipantsByProba(Proba proba) throws Exception;

    TablesDTO updateTables(Proba proba) throws Exception;



}
