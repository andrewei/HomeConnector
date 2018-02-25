package network.tcpip.speakers;

import org.json.simple.JSONObject;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by andreas on 1/15/2016.
 */
public class NetworkSpeakers {
    Thread playThreadInstace;

    public void sendJSON(JSONObject inn, String ip)  {
        Thread thread;
        class playThread implements Runnable {
            public void run(){
                try{
                    Socket socket = null;
                    ObjectOutputStream oos = null;
                    ObjectInputStream ois = null;
                    socket = new Socket(ip, 9876);
                    //write to socket using ObjectOutputStream
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(inn.toString());
                    oos.close();
                }
                catch (Exception e){
                    //TODO fix better error message
                    System.out.println("Error in sending json object");
                }
            }
        }
        playThreadInstace = new Thread(new playThread());
        playThreadInstace.start();
    }
}
