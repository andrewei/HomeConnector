package network.serial;

import com.sun.xml.internal.bind.v2.TODO;
import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class FindPort {

    private List availablePorts = new ArrayList();

    public FindPort(){
        makeList();
    }

    private void makeList() {
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        if(ports == null){
            System.out.println("No connected devices trough seriall line");
        }
        while (ports.hasMoreElements()) {
            String currentOpenPort = ((CommPortIdentifier) ports.nextElement()).getName();
            System.out.println("AvailablePorts port: " + currentOpenPort);
            availablePorts.add(currentOpenPort);
        }
    }
    public List availablePorts() {
        return availablePorts;
    }
}


