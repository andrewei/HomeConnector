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
    ArrayList<String> connectedSpeakers = new ArrayList<>();

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


    public ArrayList<String> ping(JSONObject inn, final String baseIp)  {
        connectedSpeakers.clear();
        class pingThread implements Runnable {

            @Override
            public void run() {
                for(int i = 1; i < 265; i++) {
                    String ip = baseIp + String.valueOf(i);
                    sendJSON(inn, ip);
                }
                hearPing();
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Thread thread = new Thread(new pingThread());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("in ping : size = " + connectedSpeakers.size());
        return connectedSpeakers;
    }

    private void hearPing() {
        Thread thread;
        class networkTread implements Runnable {
            private ArrayList<String> connectedSpeakers;

            public networkTread(ArrayList<String> connectedSpeakers) {
                this.connectedSpeakers = connectedSpeakers;
            }

            @Override
            public void run() {
                int port = 8555;
                ServerSocket server = null;
                try {
                    server = new ServerSocket(port);
                    server.setSoTimeout(1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONParser parser = new JSONParser();
                Object obj;
                Socket socket = null;
                BufferedReader br;
                OutputStream out = null;
                long t = System.currentTimeMillis();
                long end = t+3000;
                while (System.currentTimeMillis() < end) {
                    try {
                        socket = null;
                        System.out.println("Waiting for client request");
                        socket = server.accept();
                        if(!socket.isConnected()) {
                            return;
                        }
                        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String message = br.readLine();
                        message = message.substring(1);
                        System.out.println("Message : " + message);
                        connectedSpeakers.add(message);

                    } catch (SocketTimeoutException e) {
                        System.out.println("No additional speakers found during time frame");
                    }
                    catch (Exception e) {
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
        thread = new Thread(new networkTread(connectedSpeakers));
        thread.start();
    }
}
