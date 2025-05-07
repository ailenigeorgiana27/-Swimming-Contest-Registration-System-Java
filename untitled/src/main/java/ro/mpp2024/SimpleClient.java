package ro.mpp2024;

import java.io.IOException;
import java.net.Socket;

public class SimpleClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 55556)) {
            System.out.println("Connected to server on port 55556");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
