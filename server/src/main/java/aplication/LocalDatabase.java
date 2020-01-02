package aplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalDatabase {

    List<File> resultList;

    public List<File> getFolderItems(String path) {
        long startTime = System.nanoTime();
        resultList = new ArrayList<File>();
        //adds all files in folder and subfolders;
        iterateAddAll(path);
        long endTime = System.nanoTime();
        System.out.println("Took "+(endTime - startTime) + " ns");
        return resultList;
    }

    private void iterateAddAll(String path) {
        File folder = new File(path);
        File[] fList = folder.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                String testAudio = file.toString().toLowerCase();
                String format = "";
                if (file.length() >= 3) {
                    format = testAudio.substring(testAudio.length() - 3);
                }
                if (
                    format.equals("mp3") ||
                    format.equals("mp4") ||
                    format.equals("wav")
                ) {
                    if(
                        //TODO Clients can't handle æ ø å at the moment, fix this, works in server player.
                        //TODO remove almanzo, dont ask ...
                        testAudio.indexOf('æ') == -1 &&
                        testAudio.indexOf('ø') == -1 &&
                        testAudio.indexOf('å') == -1 &&
                        !testAudio.contains("almanzo")
                    ) {
                        resultList.add(file);
                        continue;
                    }
                }
                System.out.println("file removed : " + file);
            } else if (file.isDirectory()) {
                try {
                    iterateAddAll(file.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
