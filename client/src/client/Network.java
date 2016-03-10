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
                //create the socket server object
                int port = 9876;
                ServerSocket server = null;
                try {
                    server = new ServerSocket(port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject jsonInput;
                JSONParser parser = new JSONParser();
                Object obj;
                String message;
                Socket socket;
                ObjectInputStream ois;
                //keep listens indefinitely until receives 'exit' call or program terminates
                while (true) {
                    try {
                        System.out.println("Waiting for client request");
                        socket = server.accept();
                        ois = new ObjectInputStream(socket.getInputStream());
                        message = (String) ois.readObject();
                        obj = parser.parse(message);
                        jsonInput = (JSONObject) obj;
                        controller.receive(jsonInput);
                    } catch (Exception e) {
                        System.out.println("error when recieving file");
                        e.printStackTrace();
                    }
                    continue;
                }
            }
        }
        thread = new Thread(new networkTread());
        thread.start();
    }
}


