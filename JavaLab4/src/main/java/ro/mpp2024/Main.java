package ro.mpp2024;

import java.io.FileReader;
import java.io.IOException;

import java.util.Properties;

import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.repo.*;
import ro.mpp2024.domain.Entity;
import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.repo.database.InscriereDbRepo;
import ro.mpp2024.repo.database.ParticipantDbRepo;
import ro.mpp2024.repo.database.PersoanaOficiuDbRepo;
import ro.mpp2024.repo.database.ProbaDbRepo;
import ro.mpp2024.repo.utils.PasswordUtil;


public class Main {
    public static void main(String[] args) {
        var properties = loadProperties();
        var persoanaOficiuRepo = new PersoanaOficiuDbRepo(properties);
        var participantRepo = new ParticipantDbRepo(properties);
        var probaRepo = new ProbaDbRepo(properties);
        var inscriereRepo = new InscriereDbRepo(properties, participantRepo, probaRepo);
        try{
            persoanaOficiuRepo.add(new PersoanaOficiu("georgiana@icloud.com", PasswordUtil.hashPassword("123456")));
        }
        catch(EntityRepoException e){
            throw new RuntimeException(e);
        }
        try{
            System.out.println("am ajuns aici");
            inscriereRepo.add(new Inscriere(participantRepo.getById(2), probaRepo.getById(3) ));
            showWithMaskedPasswords(persoanaOficiuRepo);
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
    public static void showWithMaskedPasswords(PersoanaOficiuDbRepo repo) throws EntityRepoException {
        for (PersoanaOficiu persoana : repo.getAll()) {
            String maskedPassword = "●".repeat(10); // Afișează 10 buline negre indiferent de lungimea reală
            System.out.println("PersoanaOficiu { email=" + persoana.getUsername() + ", password=" + maskedPassword + " }");
        }
    }
}