package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesDataReader {
    //private static FileInputStream fileInputStream;
    private static Properties properties;

    public static String getPropertyValue(String propertyKey){
        try(FileInputStream fileInputStream = new FileInputStream("./src/test/resources/testAndSetupData.properties")){
            properties = new Properties();
            properties.load(fileInputStream);
        } catch(IOException exc){
            System.out.println(exc);
            properties = null;
        }
        return properties.getProperty(propertyKey);
    }
}
