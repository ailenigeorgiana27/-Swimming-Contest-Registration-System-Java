package ro.mpp2024.service;

import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.repo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class Service {

    private final IPersoanaOficiuRepo persoanaOficiuRepo;
    private final IParticipantRepo participantRepo;
    private final IProbaRepo probaRepo;
    private final IInscriereRepo inscriereRepo;

    @Autowired
    public Service(IPersoanaOficiuRepo persoanaOficiuRepo, IParticipantRepo participantRepo, IProbaRepo probaRepo, IInscriereRepo inscriereRepo){
        this.persoanaOficiuRepo = persoanaOficiuRepo;
        this.participantRepo = participantRepo;
        this.probaRepo = probaRepo;
        this.inscriereRepo = inscriereRepo;
    }

    public Optional<PersoanaOficiu> loginPersoanaOficiu(String username, String password) throws EntityRepoException {
        for(PersoanaOficiu persoanaOficiu : persoanaOficiuRepo.getAll()){
            if(persoanaOficiu.getUsername().equals(username) && persoanaOficiu.getPassword().equals(password)){
                return Optional.of(persoanaOficiu);
            }
        }
        return Optional.empty();
    }

    public List<Proba> findAllProbe() throws EntityRepoException {
        List<Proba> probeList = new ArrayList<>();
        probaRepo.getAll().forEach(probeList::add);
        return probeList;
    }

    public List<Inscriere> getInscrierePentruPoroba(Proba proba) throws EntityRepoException {
        List<Inscriere> inscrieri = new ArrayList<>();
        for(Inscriere inscriere : inscriereRepo.getAll()){
            if(inscriere.getProba().equals(proba)){
                inscrieri.add(inscriere);
            }
        }
        return inscrieri;
    }

    public Proba findByDistanceAndStil(String distance, String stil) throws EntityRepoException {
        for(Proba proba: probaRepo.getAll()){
            if(proba.getDistance().equals(distance) && proba.getStil().equals(stil)){
                return proba;
            }
        }
        return null;
    }

    public Participant findByNameAge(String name, int age) throws EntityRepoException {
        for(Participant participant: participantRepo.getAll()){
            if(participant.getName().equals(name) && participant.getAge() == age){
                return participant;
            }
        }
        return null;

    }

    public Optional<Inscriere> saveInscriere(Inscriere inscriere) throws EntityRepoException {
        return inscriereRepo.add(inscriere);
    }

    public List<Inscriere> findAllInscrieri() throws EntityRepoException {
        List<Inscriere> inscrieri = new ArrayList<>();
        inscriereRepo.getAll().forEach(inscrieri::add);
        return inscrieri;
    }
    public Optional<Proba> updateProba(Proba proba) throws EntityRepoException {
        return probaRepo.update(proba);
    }
}
