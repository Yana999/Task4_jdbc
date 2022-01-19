package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionUtil {
    private static Connection connection;
    private ConnectionUtil(){}
    public static Connection getConnection(){
        if(connection == null){
            openConnection();
        }
        return connection;
    }
    private static void openConnection(){
        PropertiesUtil prop = new PropertiesUtil("database.properties");
        try{
            connection = DriverManager.getConnection(
                    prop.get("db.url"),
                    prop.get("db.user"),
                    prop.get("db.password"));
            System.out.println("Db connection successful");
        }catch (SQLException e){
            System.out.println("Error! Cannot open db connection.");
        }
    }
    public static void close(){
        try {
            if(!connection.isClosed()) {
                connection.close();
                System.out.println("connection closed");
            }
        }catch (SQLException e){
            System.out.println("Impossible to close db connection");
        }
    }
}
