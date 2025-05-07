package ro.mpp2024.rpcProtocol;

import ro.mpp2024.DTO.OficiuDTO;
import ro.mpp2024.DTO.ParticipareDTO;
import ro.mpp2024.DTO.TablesDTO;
import ro.mpp2024.domain.Participant;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.service.IObserver;
import ro.mpp2024.service.IService;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ClientRpcWorker implements Runnable, IObserver {
    private final IService server;
    private final Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected = true;
    private static final Logger logger = Logger.getLogger(ClientRpcWorker.class.getName());

    public class LoggingOutputStream extends FilterOutputStream {
        public LoggingOutputStream(OutputStream out) { super(out); }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            System.out.println("Writing " + len + " bytes: " + bytesToHex(b, off, len));
            super.write(b, off, len);
        }

        private static String bytesToHex(byte[] bytes, int off, int len) {
            StringBuilder sb = new StringBuilder();
            for (int i = off; i < off + len; i++) {
                sb.append(String.format("%02x ", bytes[i]));
            }
            return sb.toString();
        }
    }

    public ClientRpcWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream( connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            logger.severe("Error initializing streams: " + e.getMessage());
            connected = false;
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Request request = (Request) input.readObject();
                logger.info("Received request: " + request);
                Response response = handleRequest(request);
                if (response != null) sendResponse(response);

            } catch (Exception e) {
                logger.severe("Error handling request: " + e.getMessage());
                connected = false;
            }
        }
        closeConnection();
    }

    private Response handleRequest(Request request) {
        //logger.info("Received request: " + request);
        try {
            Method method = this.getClass().getDeclaredMethod("handle" + request.type(), Request.class);
            return (Response) method.invoke(this, request);
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data("Unknown error").build();
        }
    }

    private void sendResponse(Response response) throws IOException {
        logger.info("Sending response: " + response);
        synchronized (output) {
            output.reset();
            output.writeObject(response);
            output.flush();
        }
    }

    private void closeConnection() {
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException ignored) {}
    }

    @Override
    public void update(Participant participant, ArrayList<Proba> probe) {

        try {
            sendResponse(new Response.Builder()
                    .type(ResponseType.PARTICIPANT_INSCRIS)
                    .data(new ParticipareDTO(participant, probe))
                    .build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response handleLogin(Request request) {
        try {
            OficiuDTO dto = (OficiuDTO) request.data();
            server.login(dto.getUsername(), dto.getPassword(), this);
            return new Response.Builder().type(ResponseType.OK).build();
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLogout(Request request) {
        try {
            server.logout((PersoanaOficiu) request.data(), this);
            connected = false;
            return new Response.Builder().type(ResponseType.OK).build();
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleRegister(Request request) {
        try {
            ParticipareDTO entry = (ParticipareDTO) request.data();
            server.inscriereParticipant(entry.getParticipant(), entry.getProbe());
            return new Response.Builder().type(ResponseType.OK).build();
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleFilter(Request request) {
        try {
            Proba proba = (Proba) request.data();
            HashMap<Participant, Integer> filtered = server.getParticipantsByProba(proba);
            return new Response.Builder().type(ResponseType.OK).data(filtered).build();
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleUpdateTables(Request request) {
        try {
            Proba proba = (Proba) request.data();
            TablesDTO updated = server.updateTables(proba);
            return new Response.Builder().type(ResponseType.OK).data(updated).build();
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
}
