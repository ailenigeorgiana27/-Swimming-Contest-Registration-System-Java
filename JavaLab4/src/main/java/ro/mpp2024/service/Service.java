package ro.mpp2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.repo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
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

    public int countProbeForParticipant(Participant participant) {
        try {
            return inscriereRepo.getInscrierePentruParticipant(participant).size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void updateNrParticipants(Proba proba) throws EntityRepoException {
        List<Inscriere> inscriereList = inscriereRepo.getInscrierePentruProba(proba);
        proba.setNrParticipants(inscriereList.size());
    }

    public Participant saveParticipant(Participant participant) throws EntityRepoException {
        Optional<Inscriere> saved = participantRepo.add(participant);
        return saved.orElse(null).getParticipant();
    }


    public List<Proba> findAllProbe() throws EntityRepoException {
        List<Proba> probeList = new ArrayList<>();
        probaRepo.getAll().forEach(probeList::add);
        return probeList;
    }


    public List<Inscriere> getInscrierePentruProba(Proba proba) throws EntityRepoException {
        List<Inscriere> inscrieri = new ArrayList<>();
        System.out.println("DEBUG: Looking for inscriere for Proba: " + proba.getId()); // Debugging

        for (Inscriere inscriere : inscriereRepo.getInscrierePentruProba(proba)) {
            System.out.println("DEBUG: Found Inscriere for Participant " + inscriere.getParticipant().getName());
            inscrieri.add(inscriere);
        }

        System.out.println("DEBUG: Found " + inscrieri.size() + " inscriere(s) for Proba " + proba.getId());
        return inscrieri;
    }


    public List<Proba> findByDistanceAndStil(String distance, String stil) throws EntityRepoException {
        List<Proba> filteredProbes = new ArrayList<>();

        // Filtrăm probele după distanță și stil
        for (Proba proba : findAllProbe()) {
            if (proba.getDistance().equalsIgnoreCase(distance) && proba.getStil().equalsIgnoreCase(stil)) {
                filteredProbes.add(proba);
            }
        }

        return filteredProbes;
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
