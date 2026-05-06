package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.FileInputStream;

public class SingletonConnection {

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties props = new Properties();
                props.load(new FileInputStream("conf.properties"));

                String url = props.getProperty("jdbc.url");
                String user = props.getProperty("jdbc.user");
                String password = props.getProperty("jdbc.password");

                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connexion OK");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
