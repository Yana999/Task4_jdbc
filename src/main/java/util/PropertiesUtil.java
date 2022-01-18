package util;

import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtil {
    private final Properties PROPERTIES = new Properties();

    public PropertiesUtil(String name){
        loadProperties(name);
    }
    public String get(String key){
        return PROPERTIES.getProperty(key);
    }

    private void loadProperties(String name){
        try(InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(name)){
            PROPERTIES.load(inputStream);
        }catch(Exception e){
            System.out.println("Impossible to read properties");
        }
    }

}
