package ro.mpp2024.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ro.mpp2024.domain.Inscriere;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.repo.EntityRepoException;
import ro.mpp2024.service.Service;

import java.io.IOException;
import java.util.*;


public class MainController {
    private Service service;

    ObservableList<Proba> modelList = FXCollections.observableArrayList();
    ObservableList<Participant> modelTable = FXCollections.observableArrayList();
    @FXML
    ListView<Proba> probaListView = new ListView<>();

    @FXML
    Button searchButton;

    @FXML
    Label messageLabel;

    @FXML
    TextField distanceField;
    @FXML
    TextField stilField;
    @FXML
    TableView<Participant> participantTable = new TableView<>();
    @FXML
    TableColumn<Participant, String> participantNameColumn;
    @FXML
    TableColumn<Participant, Integer> participantAgeColumn;
    @FXML
    TableColumn<Participant, Integer> nrProbeColumn;
    @FXML
    TextField participantNameField;
    @FXML
    TextField participantAgeField;

    private Proba selectedProba = null;

    public void setService(Service service) throws EntityRepoException {
        this.service = service;
        initModel();
    }
    private void initModel() throws EntityRepoException {

        for (Proba proba : service.findAllProbe()){
            int nrParticipantiInscrisi = 0;
            for(Inscriere inscriere : service.findAllInscrieri()){
                if(inscriere.getProba().equals(proba)){
                    nrParticipantiInscrisi++;
                }
            }
            proba.setNrParticipants(nrParticipantiInscrisi);
            service.updateProba(proba);
        }
        modelList.setAll(service.findAllProbe());
    }
    @FXML
    private void initialize() {
        probaListView.setItems(modelList);
        participantTable.setItems(modelTable);

        probaListView.setOnMouseClicked(event -> {
            Proba selected = probaListView.getSelectionModel().getSelectedItem();
            if(selected != null){
                selectedProba = selected;
            }
        });
    }
    public void onBtnSearchClicked() throws EntityRepoException {
        if(distanceField.getText()!= null && stilField.getText() != null){
            String distance = distanceField.getText();
            String stil = stilField.getText();
            if(service.findByDistanceAndStil(distance, stil) != null){
                messageLabel.setText("Proba found");
                Proba proba = service.findByDistanceAndStil(distance, stil);
                addInscriereColumn(proba);
            }
            else{
                messageLabel.setText("Proba not found");
            }
        }
        else{
            messageLabel.setText("Please select the values");
        }
    }

    private void addInscriereColumn(Proba proba) throws EntityRepoException {
        List<Inscriere> insrcieri = service.getInscrierePentruPoroba(proba);
        List<Participant> participanti = new ArrayList<>();
        Map<Participant, Integer> participantiInscrisi = new HashMap<>();



    }
    public void onBtnInscriereClicked() throws EntityRepoException {
        if(selectedProba != null && participantNameField.getText() != null && participantAgeField.getText() != null){
            String participantName = participantNameField.getText();
            int participantAge = Integer.parseInt(participantAgeField.getText());
            if(service.findByNameAge(participantName, participantAge) != null){
                Participant participant = service.findByNameAge(participantName, participantAge);
                Inscriere inscriere = new Inscriere(participant, selectedProba);
                Optional<Inscriere> saved = service.saveInscriere(inscriere);
                if(saved.isEmpty()){
                    messageLabel.setText("Inscriere saved");
                    initModel();
                }
                else{
                    messageLabel.setText("Inscriere was not saved");
                }
            }
            else{
                messageLabel.setText("Participant not found");
            }
        }
        else{
            messageLabel.setText("Please select the values");
        }
    }

    public void onBtnLogOutClicked() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("javaLab3/ui/src/main/resources/loginView.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.setScene(scene);

        LoginViewController controller = fxmlLoader.getController();
        controller.setService(service);

        stage.show();

    }


}
