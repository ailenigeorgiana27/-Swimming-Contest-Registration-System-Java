package ro.mpp2024.rpcProtocol;

import ro.mpp2024.DTO.OficiuDTO;
import ro.mpp2024.DTO.ParticipareDTO;
import ro.mpp2024.DTO.TablesDTO;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.service.IObserver;
import ro.mpp2024.service.IService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProxyRpc implements IService {
    private final String host;
    private final int port;

    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> responses = new LinkedBlockingQueue<>();
    private volatile boolean finished;
    private Logger logger = LogManager.getLogger(ProxyRpc.class);

    public ProxyRpc(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private  void initializeConnection() throws Exception {
        try {
            connection = new Socket(host, port);
            input = new ObjectInputStream(connection.getInputStream());
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();

            finished = false;
            System.out.println("Connection established");
            startReader();
        } catch (IOException e) {
            throw new Exception("Error initializing connection: " + e.getMessage(), e);
        }

        logger.info("Connection initialized");
    }

    private  void closeConnection() {
        finished = true;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (connection != null) connection.close();
            client = null;
        } catch (IOException e) {
            logger.error("Error closing connection: " + e.getMessage(), e);

        }

        logger.info("Connection closed");
    }

    private  void sendRequest(Request request) throws Exception {
        try {
            logger.info("Sending request: " + request);
            output.reset();
            output.writeObject(request);
            output.flush();
            logger.info("Request sent");

        } catch (IOException e) {
            throw new Exception("Error sending request: " + e.getMessage());
        }
    }

    private  Response readResponse() throws Exception {
        try {
            return responses.take();
        } catch (InterruptedException e) {
            throw new Exception("Error reading response: " + e.getMessage());
        }
    }

    private void startReader() {
        Thread readerThread = new Thread(new ReaderThread());
        readerThread.start();
    }

    @Override
    public void login(String username, String password, IObserver client) throws Exception {
        logger.info("Login attempt with username: " + username);
        initializeConnection();
        this.client = client;

        OficiuDTO dto = new OficiuDTO(username, 0L, password);
        Request request = new Request.Builder().type(RequestType.Login).data(dto).build();
        sendRequest(request);

        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            closeConnection();
            throw new Exception(response.data().toString());
        }
    }

    @Override
    public void logout(PersoanaOficiu oficiu, IObserver client) throws Exception {
        logger.info("Logout attempt with username: " + oficiu.getUsername());
        Request request = new Request.Builder().type(RequestType.Logout).data(oficiu).build();
        sendRequest(request);
        Response response = readResponse();
        closeConnection();

        if (response.type() == ResponseType.ERROR) {
            throw new Exception(response.data().toString());
        }
    }

    @Override
    public void inscriereParticipant(Participant participant, ArrayList<Proba> probe) throws Exception {
        logger.info("Registering participant: " + participant.getName());
        ParticipareDTO dto = new ParticipareDTO(participant, probe);
        Request request = new Request.Builder().type(RequestType.Register).data(dto).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            throw new Exception(response.data().toString());
        }
    }

    @Override
    public TablesDTO updateTables(Proba proba) throws Exception {
        logger.info("Updating tables for proba: " + proba);
        Request request = new Request.Builder().type(RequestType.UpdateTables).data(proba).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.type() == ResponseType.OK) {
            return (TablesDTO) response.data();
        } else {
            logger.info("Error updating tables: " + response.data());
            throw new Exception(response.data().toString());
        }
    }

    @Override
    public HashMap<Participant, Integer> getParticipantsByProba(Proba proba) throws Exception {
        logger.info("Getting participants for proba: " + proba);
        Request request = new Request.Builder().type(RequestType.Filter).data(proba).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.type() == ResponseType.OK) {
            return (HashMap<Participant, Integer>) response.data();
        } else {
            throw new Exception(response.data().toString());
        }
    }

    private class ReaderThread implements Runnable {

        @Override
        public  void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    logger.debug("response received " + response);
                    if (((Response) response).type() == ResponseType.PARTICIPANT_INSCRIS) {
                        Response resp = (Response) response;
                        ParticipareDTO dto = (ParticipareDTO) resp.data();
                        client.update(dto.getParticipant(), dto.getProbe());
                    } else {
                        try {
                            responses.put((Response) response);
                        } catch (InterruptedException e) {
                            logger.error(e);
                            logger.error(e.getStackTrace());
                        }
                    }

                } catch (Exception e) {
                    logger.error("Error reading response", e);

                }
            }
        }
    }}

