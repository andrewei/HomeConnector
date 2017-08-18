package com.example.weise.androidhc.network;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by weise on 18/08/2017.
 */

public class Network {
    private static Socket s;
    private static PrintWriter pw;
    String message = "";
    private static String ip = "192.168.0.101";

    public Network(String message){
        this.message = message;
        TcpTask tcpTask = new TcpTask();
        tcpTask.execute();
    }

    class TcpTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                s = new Socket(ip,9870);
                pw = new PrintWriter(s.getOutputStream());
                pw.write(message);
                pw.flush();
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
