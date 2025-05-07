package ro.mpp2024.GUI;


import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import javafx.application.Platform;
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
import ro.mpp2024.client.grpc.ConcursServiceGrpc;
import ro.mpp2024.client.grpc.NotificationServiceGrpc;
import ro.mpp2024.client.grpc.Service;
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
import java.util.stream.Collectors;

public class MainWindowController implements IObserver {

    private String oficiuUsername;

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

    private ConcursServiceGrpc.ConcursServiceBlockingStub service;
    private NotificationServiceGrpc.NotificationServiceStub observer;

    public void setStubs(ConcursServiceGrpc.ConcursServiceBlockingStub bookingStub,
                         NotificationServiceGrpc.NotificationServiceStub notificationStub) {
        this.service = bookingStub;
        this.observer = notificationStub;
        subscribeToNotifications();
    }
    private void subscribeToNotifications() {
        observer.participantInscris(Empty.getDefaultInstance(), new StreamObserver<Service.Notification>() {
            @Override
            public void onNext(Service.Notification notification) {
                // A venit o notificare ⇒ reîncarci UI-ul pe thread-ul JavaFX:
                Platform.runLater(() -> {
                    try {
                        System.out.println("Am primit o notificare! Mesaj: " + notification.getMessage());
                        // Poți actualiza UI-ul sau reîncărca tabelele aici:
                        // De exemplu, afișezi mesajul notificării
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Notificare");
                        alert.setHeaderText("Mesaj nou:");
                        alert.setContentText(notification.getMessage());
                        alert.showAndWait();

                        // Dacă ai nevoie să actualizezi tabelele, o poți face aici
                        // De exemplu, reîmprospătezi datele pentru participanți/probe
                        refreshTables();
                        if (styleCombo.getValue() != null && distanceCombo.getValue() != null) {
                            handleSearch();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                // Dacă pică conexiunea, te poți reconecta aici:
                t.printStackTrace();
                // eventual: subscribeToNotifications();
            }

            @Override
            public void onCompleted() {
                // Server-ul a închis stream-ul (rare)
            }
        });
    }



    public MainWindowController(){
        // Default constructor for FXMLLoader
    }


    public void setUser(String oficiuUsername) {
        this.oficiuUsername = oficiuUsername;
    }

    @FXML
    public void initialize() {
        probaTable.setItems(FXCollections.observableArrayList(probe.keySet()));
        participantTable.setItems(FXCollections.observableArrayList(participants.keySet()));

        distanceCombo.getItems().setAll(50, 150, 200, 800, 1500);
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
    }

    public void refreshTables() {
        try {
            Service.ProbaCountList grpcList = service.getProbeWithCounts(Empty.getDefaultInstance());

            probe.clear();
            for (Service.ProbaCountEntry entry : grpcList.getEntriesList()) {
                Service.ProbaDTO grpcProba = entry.getProba();
                Proba localProba = new Proba(grpcProba.getId(), grpcProba.getDistanta(), grpcProba.getStil());
                probe.put(localProba, entry.getNrParticipanti());
            }

            // Optional: clear participant table if you don't have participant counts here
             ObservableList<Proba> updatedList = FXCollections.observableArrayList(probe.keySet());
            probaTable.setItems(updatedList);  // actualizează lista din tabel
            probaTable.refresh();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Eroare la încărcarea probelor: " + e.getMessage());
        }
    }



    private void handleSearch() {
        if (styleCombo.getValue() != null && distanceCombo.getValue() != null) {
            String style = styleCombo.getValue();
            int distance = distanceCombo.getValue();
            try {
                Service.StyleDistanceDTO dto = Service.StyleDistanceDTO.newBuilder()
                        .setStil(style)
                        .setDistanta(distance)
                        .build();

                // Step 2: Call the gRPC method
                Service.ParticipantList grpcList = service.getParticipantsByStyleDistance(dto);

                // Step 3: Convert gRPC participants to domain objects
                participants.clear();
                for (Service.ParticipantDTO grpcParticipant : grpcList.getParticipantsList()) {
                    Participant p = new Participant(
                            grpcParticipant.getId(),
                            grpcParticipant.getName(),
                            grpcParticipant.getAge()
                    );
                    participants.put(p, 1); // If count is unknown, default to 1
                }

                // Step 4: Update UI
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
                List<Long> probaIds = selectedProbe.stream()
                        .map(Proba::getId)
                        .collect(Collectors.toList());

                Service.InscriereDTO inscriereDTO = Service.InscriereDTO.newBuilder()
                        .setName(p.getName())
                        .setAge(p.getAge())
                        .addAllProbaIds(probaIds)
                        .build();

                service.addInscriere(inscriereDTO);
                refreshTables();
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
            Service.UserDTO userDTO = Service.UserDTO.newBuilder()
                    .setUsername(oficiuUsername)
                    .build();

            service.logout(userDTO);
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
                probe.put(p, probe.get(p) + 1);
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