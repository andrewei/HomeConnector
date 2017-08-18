package network.tcpip.remote;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by weise on 17/08/2017.
 */
public class NetworkRemote {

    public void receive(NetworkRemoteController remoteController) throws IOException, ClassNotFoundException, InterruptedException {
        Thread receiveThreadInstance;

        class networkTread implements Runnable {
            InputStreamReader isr;
            BufferedReader br;
            JSONParser parser = new JSONParser();

            @Override
            public void run() {
                //create the socket server object
                int port = 9870;
                ServerSocket server = null;
                try {
                    server = new ServerSocket(port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String message;
                Socket socket;
                while (true) {
                    try {
                        System.out.println("Waiting for client request");
                        socket = server.accept();
                        isr = new InputStreamReader(socket.getInputStream());
                        br = new BufferedReader(isr);
                        message = br.readLine();
                        JSONObject obj = (JSONObject) parser.parse(message);
                        System.out.println(message);
                        remoteController.receive(obj);
                    } catch (Exception e) {
                        System.out.println("error when recieving file");
                        e.printStackTrace();
                    }
                    continue;
                }
            }
        }
        receiveThreadInstance = new Thread(new networkTread());
        receiveThreadInstance.start();
    }
}
