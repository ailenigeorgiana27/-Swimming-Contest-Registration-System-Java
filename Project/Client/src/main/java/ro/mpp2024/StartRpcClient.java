package ro.mpp2024;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.GUI.LoginController;
import ro.mpp2024.GUI.LoginControllerU;
import ro.mpp2024.GUI.MainWindowController;
import ro.mpp2024.GUI.MainWindowControllerU;
import ro.mpp2024.client.grpc.ConcursServiceGrpc;
import ro.mpp2024.client.grpc.NotificationServiceGrpc;
import ro.mpp2024.rpcProtocol.ProxyRpc;
import ro.mpp2024.service.IService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


import java.io.IOException;
import java.util.Properties;

public class StartRpcClient extends Application {
    private static int defaultPort = 55556;
    private static String defaultServer = "localhost";
    private Logger logger = LogManager.getLogger(StartRpcClient.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClient.class.getResourceAsStream("/client.properties"));
        } catch (IOException e) {
            logger.info("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            logger.debug("Wrong port number " + ex.getMessage());
        }

        IService server = new ProxyRpc(serverIP, serverPort);
        //ManagedChannel channel=ManagedChannelBuilder.forAddress(serverIP, serverPort).usePlaintext().build();
        //System.out.println("Client running on"+" " + serverIP + ":" + serverPort);

        //ConcursServiceGrpc.ConcursServiceBlockingStub concursService = ConcursServiceGrpc.newBlockingStub(channel);
        //NotificationServiceGrpc.NotificationServiceStub notificationService = NotificationServiceGrpc.newStub(channel);

        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        //Parent root = loader.load();
        //LoginController ctrl = loader.getController();
        //ctrl.setStubs(concursService,notificationService);

        //FXMLLoader mloader = new FXMLLoader(getClass().getResource("/main.fxml"));
        //Parent mroot = mloader.load();
        //MainWindowController mainCtrl = mloader.getController();
        //mainCtrl.setStubs(concursService,notificationService);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        LoginControllerU ctrl = loader.getController();
        ctrl.setService(server);

        FXMLLoader mloader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent mroot = mloader.load();
        MainWindowControllerU mainCtrl = mloader.getController();
        mainCtrl.setService(server);
        ctrl.setMainController(mainCtrl);
        ctrl.setParent(mroot);

        primaryStage.setTitle("Platforma comp. inot");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();





    }

    public static void main(String[] args) {
        launch(args);

    }
}