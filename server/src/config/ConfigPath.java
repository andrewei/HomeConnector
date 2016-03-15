package config;

import java.io.*;
import java.util.Properties;


public class ConfigPath {

    FileInputStream inputStream;
    private static Properties properties;
    FileOutputStream outputStream;

public ConfigPath(){
    properties = new Properties();
}

    public String readKey(String key){

        try {
            inputStream = new FileInputStream("f:/git/homeConnector/server/src/config/configPath.properties");
            properties.load(inputStream);
            String value = properties.getProperty(key);
            System.out.println("Writing out wanted attribute: " + key + " = " + value);
            return value;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void storeSong(String key, File file){
        try {
            outputStream = new FileOutputStream("f:/git/homeConnector/server/src/config/configPath.properties");
            String toBeStored = "file:///" + file;//list_music.getSelectionModel().getSelectedItem();
            toBeStored = toBeStored.replaceAll(" ", "%20");
            toBeStored = toBeStored.replaceAll("\\\\", "/");
            System.out.println("Writing out values to be stored: " + toBeStored);
            properties.setProperty(key, toBeStored);
            properties.store(outputStream, null);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void storeDirectory(String key, File file){
        try {
            outputStream = new FileOutputStream("f:/git/homeConnector/server/src/config/configPath.properties");
            System.out.println("Writing out values to be stored: " + file);
            properties.setProperty(key, "" + file);
            properties.store(outputStream, null);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
