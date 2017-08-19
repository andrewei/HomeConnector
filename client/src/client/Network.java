package client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by andreas on 1/15/2016.
 */
public class Network {
    Thread thread;
    NetworkController controller = new NetworkController();

    public void recive() throws IOException, ClassNotFoundException, InterruptedException {

        class networkTread implements Runnable {
            @Override
            public void run() {
                int port = 9876;
                ServerSocket server = null;
                try {
                    server = new ServerSocket(port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONParser parser = new JSONParser();
                Object obj;
                Socket socket;
                ObjectInputStream ois;
                while (true) {
                    try {
                        System.out.println("Waiting for client request");
                        socket = server.accept();
                        ois = new ObjectInputStream(socket.getInputStream());
                        obj = parser.parse((String) ois.readObject());
                        controller.receive((JSONObject) obj);
                    } catch (Exception e) {
                        System.out.println("Error when recieving file");
                        e.printStackTrace();
                    }
                }
            }
        }
        thread = new Thread(new networkTread());
        thread.start();
    }
}


