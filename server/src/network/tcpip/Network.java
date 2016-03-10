package network.tcpip;

import org.json.simple.JSONObject;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


/**
 * Created by andreas on 1/15/2016.
 */
public class Network {

    Thread thread;

    public void sendJSON(JSONObject inn, String ip, int delay)  {

        class playThread implements Runnable {

            public void run(){
                try{
                    thread.sleep(delay);
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
                    System.out.println("Error in sending json object, fix error msg latewr");
                }
            }
        }
        Thread thread = new Thread(new playThread());
        thread.start();
    }
}
