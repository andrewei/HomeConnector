package aplication;

import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by andreas on 1/5/2016.
 */
public class FindPort {
    /**
     * Created by andreas on 1/5/2016.
     */
    List availablePorts = new ArrayList();

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


