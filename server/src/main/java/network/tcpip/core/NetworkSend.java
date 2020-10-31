package network.tcpip.core;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkSend {
    Thread sendJSONThread;

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
                    System.out.println("Error in sending json object");
                    e.printStackTrace();
                }
            }
        }
        sendJSONThread = new Thread(new playThread());
        sendJSONThread.start();
    }
}
