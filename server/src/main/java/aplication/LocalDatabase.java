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
                String testAudio = "" + file;
                String format = "";
                if (file.length() >= 3) {
                    format = testAudio.substring(testAudio.length() - 3).toLowerCase();
                }
                if (
                    format.equals("mp3") ||
                    format.equals("mp4") ||
                    format.equals("wav")
                ) {
                    resultList.add(file);
                } else {
                    System.out.println("file removed : " + file);
                }
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
