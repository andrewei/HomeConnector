package network.serial;

import controller.MainController;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by andreas on 1/5/2016.
 */
public class SerialConnector {

    private static int write;
    private static MainController mainController;

    public void setWrite(int inn){
        this.write = inn;
    }

    public SerialConnector(MainController mainController) {
        super();
        this.mainController = mainController;
        write = 0;
    }

    public void connect ( String portName ) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() ) {
            System.out.println("Error: Port is currently in use");
        }
        else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

            if ( commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();

                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out, write))).start();
                System.out.println("Serial in and out line to " + portName + " established OK");

            }
            else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    public static class SerialReader implements Runnable {
        InputStream in;

        public SerialReader ( InputStream in )
        {
            this.in = in;
        }

        public void run () {
            byte[] buffer = new byte[1024];
            int len = -1;
            try {
                while ( ( len = this.in.read(buffer)) > -1 ) {
                    String line = (new String(buffer,0,len));
                    if(line.equals("1")){
                        System.out.println("doorBellClicked");
                        mainController.tab5Controller.playSong(mainController.configPath.readKey("doorbellSong"));
                    }
                }
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    /** */
    public static class SerialWriter implements Runnable
    {
        OutputStream out;
        int write;

        public SerialWriter ( OutputStream out, int write ) {
            this.out = out; this.write = write;
        }

        public void run () {
            try {
                int c = 0;
                while(true){
                    Thread.sleep(0);
                    this.write = SerialConnector.write;
                    if(SerialConnector.write != -2){
                        this.out.write(SerialConnector.write);
                        SerialConnector.write = -2;
                    }
                }
            }
            catch ( IOException e ) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println();
                //e.printStackTrace();
            }
        }
    }
}
