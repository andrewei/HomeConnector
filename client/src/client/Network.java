package client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
                int port = 9876;
                ServerSocket server = null;
                try {
                    server = new ServerSocket(port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONParser parser = new JSONParser();
                Object obj;
                Socket socket;
                ObjectInputStream ois;
                while (true) {
                    try {
                        System.out.println("Waiting for client request");
                        socket = server.accept();
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String jsonString = br.readLine();
                        //The stripping is to support both Java JSONObject and json from c#
                        String s2 = jsonString.substring(jsonString.indexOf("{"));
                        System.out.println("json" + s2);
                        obj = parser.parse(s2);
                        controller.receive((JSONObject) obj);
                    } catch (Exception e) {
                        System.out.println("Error when recieving file");
                        e.printStackTrace();
                    }
                }
            }
        }
        thread = new Thread(new networkTread());
        thread.start();
    }
}


/*
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
                Socket socket;
                //ObjectInputStream ois;
                DataInputStream ois;
                while (true) {
                    try {
                        System.out.println("Waiting for client request");
                        //socket = server.accept();
                        //BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        //String content = "";
                        //while(br.ready()){
                            //content += br.readLine();
                        //}
                        //System.out.println(content);
                        String content = "{\"ACTION\" : \"PLAY_NEWSONG\", \"SONG\" : \"D:/test.mp3\"}";
                        JSONParser p = new JSONParser();
                        JSONObject json = (JSONObject) parser.parse(content);
                        controller.receive(json);
                        //controller.receive((JSONObject) obj);
                    } catch (Exception e) {
                        System.out.println("Error when recieving file");
                        e.printStackTrace();
                    }
                }
            }
        }
        thread = new Thread(new networkTread());
        thread.start();
    }
 */

