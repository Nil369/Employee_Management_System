package db;

import java.sql.Connection; // Import our helper class
import java.sql.DriverManager;
import java.sql.Statement;
import utils.ConfigLoader;

public class DBConnection {
    public Connection connection;
    public Statement statement;
    
    public DBConnection(){
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Load environment variables from .env
            String hostName = ConfigLoader.get("DB_HOST");
            String port = ConfigLoader.get("PORT");
            String dbName = ConfigLoader.get("DB_NAME");
            String user = ConfigLoader.get("DB_USER");
            String password = ConfigLoader.get("DB_PASSWORD");


            String url = "jdbc:mysql://" + hostName + ":" + port + "/" + dbName + "?useSSL=true&requireSSL=true";

            
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();

            System.out.println("Database connected successfully!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
