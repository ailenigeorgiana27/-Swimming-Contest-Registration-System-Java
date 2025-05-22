package ro.mpp2024.GUI;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ro.mpp2024.DTO.TablesDTO;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.service.IObserver;
import ro.mpp2024.service.IService;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MainWindowControllerU implements IObserver {

    private String oficiuUsername;
    private IService service;

    @FXML
    private TableView<Participant> participantTable;
    @FXML
    private TableColumn<Participant, String> nameColumn;
    @FXML
    private TableColumn<Participant, Integer> ageColumn;
    @FXML
    private TableColumn<Participant, Integer> probeColumn;

    @FXML
    private TableView<Proba> probaTable;
    @FXML
    private TableColumn<Proba, Integer> distanceColumn;
    @FXML
    private TableColumn<Proba, String> styleColumn;
    @FXML
    private TableColumn<Proba, Integer> participantsColumn;

    @FXML
    private ComboBox<Integer> distanceCombo;
    @FXML
    private ComboBox<String> styleCombo;

    @FXML
    private Button logoutBtn;
    @FXML
    private Button searchBtn;
    @FXML
    private Button registerBtn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;

    private ObservableMap<Participant, Integer> participants = FXCollections.observableHashMap();
    private ObservableMap<Proba, Integer> probe = FXCollections.observableHashMap();
    private Logger logger = Logger.getLogger("MainWindowController");

    public MainWindowControllerU(String oficiuUsername, IService service) {
        this.oficiuUsername = oficiuUsername;
        this.service = service;
    }

    public MainWindowControllerU(){
        // Default constructor for FXMLLoader
    }

    public void setService(IService service) {
        this.service = service;
    }

    public void setUser(String oficiuUsername) {
        this.oficiuUsername = oficiuUsername;
    }

    @FXML
    public void initialize() {
        probaTable.setItems(FXCollections.observableArrayList(probe.keySet()));
        participantTable.setItems(FXCollections.observableArrayList(participants.keySet()));

        distanceCombo.getItems().setAll(50, 200, 800, 1500);
        styleCombo.getItems().setAll("fluture", "spate", "liber", "mixt");

        distanceColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getDistanta()).asObject());

        styleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStil()));

        participantsColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(probe.get(cellData.getValue())).asObject());

        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        ageColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());

        probeColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(participants.get(cellData.getValue())).asObject());


        probaTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        logger.info("Initializing MainWindowController");


        searchBtn.setOnAction(e -> handleSearch());
        registerBtn.setOnAction(e -> handleRegister());
        logoutBtn.setOnAction(e -> handleLogout(e));
        refreshTables();

    }

    public void refreshTables() {
        try {
            Proba selected = null;
            if (distanceCombo.getValue() != null && styleCombo.getValue() != null) {
                selected = new Proba(0L, distanceCombo.getValue(), styleCombo.getValue());
            }
            TablesDTO dto = service.updateTables(selected);

            probe.clear();
            probe.putAll(dto.getProbe());

            participants.clear();
            participants.putAll(dto.getParticipanti());

            probaTable.setItems(FXCollections.observableArrayList(probe.keySet()));
            participantTable.setItems(FXCollections.observableArrayList(participants.keySet()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSearch() {
        if (styleCombo.getValue() != null && distanceCombo.getValue() != null) {
            Proba selected = new Proba(0L, distanceCombo.getValue(), styleCombo.getValue());
            try {
                Map<Participant, Integer> result = service.getParticipantsByProba(selected);
                participants.clear();
                participants.putAll(result);
                participantTable.setItems(FXCollections.observableArrayList(participants.keySet()));
            } catch (Exception e) {
                showError(e.getMessage());
            }
        }
    }

    private void handleRegister() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        ArrayList<Proba> selectedProbe = new ArrayList<>(probaTable.getSelectionModel().getSelectedItems());
        if (selectedProbe.isEmpty()) {
            showError("Selectati probele!");
            return;
        }


        Participant p = new Participant(0L, name, age);
        synchronized (this) {
            try {
                service.inscriereParticipant(p, selectedProbe);
            } catch (Exception e) {
                showError(e.getMessage());
            }
        }

    }

    public void handleLogout(ActionEvent event) {
        logout();
        Stage stage = (Stage) logoutBtn.getScene().getWindow();
        stage.hide();

    }

    public void logout(){
        try {
            service.logout(new PersoanaOficiu(0L, oficiuUsername, null), this);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @Override
    public void update(Participant participant, ArrayList<Proba> probes) throws Exception {
        try {
            for (Proba p : probes)
                probe.put(p, probe.get(p) + 1);  // << ACTUALIZEZI DOAR LOCAL
            probaTable.setItems(FXCollections.observableArrayList(probe.keySet()));
            if (styleCombo.getValue() != null && distanceCombo.getValue() != null) {
                Proba sel = new Proba(0L, distanceCombo.getValue(), styleCombo.getValue());
                if (probes.contains(sel)) {
                    participants.put(participant, probes.size());
                    participantTable.setItems(FXCollections.observableArrayList(participants.keySet()));
                }
            }

        } catch (Exception e) {
            logger.warning("Error while updating tables: " + e.getMessage());
            showError(e.getMessage());
        }
    }


}


