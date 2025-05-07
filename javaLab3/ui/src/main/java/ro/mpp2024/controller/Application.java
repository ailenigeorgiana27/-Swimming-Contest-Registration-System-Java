package ro.mpp2024.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ro.mpp2024.repo.database.InscriereDbRepo;
import ro.mpp2024.repo.database.ParticipantDbRepo;
import ro.mpp2024.repo.database.PersoanaOficiuDbRepo;
import ro.mpp2024.repo.database.ProbaDbRepo;
import ro.mpp2024.service.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("javaLab3/ui/src/main/resources/loginView.fxml"));
        AnchorPane layout = fxmlLoader.load();
        stage.setScene(new Scene(layout));

        Service service = getService();
        LoginViewController loginController = fxmlLoader.getController();
        loginController.setService(service);

        stage.show();
    }

    static Service getService(){

        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        PersoanaOficiuDbRepo persoanaOficiuDbRepo = new PersoanaOficiuDbRepo(props);
        ProbaDbRepo probaDbRepo = new ProbaDbRepo(props);
        ParticipantDbRepo participantDbRepo = new ParticipantDbRepo(props);
        InscriereDbRepo inscriereDbRepo = new InscriereDbRepo(props, participantDbRepo, probaDbRepo);
        return new Service(persoanaOficiuDbRepo, participantDbRepo, probaDbRepo, inscriereDbRepo);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
