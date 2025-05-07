package ro.mpp2024.utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.rpcProtocol.ClientRpcWorker;
import ro.mpp2024.service.IService;


import java.net.Socket;


public class ChatRpcConcurrentServer extends AbsConcurrentServer {
    private IService chatServer;
    private static Logger logger = LogManager.getLogger(ChatRpcConcurrentServer.class);
    public ChatRpcConcurrentServer(int port, IService chatServer) {
        super(port);
        this.chatServer = chatServer;
        logger.info("RpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientRpcWorker worker=new ClientRpcWorker(chatServer, client);
        logger.info("Creating worker for client " + client);
        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        logger.info("Stopping services ...");
    }
}
