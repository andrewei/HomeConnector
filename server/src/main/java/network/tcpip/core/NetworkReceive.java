package network.tcpip.core;

import network.tcpip.helpers.IReceiveController;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkReceive {

    public void receive(IReceiveController controller, int port) throws IOException, ClassNotFoundException, InterruptedException {
        Thread receiveThreadInstance;

        class networkTread implements Runnable {
            InputStreamReader isr;
            BufferedReader br;
            JSONParser parser = new JSONParser();

            @Override
            public void run() {
                ServerSocket server = null;
                try {
                    server = new ServerSocket(port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String message;
                Socket socket = null;
                while (true) {
                    try {
                        System.out.println("Waiting for client request");
                        socket = server.accept();
                        isr = new InputStreamReader(socket.getInputStream());
                        br = new BufferedReader(isr);
                        message = br.readLine();
                        message = message.substring(message.indexOf("{"));
                        System.out.println("obj received");
                        JSONObject obj = (JSONObject) parser.parse(message);
                        System.out.println(message);
                        controller.receive(obj);
                    } catch (Exception e) {
                        System.out.println("Error when recieving file");
                        e.printStackTrace();
                    } finally {
                        try {
                            if (socket != null) {
                                socket.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        receiveThreadInstance = new Thread(new networkTread());
        receiveThreadInstance.start();
    }
}
