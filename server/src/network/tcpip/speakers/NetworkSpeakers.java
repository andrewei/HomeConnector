package network.tcpip.speakers;

import org.json.simple.JSONObject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkSpeakers {
    Thread playThreadInstace;

    public void sendJSON(JSONObject inn, String ip)  {
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
