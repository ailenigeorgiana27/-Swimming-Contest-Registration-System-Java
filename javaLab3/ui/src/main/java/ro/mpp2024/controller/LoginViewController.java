package ro.mpp2024.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.repo.EntityRepoException;
import ro.mpp2024.service.Service;

import java.io.IOException;
import java.util.Optional;

public class LoginViewController{
    private Service service;
    @FXML
    TextField usernameBox;
    @FXML
    TextField passwordBox;
    @FXML
    Button loginButton;
    @FXML
    private Label loginMessage;




    public void setService(Service service){
        this.service = service;
    }

    public void onLoginButtonClicked() throws EntityRepoException, IOException {
        var username = usernameBox.getText();
        var password = passwordBox.getText();
        Optional<PersoanaOficiu> foundPersoanaOficiu = service.loginPersoanaOficiu(username, password);
        if(foundPersoanaOficiu.isPresent()){
            openMainScene(foundPersoanaOficiu.get());
        }
        else{
            loginMessage.setText("Invalid username or password");
        }
    }

    private void openMainScene(PersoanaOficiu persoanaOficiu) throws IOException, EntityRepoException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("javaLab3/ui/src/main/resources/mainView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) loginMessage.getScene().getWindow();
        stage.setScene(scene);

        MainController controller = loader.getController();
        controller.setService(service);

        stage.show();
    }

}
