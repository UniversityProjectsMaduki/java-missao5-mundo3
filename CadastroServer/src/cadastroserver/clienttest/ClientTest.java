package cadastroserver.clienttest;

import java.io.*;
import java.net.Socket;

public class ClientTest {

    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

         

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        ClientTest client = new ClientTest();
        client.startConnection("127.0.0.1", 4321);

    }
}
