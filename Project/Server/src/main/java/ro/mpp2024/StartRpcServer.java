package ro.mpp2024;

import java.io.IOException;
import java.net.Socket;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.database.InscriereDbRepo;

import ro.mpp2024.database.ProbaDbRepo;
import ro.mpp2024.orm.ParticipantRepoORM;
import ro.mpp2024.orm.PersoanaOficiuRepoORM;
import ro.mpp2024.rpcProtocol.ClientRpcWorker;
import ro.mpp2024.utils.AbsConcurrentServer;
import ro.mpp2024.utils.ServerException;


public class StartRpcServer {
    private static int defaultPort = 55556;
    private static Logger logger = LogManager.getLogger(StartRpcServer.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(props.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            logger.debug("Wrong port number " + ex.getMessage());
            logger.debug("Using default port: " + defaultPort);
        }




        //PersoanaOficiuDbRepo oficiuDBRepository = new PersoanaOficiuDbRepo(props);
        //ParticipantDbRepo participantRepository = new ParticipantDbRepo(props);
        ProbaDbRepo probaRepository = new ProbaDbRepo(props);

        PersoanaOficiuRepoORM oficiuDBRepository = new PersoanaOficiuRepoORM();
        ParticipantRepoORM participantRepository = new ParticipantRepoORM();
        //ProbaRepoORM probaRepository = new ProbaRepoORM();
        InscriereDbRepo participareDBRepository = new InscriereDbRepo(props, participantRepository, probaRepository);
        //InscriereRepoORM participareDBRepository = new InscriereRepoORM(participantRepository, probaRepository);
        Service service = new Service(participantRepository, probaRepository, participareDBRepository, oficiuDBRepository);

        AbsConcurrentServer server = new AbsConcurrentServer(serverPort) {
            @Override
            protected Thread createWorker(Socket client) {
                ClientRpcWorker worker = new ClientRpcWorker(service, client);
                return new Thread(worker);
            }
        };

        try {
            server.start();
            System.out.println("Server started...");
        } catch (ServerException e) {
            logger.debug("Server exception " + e.getMessage());
        }
    }
}
