package network.tcpip.speakers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class NetworkSpeakers {
    Thread playThreadInstace;
    NetworkRecieveController recieveController;

    public NetworkSpeakers(NetworkRecieveController recieveController) {
        this.recieveController = recieveController;
    }

    public void sendJSON(JSONObject inn, String ip)  {
        class playThread implements Runnable {
            public void run(){
                Socket socket = null;
                try{
                    ObjectOutputStream oos = null;
                    ObjectInputStream ois = null;
                    socket = new Socket(ip, 9876);
                    //write to socket using ObjectOutputStream
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(inn.toString());
                    oos.close();
                }
                catch (Exception e){
                    try {
                        if(socket != null) {
                            socket.close();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    //TODO fix better error message
                    //System.out.println("Error in sending json object");
                }
            }
        }
        playThreadInstace = new Thread(new playThread());
        playThreadInstace.start();
    }


    public void ping(JSONObject inn, final String baseIp)  {
        class pingThread implements Runnable {

            @Override
            public void run() {
                for(int i = 1; i < 265; i++) {
                    String ip = baseIp + String.valueOf(i);
                    sendJSON(inn, ip);
                }
            }
        }
        Thread thread = new Thread(new pingThread());
        thread.start();
    }

    public void recive() throws IOException, ClassNotFoundException, InterruptedException {
        class networkTread implements Runnable {

            @Override
            public void run() {
                int port = 8555;
                ServerSocket server = null;
                try {
                    server = new ServerSocket(port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONParser parser = new JSONParser();
                Object obj;
                Socket socket = null;
                BufferedReader br;
                OutputStream out = null;
                while (true) {
                    try {
                        System.out.println("Waiting for client request");
                        socket = server.accept();
                        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String jsonString = br.readLine();
                        //The stripping is to support both Java JSONObject and json from c#
                        String s2 = jsonString.substring(jsonString.indexOf("{"));
                        System.out.println("json" + s2);
                        obj = parser.parse(s2);
                        recieveController.receive((JSONObject) obj);
                    } catch (Exception e) {
                        System.out.println("Error when recieving file");
                        e.printStackTrace();
                    }
                    finally {
                        try {
                            if(socket != null) {
                                socket.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        Thread thread = new Thread(new networkTread());
        thread.start();
    }
}
