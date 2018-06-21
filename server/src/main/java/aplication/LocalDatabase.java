package aplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalDatabase {

    List<File> resultList;
    public void printFolderItems(String path){

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
    }

    public List<File> getFolderItems(String path){
        resultList = new ArrayList<File>();
        //adds all files in folder and subfolders;
        iterateAddAll(path);
        return resultList;
    }

    private void iterateAddAll(String path)  {
        File folder = new File(path);
        File[] fList = folder.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                String testAudio = "" + file;
                if(testAudio.contains(".mp3") ||
                        testAudio.contains(".MP3") ||
                        testAudio.contains(".mp4") ||
                        testAudio.contains(".MP4") ||
                        testAudio.contains(".wav") ||
                        testAudio.contains(".WAV")
                        ){
                    resultList.add(file);
                }
                else{
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
