package utils;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class UtilsNetwork {
    public static String getHostIP(boolean onlyBaseIP) {
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            System.out.println(socket.getLocalAddress().getHostAddress());
            if(!onlyBaseIP) {
                return socket.getLocalAddress().getHostAddress();
            }
            String[] addressArray = socket.getLocalAddress().getHostAddress().split("\\.");
            System.out.println(Arrays.toString(addressArray));
            String[] addressBaseArray = Arrays.copyOf(addressArray, addressArray.length-1);
            return String.join(".", addressBaseArray);
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        return "";
    }
}
