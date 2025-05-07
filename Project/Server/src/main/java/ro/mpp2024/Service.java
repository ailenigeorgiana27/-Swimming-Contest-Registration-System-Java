package ro.mpp2024;

import ro.mpp2024.DTO.TablesDTO;

import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.domain.Validators.ParticipantValidator;
import ro.mpp2024.service.IObserver;
import ro.mpp2024.service.IService;
import ro.mpp2024.service.ServiceException;

import java.lang.reflect.InaccessibleObjectException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements IService {

    private IParticipantRepo participantRepository;
    private IProbaRepo probaRepository;
    private IInscriereRepo participareRepository;
    private IPersoanaOficiuRepo oficiuRepository;
    private ParticipantValidator participantValidator = new ParticipantValidator();
    private Map<String, IObserver> loggedClients = new HashMap<>();
    private final int defaultThreadsNo = 5;


    public Service(IParticipantRepo partRepo, IProbaRepo probaRepo, IInscriereRepo participareRepo, IPersoanaOficiuRepo oficiuRepo) {
        participantRepository = partRepo;
        probaRepository = probaRepo;
        participareRepository = participareRepo;
        oficiuRepository = oficiuRepo;

    }

    public synchronized void login(String username, String password, IObserver client) {
        PersoanaOficiu oficiu = oficiuRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("Nu exista oficiu cu acest nume"));

        var passDecoded = oficiu.getPassword();
        if (passDecoded.equals(password)) {
            if (loggedClients.containsKey(username)) {
                throw new ServiceException("Oficiul este deja logat!");
            }
            loggedClients.put(username, client);
        }

        else throw new ServiceException("Parola incorecta!");

    }

    public HashMap<Participant, Integer> getParticipantsByProba(Proba proba) {
        if(proba == null)
            return new HashMap<>();

        proba = probaRepository
                .findProbaByStyleDistance(proba.getStil(), proba.getDistanta())
                .orElseThrow(() -> new ServiceException("Proba incorecta!"));

        HashMap<Participant, Integer> participanti = new HashMap<>();
        participareRepository.findParticipantiByProba(proba.getId()).forEach(
                participant -> {
                    int nrProbe = getNrProbeParticipant(participant.getId());
                    participanti.put(participant, nrProbe);
                });

        return participanti;
    }

    public synchronized HashMap<Proba, Integer> getAllProbeWithNoOfParticipanti() {
        HashMap<Proba, Integer> probe = new HashMap<>();
        probaRepository.findAll().forEach(proba -> {
            int nrParticipanti = getNoParticipantiByProba(proba.getId());
            probe.put(proba, nrParticipanti);
        });
        return probe;
    }

    public int getNrProbeParticipant(Long idParticipant){
        return participareRepository.findNoOfProbeDupaParticipanti(idParticipant);
    }


    public synchronized void inscriereParticipant(Participant participant, ArrayList<Proba> probe){
        if(probe.isEmpty())
            throw new ServiceException("Participantul nu a fost inscris la nicio proba!");

        else {
            if(!participantRepository.save(participant).isPresent()) {
                Long id = participantRepository.getMaxParticipantId();
                participant.setId(id);
            }
            else{
                throw new ServiceException("Participantul este deja inscris!");
            }
            probe.forEach(proba -> {
                participareRepository.save(new Inscriere(null, participant, proba));
            });
            notifyAllClients(participant, probe);
        }
    }

    public int getNoParticipantiByProba(Long idProba){
        return participareRepository.findNoOfParticipanti(idProba);
    }

    public List<Proba> getAllProbe(){
        return probaRepository.findAll();
    }

    private void notifyAllClients(Participant participant, ArrayList<Proba> probe) {
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (var client : loggedClients.entrySet()) {
            executor.execute(() -> {
                try {
                    client.getValue().update(participant, probe);
                } catch (Exception e) {
                    System.err.println("Eroare la notificarea clientului " + client.getKey() + ": " + e.getMessage());
                }
            });
        }
        executor.shutdown();
    }


    public synchronized void logout(PersoanaOficiu oficiu, IObserver client) {
        if (oficiu != null) {
            loggedClients.remove(oficiu.getUsername());
        } else {
            throw new ServiceException("Oficiul nu este logat!");
        }
    }



    @Override
    public synchronized TablesDTO updateTables(Proba proba) {
        HashMap<Participant, Integer> participanti = new HashMap<>();
        HashMap<Proba, Integer> probe = new HashMap<>();

        for (Proba p : probaRepository.findAll()) {
            probe.put(p, participareRepository.findNoOfParticipanti(p.getId()));
        }

        if(proba == null)
            return new TablesDTO(probe, participanti);
        for (Participant participant : getParticipantsByProba(proba).keySet()) {
            participanti.put(participant, participareRepository.findNoOfProbeDupaParticipanti(participant.getId()));
        }

        return new TablesDTO(probe, participanti);
    }


}