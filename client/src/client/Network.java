package client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import sun.nio.ByteBuffered;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

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
                        if(controller.receive((JSONObject) obj)) {
                            socket = new Socket(socket.getInetAddress(), 8555);
                            String ping = socket.getLocalAddress().toString();
                            out = socket.getOutputStream();
                            out.write(ping.getBytes(Charset.forName("UTF-8")));
                            out.flush();
                        }
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

