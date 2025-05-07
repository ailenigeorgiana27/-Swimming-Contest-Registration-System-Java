package ro.mpp2024;

import java.io.FileReader;
import java.io.IOException;

import java.util.Properties;

import ro.mpp2024.repo.*;
import ro.mpp2024.domain.Entity;
import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.repo.database.InscriereDbRepo;
import ro.mpp2024.repo.database.ParticipantDbRepo;
import ro.mpp2024.repo.database.PersoanaOficiuDbRepo;
import ro.mpp2024.repo.database.ProbaDbRepo;


public class Main {
    public static void main(String[] args) {
        var properties = loadProperties();
        var persoanaOficiuRepo = new PersoanaOficiuDbRepo(properties);
        var participantRepo = new ParticipantDbRepo(properties);
        var probaRepo = new ProbaDbRepo(properties);
        var inscriereRepo = new InscriereDbRepo(properties, participantRepo, probaRepo);
        try{
            System.out.println("am ajuns aici");
            inscriereRepo.add(new Inscriere(participantRepo.getById(1), probaRepo.getById(1) ));
            show(persoanaOficiuRepo);
            show(participantRepo);
            show(probaRepo);
            show(inscriereRepo);
        }catch(EntityRepoException e){
            throw new RuntimeException(e);
        }
    }

    public static<ID, E extends Entity<ID>> void show(IRepo<ID, E> repo) throws EntityRepoException{
        repo.getAll().forEach(System.out::println);
    }

    public static Properties loadProperties() {
        Properties properties = new Properties();
        try{
            properties.load(new FileReader("bd.properties"));
        }catch (IOException e){
            System.out.println("Cannot find bd.config" + e);
        }
        return properties;
    }
}