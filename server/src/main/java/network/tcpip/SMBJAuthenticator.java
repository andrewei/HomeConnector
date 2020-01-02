package network.tcpip;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import controller.MainController;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

public class SMBJAuthenticator {
    MainController controller;

    public SMBJAuthenticator(MainController controller) {
        this.controller = controller;
    }

    public void connect() {


        SmbConfig config = SmbConfig.builder()
                .withTimeout(60, TimeUnit.SECONDS) // Timeout sets Read, Write, and Transact timeouts (default is 60 seconds)
                .withSoTimeout(60, TimeUnit.SECONDS) // Socket Timeout (default is 0 seconds, blocks forever)
                .build();

        SMBClient client = new SMBClient(config);

        try {
            Connection connection = client.connect("FENRIZ-PC");
            AuthenticationContext ac = new AuthenticationContext("remote", "hunterhunter".toCharArray(), null);
            Session session = connection.authenticate(ac);
            System.out.println(session.getConnection().getConnectionInfo().toString());
            com.hierynomus.smbj.share.File smbFileRead = null;

            DiskShare share = (DiskShare) session.connectShare("d");
            /*
            // Connect to Share

            java.io.File file = new java.io.File("temp.mp3");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            FileInputStream fis = null;
            */

            int bufferSize = share.getTreeConnect().getSession().getConnection().getNegotiatedProtocol().getMaxReadSize();
            System.out.println("buffer size:"+bufferSize);
            byte[] buffer = new byte[bufferSize];
            int count;

            //download file
            String filePath = "test.mp3";
            File file = new File("temp");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            if(share.fileExists(filePath)) {
                smbFileRead = share.openFile(filePath, EnumSet.of(AccessMask.GENERIC_READ), null, SMB2ShareAccess.ALL,
                        SMB2CreateDisposition.FILE_OPEN, null);
                long offset = 0;
                long remaining = smbFileRead.getFileInformation().getStandardInformation().getEndOfFile();
                System.out.println("Remaining : " + remaining);
                try (FileOutputStream out = new FileOutputStream(file)) {
                    while (remaining > 0) {
                        int amount = remaining > buffer.length ? buffer.length : (int) remaining;
                        int amountRead = smbFileRead.read(buffer, offset, 0, amount);
                        if (amountRead == -1) {
                            remaining = 0;
                        } else {
                            out.write(buffer, 0, amountRead);
                            remaining -= amountRead;
                            offset += amountRead;
                        }
                    }
                }
                controller.musicController.playSong("file:///D:/git/HomeConnector/server/temp");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
