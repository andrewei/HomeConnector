package client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Network {
    private MainController main;
    private Thread thread;
    private NetworkController controller;

    public Network(MainController main) {
        this.main = main;
        this.controller = new NetworkController(main);
    }

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
                        String serverIP = socket.getInetAddress().toString().substring(1);
                        String localIP = socket.getLocalAddress().toString().substring(1);
                        controller.receive((JSONObject) obj, serverIP, localIP);
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
        thread = new Thread(new networkTread());
        thread.start();
    }

    public void sendJSON(JSONObject inn, String ip)  {
        class sendSocket implements Runnable {
            public void run(){
                Socket socket = null;
                try{
                    ObjectOutputStream oos = null;
                    socket = new Socket(ip, 8555);
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
        Thread thread = new Thread(new sendSocket());
        thread.start();
    }
}