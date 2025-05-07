package ro.mpp2024.service;

import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Proba;

import java.util.ArrayList;

public interface IObserver {
    void update(Participant participant, ArrayList<Proba> probe ) throws Exception;

}
